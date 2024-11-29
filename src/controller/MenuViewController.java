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
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MenuViewController {

    @FXML
    private Label playerLabel;
    @FXML
    private Button startButton;
    private MediaPlayer mediaPlayer;
    private MediaPlayer menuMediaPlayer;
    @FXML
    private BorderPane borderPane = new BorderPane();

    // Đọc tên người dùng từ file
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

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.menuMediaPlayer = mediaPlayer;
    }

    // Phát nhạc menu
    public void playMenuMusic() {
        if (menuMediaPlayer != null) {
            menuMediaPlayer.play();
        }
    }

    @FXML
    private void initialize() {
        String username = readUsernameFromFile();
        playerLabel.setText(username); // Cập nhật tên người dùng vào Label
        String imagePath = getClass().getResource("/view/image_nen/nen.gif").toExternalForm(); // Đường dẫn hình ảnh nền
        borderPane.setStyle("-fx-background-image:url('" + imagePath + "'); " +
                "-fx-background-size: cover; " +
                "-fx-background-position: center;");
        
        // Khởi tạo Media và MediaPlayer
        Media media = new Media(getClass().getResource("/view/music/NhacNen.ogg").toExternalForm());
        if (media.getError() != null) {
            System.out.println("Lỗi khi tải media: " + media.getError().getMessage());
        }
        
        if (media.getError() == null) {
            mediaPlayer = new MediaPlayer(media);
            
            if (mediaPlayer != null) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Phát nhạc liên tục
                mediaPlayer.setVolume(1.0); // Đảm bảo âm lượng tối đa
                mediaPlayer.setOnError(() -> {
                    System.out.println("Lỗi trong MediaPlayer: " + mediaPlayer.getError().getMessage());
                });
                mediaPlayer.play(); // Bắt đầu phát nhạc
            } else {
                System.out.println("Không thể khởi tạo MediaPlayer.");
            }
        } else {
            System.out.println("Lỗi: Không thể tải tệp Media.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        mediaPlayer.stop(); // Dừng nhạc nền khi đăng xuất
        Alert logoutAlert = new Alert(AlertType.CONFIRMATION);
        logoutAlert.setTitle("Thông báo");
        logoutAlert.setContentText("Bạn có chắc chắn muốn đăng xuất!");

        ButtonType yesButton = new ButtonType("Có");
        ButtonType closeButton = new ButtonType("Đóng", ButtonType.CLOSE.getButtonData());
        logoutAlert.getButtonTypes().setAll(yesButton, closeButton);

        logoutAlert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                deleteData();  // Xóa dữ liệu trong file
                switchToInterface(event);  // Chuyển về giao diện menu
            }
        });
    }

    // Xóa nội dung trong file user.txt
    private void deleteData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt"))) {
            writer.write("");  // Ghi chuỗi rỗng để xóa nội dung file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Chuyển về giao diện menu (interface.fxml)
    @FXML
    private void switchToInterface(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/interface.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            stage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openGameView(ActionEvent event) {
        mediaPlayer.stop(); // Dừng nhạc nền khi bắt đầu game
        try {
            // Tải FXML cho GameView
            Parent gameViewRoot = FXMLLoader.load(getClass().getResource("/view/GameModeView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(gameViewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openRankingView(ActionEvent event) {
        mediaPlayer.stop(); // Dừng nhạc nền khi mở bảng xếp hạng
        try {
            // Tải FXML cho bảng xếp hạng
            Parent rankingViewRoot = FXMLLoader.load(getClass().getResource("/view/Leaderboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(rankingViewRoot));
            stage.show();
            stage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));
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
                "1.Game có nhiều chế độ để người chơi có thể khám phá!\n" +
                "2. Dùng các phím mũi tên để điều khiển rắn:\n" +
                "   - Lên (↑), Xuống (↓), Trái (←), Phải (→).\n" +
                "3. Cố gắng ăn mồi để tăng điểm.\n" +
                "4. Tránh va chạm vào thân rắn, tường hoặc chướng ngại vật(nếu có) để không bị thua.\n" +
                "Game có 4 chế độ chơi:\n" +
                "Chế độ 1: chế độ cổ điển\n" +
                "Chế độ 2: Chế độ tự do\n" +
                "Chế độ 3: Chế độ vượt chướng ngại vật\n" +
                "Chế độ 4: Chế độ thử thách\n" +
                "Trong chế độ 4 sẽ có các loại mồi đặc biệt đang chờ bạn khám phá.\n" +
                
                "Chúc bạn chơi game vui vẻ!"
        );

        ButtonType closeButton = new ButtonType("Đóng", ButtonType.CLOSE.getButtonData());
        helpAlert.getButtonTypes().add(closeButton);

        helpAlert.showAndWait();
    }

    @FXML
    private void openSetting() {
        try {
            // Tải FXML cho SettingController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SettingView.fxml"));
            Parent root = loader.load();
            // Tạo cửa sổ mới cho cài đặt
            Stage newStage = new Stage();
            newStage.setTitle("Cài đặt");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(playerLabel.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
