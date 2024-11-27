package controller;


import javafx.scene.image.Image;
import java.net.URL;
import java.time.LocalTime;

public class GameEnvironment {
    private String weatherCondition;
    private int hour;
    private GameViewController gameController;

    public GameEnvironment(String weatherCondition, int hour, GameViewController gameController) {
        this.weatherCondition = weatherCondition;
        this.hour = hour;
        this.gameController = gameController;
        
    }

    public void updateGameBasedOnWeather() {
        if (weatherCondition.contains("rain")) {
            URL resource = getClass().getResource("/view/image_weather/rain.png");
            if (resource != null) {
                Image image = new Image(resource.toExternalForm());
                gameController.updateWeather(image);
                System.out.println("Setting environment: trời mưa");
            } else {
                System.err.println("Hình ảnh không tồn tại: rain.png");
            }
        }
        DarkOrLightMode(); // Gọi ngoài khối if-else
    }


    public void DarkOrLightMode() {
        LocalTime time = LocalTime.now();
        int hour = time.getHour(); // Lấy giờ hiện tại

        if (hour > 6 && hour<18) {
            // Tạo một Image từ đường dẫn file hoặc URL

            Image image = new Image(getClass().getResource("/view/image_weather/sun.png").toExternalForm()); // Đảm bảo sử dụng đúng đường dẫn


            // Gán hình ảnh cho ImageView
            gameController.updateWeather(image);
            System.out.println("Setting environment: trời sáng");
        } else {

            // Tạo một Image từ đường dẫn file hoặc URL
        	
            Image image = new Image(getClass().getResource("/view/image_weather/moon.png").toExternalForm()); // Đảm bảo sử dụng đúng đường dẫn

        	gameController.updateWeather(image);
            // Cập nhật chế độ giao diện tối
            String styleBoard = "-fx-background-color: #2E2E2E;";
            String styleMenu = "-fx-background-color: #444444";
            String styleUser = "-fx-text-fill: #FFFFFF;";
            String styleButton = "-fx-background-color: #666666; -fx-text-fill: white;";
            String styleScore ="-fx-text-fill: white;";
            gameController.updateDarkMode(styleBoard, styleMenu, styleUser, styleButton,styleScore);

            // Gán hình ảnh cho ImageView
            
            System.out.println("Setting environment: trời tối");
        }
    }
}
