package controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DatabaseConnection;
public class SignController {
	 @FXML
	    private TextField txtUsername;
	    @FXML
	    private PasswordField txtPassword;
	    @FXML
	    private PasswordField txtConfirmPassword;
	    @FXML
	    private Button btnRegister;
	    @FXML
	    private Button btnLogin;
	    @FXML
	    private void handleRegister() {
	        String username = txtUsername.getText();
	        String password = txtPassword.getText();
	        String confirmPassword = txtConfirmPassword.getText();
	        try (Connection conn = DatabaseConnection.getConnection()) {
	            String query = "SELECT COUNT(*) FROM users WHERE username = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, username);
	            ResultSet rs = stmt.executeQuery();
	            rs.next();
	            if (rs.getInt(1)>0) {
		            // Hiển thị thông báo username đã tồn tại
		            Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setContentText("Username này đã tồn tại! Vui lòng đặt tên khác.");
		            alert.showAndWait();
		            return;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        if(password.length()<6) {
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setContentText("Mật khẩu cần ít nhất 6 kí tự!");
	            alert.show();
	        	return;
	        }
	        if (!password.equals(confirmPassword)) {
	            // Hiển thị cảnh báo nếu mật khẩu không khớp
	            Alert alert = new Alert(AlertType.ERROR);
	            alert.setContentText("Mật khẩu không khớp.");
	            alert.show();
	            return;
	        }

	        // Kết nối đến database và thêm tài khoản mới
	        try (Connection conn = DatabaseConnection.getConnection()) {
	            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setString(1, username);
	            stmt.setString(2, password);

	            stmt.executeUpdate();
	            
	            // Hiển thị thông báo thành công và điều hướng về trang Đăng nhập
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setContentText("Đăng ký thành công! Vui lòng đăng nhập.");
	            alert.showAndWait();

	            handleLogin(); // Điều hướng về trang Đăng nhập
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    // Phương thức điều hướng về trang Đăng nhập
	    @FXML
	    private void handleLogin() {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/interface.fxml"));
	            Parent root = loader.load();
	            Stage stage = (Stage) btnLogin.getScene().getWindow();
	            stage.setScene(new Scene(root));
	            stage.setTitle("Đăng nhập");
	            stage.show();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
}}
