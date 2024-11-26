package controller;

import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {
    @FXML
    private TextField txtUsername; // Tên người dùng
    @FXML
    private PasswordField txtPassword = new PasswordField(); // Mật khẩu
    @FXML
    private TextField txtPassShow = new TextField(); // Show/Hide mật khẩu
    @FXML
    private Button btnShowHide;
    @FXML
    private Button btnLogin; // nút đăng nhập
    @FXML
    private Button btnRegister; // Nút đăng ký

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String passwordShow = txtPassShow.getText();

        if (checkLogin(username, password) && checkLogin(username, passwordShow)) {
            saveUserToFile(username); // Lưu tên người dùng vào file
            proceedToGame(); // Chuyển sang màn hình game
        } else {
            showAlert("Đăng nhập thất bại", "Tên người dùng hoặc mật khẩu không đúng");
        }
    }

    private boolean checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveUserToFile(String username) {
    	String filePath = "user.txt";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void proceedToGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close(); // Đóng cửa sổ đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Game Snake");
            newStage.show();
            newStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        if (isUserLoggedIn()) {
            btnLogin.setText("Tiếp tục");
        }
    }

    private boolean isUserLoggedIn() {
        File file = new File("user.txt");
        return file.exists();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sign.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close(); // Đóng cửa sổ đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Đăng ký");
            newStage.show();
            newStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowHide() {
        if (txtPassword.isVisible()) {
            // Hiển thị mật khẩu
            txtPassword.setVisible(false);
            txtPassShow.setVisible(true);
            txtPassShow.setText(txtPassword.getText());
            btnShowHide.setText("Hide");
        } else {
            // Ẩn mật khẩu
            txtPassword.setVisible(true);
            txtPassShow.setVisible(false);
            txtPassword.setText(txtPassShow.getText());
            btnShowHide.setText("Show");
        }
    }
}
