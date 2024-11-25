package controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class GameEnvironment {
    private String weatherCondition;
    private int hour;
    private GameViewController gameController;
    @FXML
    private AnchorPane BoardMain;
    @FXML
    private ImageView imgWeather = new ImageView();
    public GameEnvironment(String weatherCondition, int hour, GameViewController gameController) {
        this.weatherCondition = weatherCondition;
        this.hour = hour;
        this.gameController = gameController;
    }

    public void updateGameBasedOnWeather() {
        if (weatherCondition.contains("rain")) {
//        	imgWeather = new ImageView();

        	// Tạo một Image từ đường dẫn file hoặc URL
        	Image image = new Image("file:/C:/Users/Thanh Dat/git/GameSnake/src/view/image_codinh/rain.png"); // Đường dẫn cục bộ
        	// Image image = new Image("https://example.com/image.jpg"); // URL từ internet

        	// Gán hình ảnh cho ImageView
        	gameController.updateWeather(image);
            System.out.println("Setting environment: trời mưa");
            DarkOrLightMode();
            // Thay đổi màu sắc, tốc độ hoặc các hiệu ứng trong game
        } else {
        	DarkOrLightMode();
        }
    }
    public void DarkOrLightMode() {
    	if (hour >= 6 && hour < 18) {
        	// Tạo một Image từ đường dẫn file hoặc URL
        	Image image = new Image("file:/C:/Users/Thanh Dat/git/GameSnake/src/view/image_codinh/sun.png"); // Đường dẫn cục bộ
        	// Image image = new Image("https://example.com/image.jpg"); // URL từ internet

        	// Gán hình ảnh cho ImageView
        	gameController.updateWeather(image);
            System.out.println("Setting environment: trời sáng");
        } else {
        	// Tạo một Image từ đường dẫn file hoặc URL
        	Image image = new Image("file:/C:/Users/Thanh Dat/git/GameSnake/src/view/image_codinh/moon.png"); // Đường dẫn cục bộ
        	// Image image = new Image("https://example.com/image.jpg"); // URL từ internet
        	String styleBoard = "-fx-background-color: #2E2E2E;";
        	String styleMenu = "-fx-background-color: #444444";
        	String styleUser = "-fx-text-fill: #FFFFFF;";
        	String styleButton = "-fx-background-color: #666666; -fx-text-fill: white;";
        	gameController.updateDarkMode(styleBoard, styleMenu, styleUser, styleButton);
        	// Gán hình ảnh cho ImageView
        	gameController.updateWeather(image);
            System.out.println("Setting environment: trời tối");
        }
    }
}
