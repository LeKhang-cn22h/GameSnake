package controller;


import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModeViewController {
	@FXML
	private Button btnMode1;
	@FXML
	private Button btnMode2;
	@FXML
	private Button btnMode3;
	@FXML
	private Button btnMode4;
	@FXML
	private int getmodeGame(ActionEvent event) {
		Button btnM = (Button) event.getSource();
		switch (btnM.getText()) {
		case "Cổ Điển":
			SharedData.setSelectedMode(1);
			break;
		case "Tự Do":
			SharedData.setSelectedMode(2);
			break;
		case "Chướng ngại":
			SharedData.setSelectedMode(3);
			break;
		case "Thách thức":
			SharedData.setSelectedMode(4);
			break;
		default:
			SharedData.setSelectedMode(1);
			break;
		}
        Stage stage = (Stage) btnMode1.getScene().getWindow();
        stage.close(); 
        try {
            // Tải FXML cho GameView
            Parent gameViewRoot = FXMLLoader.load(getClass().getResource("/view/GameView.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Đặt giao diện mới cho Stage
            stage.setScene(new Scene(gameViewRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return SharedData.getSelectedMode();
	}
}