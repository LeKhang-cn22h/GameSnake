
module game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.net.http;
    requires org.json;
	requires javafx.graphics;
    // Mở package 'application' để JavaFX có thể truy cập vào các class trong đó (như Main.java, các controller và view)
    opens application to javafx.graphics, javafx.fxml,org.json;

    // Mở các package của model nếu bạn sử dụng chúng trong controller hoặc view
    opens model to javafx.base, javafx.graphics, javafx.fxml,org.json;
    opens view to javafx.graphics, javafx.fxml,org.json;
    opens controller to javafx.graphics, javafx.fxml,org.json;
    opens database to javafx.graphics, javafx.fxml,org.json;
}
