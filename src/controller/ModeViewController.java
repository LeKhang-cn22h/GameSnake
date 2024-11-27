package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ModeViewController {
    
    // Khai báo các nút điều khiển chế độ chơi
    @FXML
    private Button btnMode1;
    @FXML
    private Button backMenu;

    // Hàm xử lý khi người dùng chọn chế độ chơi
    @FXML
    private int getmodeGame(ActionEvent event) {
        Button btnM = (Button) event.getSource(); // Lấy nút đã nhấn
        // Kiểm tra tên của nút và lưu chế độ chơi tương ứng
        switch (btnM.getText()) {
            case "Cổ Điển":
                SharedData.setSelectedMode(1); // Chế độ 1: Cổ điển
                break;
            case "Tự Do":
                SharedData.setSelectedMode(2); // Chế độ 2: Tự do
                break;
            case "Chướng ngại":
                SharedData.setSelectedMode(3); // Chế độ 3: Chướng ngại
                break;
            case "Thách thức":
                SharedData.setSelectedMode(4); // Chế độ 4: Thách thức
                break;
            default:
                SharedData.setSelectedMode(1); // Mặc định là chế độ Cổ điển
                break;
        }
        // Đóng cửa sổ hiện tại (chế độ chọn)
        Stage stage = (Stage) btnMode1.getScene().getWindow();
        stage.close(); 
        
        try {
            // Tải giao diện GameView để chuyển sang màn hình game
            Parent gameViewRoot = FXMLLoader.load(getClass().getResource("/view/GameView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            // Đặt giao diện mới cho cửa sổ hiện tại
            stage.setScene(new Scene(gameViewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
        return SharedData.getSelectedMode(); // Trả về chế độ đã chọn
    }

    // Hàm xử lý khi người dùng nhấn nút quay lại menu
    @FXML
    private void handleBackMenu(ActionEvent event) {
        try {
            // Tải giao diện menu chính
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            // Đóng cửa sổ hiện tại (chế độ chọn)
            Stage stage = (Stage) backMenu.getScene().getWindow();
            stage.close(); 
            // Tạo cửa sổ mới cho menu
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Game Snake"); // Tiêu đề cửa sổ
            newStage.show();
            // Thêm icon cho cửa sổ mới
            newStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));
            // Đặt focus vào root để nhận sự kiện bàn phím
            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }
}
