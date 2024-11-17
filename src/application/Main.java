
package application;

import java.io.File;

import java.net.http.HttpClient;

import java.net.URI;
import database.DatabaseConnection;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        String filePath = "user.txt";
        File file = new File(filePath);
    	String title;
    	String resource;
        if(!file.exists() || file.length()==0) {
        	title = "Login";
        	resource = "/view/interface.fxml";
        }
        else {
        	title = "Game Snake";
        	resource = "/view/menuView.fxml";
        }

	        try {
		            // Tải giao diện đăng nhập (LoginView.fxml) đầu tiên
		            Parent root = FXMLLoader.load(getClass().getResource(resource));
		            Scene scene = new Scene(root);
		//            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		            
			    	primaryStage.setScene(scene);
		            primaryStage.setTitle(title);
		            primaryStage.show();
		            // Đặt focus vào root để nhận sự kiện bàn phím
		            root.requestFocus();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
    }


    public static void main(String[] args) {
        launch(args);
        HttpClient client = HttpClient.newHttpClient();
        System.out.println("HttpClient is available and ready to use!");
    }
}
