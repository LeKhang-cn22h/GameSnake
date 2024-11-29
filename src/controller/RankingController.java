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
    private Label labMode; // Nhãn hiển thị chế độ chơi hiện tại
    @FXML
    private MenuButton menuMode; // Menu để chọn chế độ chơi
    @FXML
    private Button btnBack; // Nút quay lại
    @FXML
    private TableView<Rank> tableView; // Bảng xếp hạng

    @FXML
    private TableColumn<Rank, Integer> rankColumn; // Cột thứ hạng
    @FXML
    private TableColumn<Rank, String> usernameColumn; // Cột tên người chơi
    @FXML
    private TableColumn<Rank, Integer> scoreColumn; // Cột điểm số
    @FXML
    private TableColumn<Rank, String> dateColumn; // Cột ngày chơi

    private MediaPlayer rankingMediaPlayer; // MediaPlayer cho nhạc bảng xếp hạng
    private MediaPlayer menuMediaPlayer; // MediaPlayer cho nhạc menu

    /**
     * Hàm khởi tạo bảng xếp hạng, cài đặt các cột và phát nhạc nền.
     */
    @FXML
    private void initialize() {
        // Cài đặt các cột trong bảng
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        // Lấy danh sách xếp hạng từ cơ sở dữ liệu theo chế độ đã chọn
        ObservableList<Rank> ranks = FXCollections.observableArrayList(ScoreDAO.getInstance().getTopScores(10, 1));

        // Cập nhật bảng xếp hạng với dữ liệu lấy được
        tableView.setItems(ranks);
        // Tạo và phát nhạc nền cho bảng xếp hạng
        Media rankingMusic = new Media(getClass().getResource("/view/music/NhacNen.ogg").toString());
        rankingMediaPlayer = new MediaPlayer(rankingMusic);
        rankingMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp lại nhạc nền
        rankingMediaPlayer.play();

        // Dừng nhạc menu nếu có
        if (menuMediaPlayer != null) {
            menuMediaPlayer.stop(); // Dừng nhạc menu
            System.out.println("Menu music stopped!");
        }
    }

    /**
     * Truyền MediaPlayer của nhạc menu khi vào bảng xếp hạng.
     * @param mediaPlayer Đối tượng MediaPlayer của nhạc menu
     */
    public void setMenuMediaPlayer(MediaPlayer mediaPlayer) {
        this.menuMediaPlayer = mediaPlayer;
    }

    /**
     * Xử lý sự kiện khi người dùng nhấn nút "Quay lại".
     * @param event sự kiện ActionEvent
     */
    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            // Dừng nhạc bảng xếp hạng khi quay lại menu
            if (rankingMediaPlayer != null) {
                rankingMediaPlayer.stop(); // Dừng nhạc bảng xếp hạng
                System.out.println("Ranking music stopped!");
            }

            // Chuyển cảnh về menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();

            // Truyền MediaPlayer của nhạc menu vào controller menu
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
            e.printStackTrace(); // In ra lỗi nếu có
        }
    }

    /**
     * Xử lý sự kiện khi người dùng chọn chế độ chơi từ menu.
     * @param event sự kiện ActionEvent khi chọn chế độ
     */
    @FXML
    private void getRanking(ActionEvent event) {
        MenuItem mode = (MenuItem) event.getSource();
        labMode.setText(mode.getText()); // Cập nhật nhãn chế độ
        menuMode.setText(mode.getText()); // Cập nhật menu chế độ

        int modeG = 1; // Mặc định chế độ là 1
        switch (mode.getText()) {
            case "Cổ Điển":
                modeG = 1; // Chế độ Cổ Điển
                break;
            case "Tự Do":
                modeG = 2; // Chế độ Tự Do
                break;
            case "Chướng ngại":
                modeG = 3; // Chế độ Chướng ngại
                break;
            case "Thách thức":
                modeG = 4; // Chế độ Thách thức
                break;
            default:
                modeG = 5; // Mặc định nếu không phải chế độ trên
                break;
        }

        // Lấy danh sách xếp hạng từ cơ sở dữ liệu theo chế độ đã chọn
        ObservableList<Rank> ranks = FXCollections.observableArrayList(ScoreDAO.getInstance().getTopScores(10, modeG));

        // Cập nhật bảng xếp hạng với dữ liệu lấy được
        tableView.setItems(ranks);
    }
}
