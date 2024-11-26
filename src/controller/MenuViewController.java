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

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.menuMediaPlayer = mediaPlayer;
    }

    // Phương thức để phát nhạc menu
    public void playMenuMusic() {
        if (menuMediaPlayer != null) {
            menuMediaPlayer.play();
        }
    }

    @FXML
    private void initialize() {
        String username = readUsernameFromFile();
        playerLabel.setText(username); // Cập nhật tên người dùng vào Label
        
        // Khởi tạo Media và MediaPlayer
        // Chú ý: Đảm bảo đường dẫn đúng, sử dụng "toExternalForm()" để lấy đường dẫn đúng khi tải từ resources
        Media media = new Media(getClass().getResource("/view/music/NhacNen.ogg").toExternalForm());
        // Kiểm tra lỗi khi tải Media
        if (media.getError() != null) {
            System.out.println("Error loading media: " + media.getError().getMessage());
        }
        
        // Kiểm tra xem MediaPlayer có được khởi tạo thành công không
        if (media.getError() == null) {
            mediaPlayer = new MediaPlayer(media);
            
            // Đảm bảo MediaPlayer đã được khởi tạo
            if (mediaPlayer != null) {
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Phát nhạc liên tục
                mediaPlayer.setVolume(1.0); // Đảm bảo âm lượng tối đa
                mediaPlayer.setOnError(() -> {
                    System.out.println("Error in MediaPlayer: " + mediaPlayer.getError().getMessage());
                });
                mediaPlayer.play(); // Bắt đầu phát nhạc
            } else {
                System.out.println("Failed to initialize MediaPlayer.");
            }
        } else {
            System.out.println("Error: Media file could not be loaded.");
        }
    }


    @FXML
    private void logout(ActionEvent event) {
    	mediaPlayer.stop(); 
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
    @FXML
    private void switchToInterface(ActionEvent event) {
        try {
            // Chuyển về giao diện menu (interface.fxml)
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
    	 mediaPlayer.stop();
        try {
            // Tải FXML cho bảng xếp hạng
            Parent rankingViewRoot = FXMLLoader.load(getClass().getResource("/view/Leaderboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(rankingViewRoot));
            stage.show();
            stage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));

            // Tiếp tục phát nhạc nếu nó không đang phát
            
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
    @FXML
    private void openSetting() {
        try {
            // Tải fxml cho SettingController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SettingView.fxml"));
            Parent root = loader.load();
            // Tạo một cửa sổ mới
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
