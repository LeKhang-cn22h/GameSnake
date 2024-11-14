package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Rank;
import DAO.ScoreDAO;

public class RankingController {
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

    @FXML
    private void initialize() {
        // Set up the columns
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Fetch data from the database
        ObservableList<Rank> ranks = FXCollections.observableArrayList(ScoreDAO.getInstance().getTopScores(10));

        // Populate the TableView
        tableView.setItems(ranks);
    }
    @FXML
    public void proceedToGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.close(); // Đóng cửa sổ đăng nhập
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Game Snake");
            newStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}