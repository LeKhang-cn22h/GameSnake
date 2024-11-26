package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;

import DAO.ScoreDAO;
import model.Rank;

public class RankingController {
	@FXML
	private Label labMode;
	@FXML
	private MenuButton menuMode;
    @FXML
    private Button btnBack;
    @FXML
    private TableView<Rank> tableView;

    @FXML
    private TableColumn<Rank, Integer> rankColumn;
    @FXML
    private TableColumn<Rank, String> usernameColumn;
    @FXML
    private TableColumn<Rank, Integer> scoreColumn;
    @FXML
    private TableColumn<Rank, String> dateColumn;

    private MediaPlayer rankingMediaPlayer; // MediaPlayer cho nhạc bảng xếp hạng
    private MediaPlayer menuMediaPlayer; // MediaPlayer cho nhạc menu

    /**
     * Hàm khởi tạo bảng xếp hạng
     */
    @FXML
    private void initialize() {
        // Cài đặt các cột
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        // Tạo MediaPlayer cho nhạc bảng xếp hạng và phát nhạc
        Media rankingMusic = new Media(getClass().getResource("/view/music/NhacNen.ogg").toString());
        rankingMediaPlayer = new MediaPlayer(rankingMusic);
        rankingMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp lại nhạc
        rankingMediaPlayer.play();

        // Dừng nhạc menu nếu có
        if (menuMediaPlayer != null) {
            menuMediaPlayer.stop();
            System.out.println("Menu music stopped!");
        }
    }

    /**
     * Truyền MediaPlayer cho nhạc menu khi vào bảng xếp hạng
     * @param mediaPlayer Đối tượng MediaPlayer của nhạc menu
     */
    public void setMenuMediaPlayer(MediaPlayer mediaPlayer) {
        this.menuMediaPlayer = mediaPlayer;
    }

    /**
     * Xử lý sự kiện nút "Quay lại"
     * @param event sự kiện ActionEvent
     */
    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            // Dừng nhạc bảng xếp hạng khi quay lại menu
            if (rankingMediaPlayer != null) {
                rankingMediaPlayer.stop();
                System.out.println("Ranking music stopped!");
            }

            // Chuyển cảnh về menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            

            // Truyền MediaPlayer nhạc menu vào controller menu
            MenuViewController menuController = loader.getController();
            if (menuController != null) {
                // Nếu có MediaPlayer của menu, phát lại nhạc menu
                if (menuMediaPlayer != null) {
                    menuController.setMediaPlayer(menuMediaPlayer);
                    menuController.playMenuMusic(); // Phát nhạc menu
                }
            }

            // Chuyển sang cảnh menu
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            
        }
    }
    @FXML
    private void getRanking(ActionEvent event) {
    	MenuItem mode = (MenuItem) event.getSource();
    	labMode.setText(mode.getText());
    	menuMode.setText(mode.getText());
    	int modeG = 1;
		switch (mode.getText()) {
		case "Cổ Điển":
			modeG = 1;
			break;
		case "Tự Do":
			modeG = 2;
			break;
		case "Chướng ngại":
			modeG = 3;
			break;
		case "Thách thức":
			modeG = 4;
			break;
		default:
			modeG = 5;
			break;
		}
        // Lấy dữ liệu từ cơ sở dữ liệu
        ObservableList<Rank> ranks = FXCollections.observableArrayList(ScoreDAO.getInstance().getTopScores(10,modeG));

        // Gán dữ liệu vào bảng
        tableView.setItems(ranks);
    }
}
