package controller;

import javafx.scene.image.Image;
import java.net.URL;
import java.time.LocalTime;

public class GameEnvironment {

    private String weatherCondition;  // Điều kiện thời tiết (rain, sunny, etc.)
    private int hour;  // Giờ hiện tại (dùng để xác định ngày hay đêm)
    private GameViewController gameController;  // Controller của game để cập nhật giao diện

    // Constructor: Khởi tạo đối tượng GameEnvironment với điều kiện thời tiết, giờ và controller game
    public GameEnvironment(String weatherCondition, int hour, GameViewController gameController) {
        this.weatherCondition = weatherCondition;
        this.hour = hour;
        this.gameController = gameController;
    }

    // Phương thức cập nhật giao diện dựa trên thời tiết
    public void updateGameBasedOnWeather() {
        // Kiểm tra điều kiện thời tiết và cập nhật giao diện tương ứng
        if (weatherCondition.contains("rain")) {
            // Cập nhật hình ảnh và hiển thị thông báo cho thời tiết mưa
            URL resource = getClass().getResource("/view/image_weather/rain.png");
            if (resource != null) {
                Image image = new Image(resource.toExternalForm());
                gameController.updateWeather(image);
                System.out.println("Setting environment: trời mưa");
            } else {
                System.err.println("Hình ảnh không tồn tại: rain.png");
            }
        }
        
        // Cập nhật chế độ sáng/tối dựa trên giờ hiện tại
        DarkOrLightMode(); // Gọi ngoài khối if-else để tiết kiệm code
    }

    // Phương thức kiểm tra và áp dụng chế độ sáng/tối
    public void DarkOrLightMode() {
        LocalTime time = LocalTime.now();  // Lấy giờ hiện tại
        int hour = time.getHour();  // Lấy giờ từ thời gian hiện tại

        if (hour > 6 && hour < 18) {  // Kiểm tra nếu là ban ngày
            // Cập nhật hình ảnh mặt trời cho ban ngày
            Image image = new Image(getClass().getResource("/view/image_weather/sun.png").toExternalForm());
            gameController.updateWeather(image);
            System.out.println("Setting environment: trời sáng");
        } else {  // Nếu là ban đêm
            // Cập nhật chế độ tối cho giao diện
            Image image = new Image(getClass().getResource("/view/image_weather/moon.png").toExternalForm());
            gameController.updateWeather(image);
            
            // Cập nhật giao diện tối
            String styleBoard = "-fx-background-color: #2E2E2E;";
            String styleMenu = "-fx-background-color: #444444";
            String styleUser = "-fx-text-fill: #FFFFFF;";
            String styleButton = "-fx-background-color: #666666; -fx-text-fill: white;";
            String styleScore = "-fx-text-fill: white;";
            gameController.updateDarkMode(styleBoard, styleMenu, styleUser, styleButton, styleScore);
            
            System.out.println("Setting environment: trời tối");
        }
    }
}
