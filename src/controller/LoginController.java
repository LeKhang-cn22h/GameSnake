package controller;
import javafx.scene.control.Alert;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
//import java.io.IOException;
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
	 private Button btnLogin;// nút đăng nhập
	 @FXML
	 private Button btnRegister; // Nút đăng ký
	 @FXML
	private void handleLogin() {
		String username= txtUsername.getText();
		String password=txtPassword.getText();
		if (checkLogin(username,password)) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameView.fxml"));
	            Parent root = loader.load();
	            Stage stage = (Stage) btnRegister.getScene().getWindow();
	            // Đóng cửa sổ đăng nhập
	            stage.close(); 
	            // Tạo một cửa sổ mới cho trang đăng ký
	            Stage newStage = new Stage();
	            newStage.setScene(new Scene(root));
	            newStage.setTitle("Game Snake");
	            newStage.show();
				showAlert("đăng nhập thành công","chào mừng"+username+"!");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		else {
			showAlert("đăng nhập thất bại","tên người dùng hoặc mật khẩu không đúng");
		}
	}
	private boolean checkLogin(String username, String password) {
		String querry="SELECT * FROM users WHERE username =? AND password=?";
		try (Connection conn=DatabaseConnection.getConnection();
				PreparedStatement stmt=conn.prepareStatement(querry)){
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs =stmt.executeQuery();
			return rs.next();
			}catch(SQLException e) {
				e.printStackTrace();
				return false;
			}
	}
	private void showAlert(String title, String message) {
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
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
            // Đóng cửa sổ đăng nhập
            stage.close(); 
            // Tạo một cửa sổ mới cho trang đăng ký
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Đăng ký");
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowHide() {
        //Ràng buộc txtPassword bằng với txtPassShow
        

        if (txtPassword.isVisible()) {
            // Hiển thị mật khẩu (hiện TextField, ẩn txtPassword)
            txtPassword.setVisible(false);
            txtPassShow.setVisible(true);
            txtPassShow.setText(txtPassword.getText());
            btnShowHide.setText("Hide");
        } else {
            // Ẩn mật khẩu (hiện txtPassword, ẩn TextField)
            txtPassword.setVisible(true);
            txtPassShow.setVisible(false);
            txtPassword.setText(txtPassShow.getText());
            btnShowHide.setText("Show");
        }
    }
	
}
