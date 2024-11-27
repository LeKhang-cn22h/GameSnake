package application;

import java.io.File;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String filePath = "user.txt";
        File file = new File(filePath);
        String title;
        String resource;

        // Kiểm tra file để quyết định giao diện hiển thị
        if (!file.exists() || file.length() == 0) {
            title = "Login";
            resource = "/view/interface.fxml";  // Giao diện đăng nhập
        } else {
            title = "Game Snake";
            resource = "/view/menuView.fxml";  // Giao diện game
        }

        try {
            // Tải giao diện từ file FXML
            Parent root = FXMLLoader.load(getClass().getResource(resource));
            Scene scene = new Scene(root);

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
            primaryStage.getIcons().add(new Image(getClass().getResource("/view/image_signLogin/SNAKE.png").toExternalForm()));

            // Đặt focus vào root để nhận sự kiện bàn phím
            root.requestFocus();
        } catch (Exception e) {
            // Thêm thông báo lỗi rõ ràng hơn
            System.err.println("Error loading FXML resource: " + resource);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
