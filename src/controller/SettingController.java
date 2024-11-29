package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingController {

    @FXML
    private GridPane BgrGrid; // Lưới để hiển thị các hình ảnh nền
    @FXML
    private Button btnColorSnake; // Nút để mở cửa sổ chọn màu rắn
    @FXML
    private Label labColor; // Nhãn hiển thị màu rắn đã chọn
    @FXML
    private Button btnColor; // Nút để chọn màu rắn
    @FXML
    private Button btnColorOk; // Nút xác nhận chọn màu
    @FXML
    private Button btnBgrMap; // Nút để chọn hình nền bản đồ
    @FXML
    private ImageView imgBgr; // Hình ảnh nền bản đồ hiện tại
    @FXML
    private ImageView imgSelected; // Hình ảnh nền đã chọn
    @FXML
    private Button btnBgrOk; // Nút xác nhận chọn hình nền
    @FXML 
    private Slider speedSlider = new Slider(); // Slider để điều chỉnh tốc độ game

    /**
     * Mở cửa sổ để chọn màu cho rắn.
     */
    @FXML
    private void openColorBoard() {
        try {
            // Tải fxml cho ColorBoard và mở cửa sổ mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ColorBoard.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Đổi màu Rắn");
            newStage.initModality(Modality.WINDOW_MODAL); // Cửa sổ này sẽ chặn cửa sổ chính
            newStage.initOwner(btnColorSnake.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // In lỗi nếu có
        }
    }

    /**
     * Hàm khởi tạo, thiết lập giá trị slider và lắng nghe thay đổi của slider.
     */
    @FXML
    public void initialize() {
        // Đặt giá trị slider từ SharedData khi mở giao diện
        speedSlider.setValue(SharedData.getSpeed());

        // Lắng nghe sự thay đổi giá trị của slider và cập nhật vào SharedData
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SharedData.setSpeed(newValue.doubleValue()); // Cập nhật tốc độ trong SharedData
        });
    }

    /**
     * Mở cửa sổ để chọn hình nền cho bản đồ.
     */
    @FXML
    private void openBgrBoard() {
        try {
            // Tải fxml cho ImageBoard và mở cửa sổ mới
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImageBoard.fxml"));
            Parent root = loader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Đổi hình map");
            newStage.initModality(Modality.WINDOW_MODAL); // Cửa sổ này sẽ chặn cửa sổ chính
            newStage.initOwner(btnColorSnake.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // In lỗi nếu có
        }
    }

    /**
     * Cập nhật màu rắn đã chọn vào nhãn labColor.
     * @param event Sự kiện ActionEvent khi người dùng chọn màu
     */
    @FXML
    private void getColor(ActionEvent event) {
        Button btnC = (Button) event.getSource();
        labColor.setText(btnC.getText()); // Cập nhật màu rắn trong nhãn
    }

    /**
     * Cập nhật hình nền đã chọn và hiển thị hình ảnh trong imgSelected.
     * @param event Sự kiện MouseEvent khi người dùng chọn hình nền
     */
    @FXML
    private void getBgr(MouseEvent event) {
        Node clickedNode = event.getPickResult().getIntersectedNode();

        // Kiểm tra nếu node được click là ImageView
        if (clickedNode instanceof ImageView) {
            ImageView imgB = (ImageView) clickedNode;
            imgSelected.setImage(imgB.getImage()); // Cập nhật hình ảnh đã chọn
            System.out.println("Image selected: " + imgB.getImage()); // In hình ảnh đã chọn
        } else if (clickedNode != null) {
            System.out.println("Clicked on a GridPane cell but no ImageView found.");
        } else {
            System.out.println("Nothing clicked.");
        }
    }

    /**
     * Thay đổi màu rắn và lưu vào SharedData.
     * Đóng cửa sổ sau khi thay đổi.
     * @return Màu đã chọn
     */
    @FXML
    public String ChangeColor() {
        if (labColor.getText() != null) {
            SharedData.setSelectedColor(labColor.getText()); // Lưu màu rắn vào SharedData
        }
        Stage stage = (Stage) btnColorOk.getScene().getWindow();
        stage.close(); // Đóng cửa sổ
        return SharedData.getSelectedColor(); // Trả về màu đã chọn
    }

    /**
     * Thay đổi hình nền và lưu vào SharedData.
     * Đóng cửa sổ sau khi thay đổi.
     * @return Hình nền đã chọn
     */
    @FXML
    public Image ChangeBgr() {
        if (imgSelected.getImage() != null) {
            SharedData.setSelectedBgr(imgSelected.getImage()); // Lưu hình nền vào SharedData
        }
        Stage stage = (Stage) btnBgrOk.getScene().getWindow();
        stage.close(); // Đóng cửa sổ
        return SharedData.getSelectedBgr(); // Trả về hình nền đã chọn
    }

    /**
     * Cập nhật tốc độ game từ slider vào SharedData.
     */
    @FXML
    private void updateSpeed() {
        double speed = speedSlider.getValue(); // Lấy giá trị từ slider
        SharedData.setSpeed(speed); // Cập nhật tốc độ vào SharedData
    }
}
