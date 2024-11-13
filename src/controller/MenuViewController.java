package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;

public class MenuViewController {


    @FXML
    private Label playerLabel;
    @FXML
    private Button startButton;
 

    // Phương thức đọc tên người dùng từ file
    private String readUsernameFromFile() {
        File file = new File("user.txt");
        String username = "Player"; // Mặc định nếu không tìm thấy file
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                username = reader.readLine(); // Đọc tên người dùng từ file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return username;
    }

    
    @FXML
    private void initialize() {
        String username = readUsernameFromFile();
        playerLabel.setText( username); // Cập nhật tên người dùng vào Label
    }

    @FXML
    private void logout(ActionEvent event) {
        Alert logoutAlert = new Alert(AlertType.CONFIRMATION);
        logoutAlert.setTitle("Thông báo");
        logoutAlert.setContentText("Bạn có chắc chắn muốn đăng xuất!");
        
        ButtonType yesButton = new ButtonType("Có");
        ButtonType closeButton = new ButtonType("Đóng", ButtonType.CLOSE.getButtonData());
        logoutAlert.getButtonTypes().setAll(yesButton, closeButton);

        logoutAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                deleteData();  // Xóa dữ liệu trong file user.txt
                switchToInterface(event);  // Chuyển về giao diện interface.fxml
            }
        });
    }

    // Phương thức xóa nội dung file user.txt
    private void deleteData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt"))) {
            writer.write("");  // Ghi chuỗi rỗng để xóa nội dung file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức chuyển về giao diện interface.fxml
    private void switchToInterface(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/interface.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openGameView(ActionEvent event) {
        try {
            // Tải FXML cho GameView
            Parent gameViewRoot = FXMLLoader.load(getClass().getResource("/view/GameView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(gameViewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   

    @FXML
    private void openRankingView(ActionEvent event) {
        try {
            // Tải FXML cho bảng xếp hạng
            Parent rankingViewRoot = FXMLLoader.load(getClass().getResource("/view/Leaderboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(rankingViewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void showHelpDialog(ActionEvent event) {
        Alert helpAlert = new Alert(AlertType.NONE);
        helpAlert.setTitle("Hướng dẫn chơi game Rắn săn mồi");
        helpAlert.setHeaderText("Cách chơi game Rắn săn mồi trên laptop");
        helpAlert.setContentText(
                "1. Dùng các phím mũi tên để điều khiển rắn:\n" +
                "   - Lên (↑), Xuống (↓), Trái (←), Phải (→).\n" +
                "2. Cố gắng ăn mồi để tăng điểm.\n" +
                "3. Tránh va chạm vào thân rắn hoặc tường để không bị thua.\n" +
                "Chúc bạn chơi game vui vẻ!"
        );

        ButtonType closeButton = new ButtonType("Đóng", ButtonType.CLOSE.getButtonData());
        helpAlert.getButtonTypes().add(closeButton);

        helpAlert.showAndWait();
    }
}
