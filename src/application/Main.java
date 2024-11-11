//package application;
//
//import javafx.application.Application;
//import javafx.stage.Stage;
//import javafx.scene.Scene;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//
//public class Main extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/view/GameView.fxml"));
//            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//
//            primaryStage.setScene(scene);
//            primaryStage.setTitle("Grid Button Game");
//            primaryStage.show();
//            // Đặt focus vào root để nhận sự kiện bàn phím
//            root.requestFocus();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Tải giao diện đăng nhập (LoginView.fxml) đầu tiên
            Parent root = FXMLLoader.load(getClass().getResource("/view/interface.fxml"));
            Scene scene = new Scene(root);
//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
	    	primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.show();
            // Đặt focus vào root để nhận sự kiện bàn phím
            root.requestFocus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
