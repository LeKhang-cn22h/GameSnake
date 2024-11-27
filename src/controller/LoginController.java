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
    private Button btnLogin; // Nút đăng nhập
    @FXML
    private Button btnRegister; // Nút đăng ký

    // Xử lý sự kiện đăng nhập
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText();  // Lấy tên người dùng
        String password = txtPassword.isVisible() ? txtPassword.getText() : txtPassShow.getText();  // Lấy mật khẩu (kiểm tra xem mật khẩu có đang hiển thị hay không)
        
        if (checkLogin(username, password)) {  // Kiểm tra thông tin đăng nhập
            saveUserToFile(username); // Lưu tên người dùng vào file
            proceedToGame(); // Chuyển sang màn hình game
        } else {
            showAlert("Đăng nhập thất bại", "Tên người dùng hoặc mật khẩu không đúng");  // Hiển thị thông báo nếu đăng nhập thất bại
        }
    }

    // Kiểm tra thông tin đăng nhập với cơ sở dữ liệu
    private boolean checkLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // Nếu có kết quả trả về thì đăng nhập thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  // Nếu có lỗi trong quá trình kết nối, trả về false
        }
    }

    // Lưu tên người dùng vào file
    private void saveUserToFile(String username) {
        String filePath = "user.txt";  // Đường dẫn file lưu tên người dùng
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(username);  // Ghi tên người dùng vào file
        } catch (IOException e) {
            e.printStackTrace();  // Nếu có lỗi trong quá trình ghi file, in ra lỗi
        }
    }

    // Chuyển sang màn hình game
    public void proceedToGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();  // Đóng cửa sổ đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));  // Thiết lập giao diện mới cho cửa sổ game
            newStage.setTitle("Game Snake");
            newStage.show();  // Hiển thị cửa sổ game
            newStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));  // Thêm biểu tượng cho cửa sổ game
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi nếu có ngoại lệ
        }
    }

    // Phương thức khởi tạo, kiểm tra xem người dùng đã đăng nhập chưa
    @FXML
    private void initialize() {
        if (isUserLoggedIn()) {  // Nếu người dùng đã đăng nhập
            btnLogin.setText("Tiếp tục");  // Thay đổi text của nút đăng nhập thành "Tiếp tục"
        }
    }

    // Kiểm tra xem người dùng đã đăng nhập hay chưa (dựa trên file lưu tên người dùng)
    private boolean isUserLoggedIn() {
        File file = new File("user.txt");  // Kiểm tra sự tồn tại của file lưu tên người dùng
        return file.exists();  // Nếu file tồn tại, người dùng đã đăng nhập
    }

    // Hiển thị thông báo cho người dùng
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);  // Tiêu đề của thông báo
        alert.setContentText(message);  // Nội dung thông báo
        alert.showAndWait();  // Hiển thị thông báo và đợi người dùng phản hồi
    }

    // Xử lý sự kiện khi người dùng nhấn nút đăng ký
    @FXML
    private void handleRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/sign.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnRegister.getScene().getWindow();
            stage.close();  // Đóng cửa sổ đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));  // Thiết lập giao diện cho cửa sổ đăng ký
            newStage.setTitle("Đăng ký");
            newStage.show();  // Hiển thị cửa sổ đăng ký
            newStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));  // Thêm biểu tượng cho cửa sổ đăng ký
        } catch (Exception e) {
            e.printStackTrace();  // In lỗi nếu có ngoại lệ
        }
    }

    // Xử lý sự kiện Show/Hide mật khẩu
    @FXML
    private void handleShowHide() {
        boolean isPasswordVisible = txtPassword.getSkin() instanceof javafx.scene.control.skin.TextFieldSkin;  // Kiểm tra xem mật khẩu có đang hiển thị hay không
        if (isPasswordVisible) {
            txtPassword.setSkin(null);  // Ẩn mật khẩu
            btnShowHide.setText("Hide");  // Thay đổi text nút thành "Hide"
        } else {
            txtPassword.setSkin(new javafx.scene.control.skin.TextFieldSkin(txtPassword));  // Hiển thị mật khẩu
            btnShowHide.setText("Show");  // Thay đổi text nút thành "Show"
        }
    }
}
