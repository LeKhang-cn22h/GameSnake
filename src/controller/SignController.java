package controller;

import DAO.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;

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

    private UserDAO userDAO;

    public SignController() {
        userDAO = new UserDAO(); // Khởi tạo đối tượng UserDAO để xử lý các thao tác liên quan đến người dùng
    }

    /**
     * Xử lý sự kiện đăng ký người dùng mới
     */
    @FXML
    private void handleRegister() {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        // Kiểm tra các trường thông tin nhập vào
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            showAlert(AlertType.ERROR, "Lỗi", "Mật khẩu cần ít nhất 6 kí tự!");
            return;
        }

        // Kiểm tra mật khẩu xác nhận có khớp không
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Lỗi", "Mật khẩu không khớp.");
            return;
        }

        // Kiểm tra xem username đã tồn tại chưa
        if (userDAO.checkUsernameExists(username)) {
            showAlert(AlertType.ERROR, "Lỗi", "Username này đã tồn tại! Vui lòng đặt tên khác.");
            return;
        }

        // Tạo đối tượng User mới và lưu vào cơ sở dữ liệu
        User newUser = new User(username, password);
        int result = userDAO.insert(newUser);

        // Kiểm tra kết quả đăng ký
        if (result > 0) {
            showAlert(AlertType.INFORMATION, "Thành công", "Đăng ký thành công! Vui lòng đăng nhập.");
            handleLogin(); // Điều hướng về trang Đăng nhập
        } else {
            showAlert(AlertType.ERROR, "Lỗi", "Đăng ký không thành công. Vui lòng thử lại.");
        }
    }

    /**
     * Xử lý sự kiện đăng nhập, chuyển đến giao diện đăng nhập
     */
    @FXML
    private void handleLogin() {
        try {
            // Tải giao diện đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/interface.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Đăng nhập");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Hiển thị thông báo lỗi hoặc thành công
     * @param alertType Loại thông báo (ERROR, INFORMATION, ...)
     * @param title Tiêu đề thông báo
     * @param message Nội dung thông báo
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
