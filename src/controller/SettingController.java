package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SettingController {
	private String color;
	@FXML
	private Button btnColorSnake;
	@FXML
	private Label labColor;
	@FXML
	private Button btnColor;
	@FXML
	private Button btnColorOk;
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
	private void getColor(ActionEvent event) {
		Button btnC = (Button) event.getSource();
		labColor.setText(btnC.getText());
	}
	@FXML
	public String ChangeColor() {
	    if (labColor.getText() != null) {
	        SharedData.setSelectedColor(labColor.getText());
	    } else {
	        SharedData.setSelectedColor("yellow");
	    }
        Stage stage = (Stage) btnColorOk.getScene().getWindow();
        stage.close(); 
	    return SharedData.getSelectedColor();
	}
}
