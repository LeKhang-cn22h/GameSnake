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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingController {
	@FXML
	private GridPane BgrGrid = new GridPane();
	@FXML
	private Button btnColorSnake;
	@FXML
	private Label labColor;
	@FXML
	private Button btnColor;
	@FXML
	private Button btnColorOk;
	@FXML
	private Button btnBgrMap;
	@FXML
	private ImageView imgBgr = new ImageView();
	@FXML
	private ImageView imgSelected;
	@FXML
	private Button btnBgrOk;
	
	@FXML
	private void openColorBoard() {
        try {
            // Tải fxml cho SettingController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ColorBoard.fxml"));
            Parent root = loader.load();
            // Tạo một cửa sổ mới
            Stage newStage = new Stage();
            newStage.setTitle("Đổi màu Rắn");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(btnColorSnake.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	@FXML
	private void openBgrBoard() {
        try {
            // Tải fxml cho SettingController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ImageBoard.fxml"));
            Parent root = loader.load();
            // Tạo một cửa sổ mới
            Stage newStage = new Stage();
            newStage.setTitle("Đổi hình map");
            newStage.initModality(Modality.WINDOW_MODAL);
            newStage.initOwner(btnColorSnake.getScene().getWindow());
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	private void getColor(ActionEvent event) {
		Button btnC = (Button) event.getSource();
		labColor.setText(btnC.getText());
	}
	@FXML
	private void getBgr(MouseEvent event) {
	    // Lấy Node được click từ PickResult
	    Node clickedNode = event.getPickResult().getIntersectedNode();

	    // Kiểm tra nếu Node là ImageView
	    if (clickedNode instanceof ImageView) {
	        ImageView imgB = (ImageView) clickedNode;
	        imgSelected.setImage(imgB.getImage()); // Gán hình ảnh từ ImageView được click
	        System.out.println("Image selected: " + imgB.getImage());
	    } else if (clickedNode != null) {
	        // Nếu Node không phải ImageView nhưng thuộc ô GridPane
	        System.out.println("Clicked on a GridPane cell but no ImageView found.");
	    } else {
	        System.out.println("Nothing clicked.");
	    }
	}
	@FXML
	public String ChangeColor() {
	    if (labColor.getText() != null) {
	        SharedData.setSelectedColor(labColor.getText());
	    }
        Stage stage = (Stage) btnColorOk.getScene().getWindow();
        stage.close(); 
	    return SharedData.getSelectedColor();
	}
	@FXML
	public Image ChangeBgr() {
	    if (imgSelected.getImage() != null) {
	        SharedData.setSelectedBgr(imgSelected.getImage());
	    }
        Stage stage = (Stage) btnBgrOk.getScene().getWindow();
        stage.close(); 
	    return SharedData.getSelectedBgr();
	}
}
