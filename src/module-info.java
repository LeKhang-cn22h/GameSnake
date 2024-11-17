module game {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires java.base;
<<<<<<< HEAD
    requires java.net.http;
    requires org.json;
	requires javafx.graphics;
=======
	requires javafx.graphics;

    requires java.desktop;

>>>>>>> 088041c04172db935a172c256360037b986f0a5a
    // Mở package 'application' để JavaFX có thể truy cập vào các class trong đó (như Main.java, các controller và view)
    opens application to javafx.graphics, javafx.fxml,org.json;

    // Mở các package của model nếu bạn sử dụng chúng trong controller hoặc view
<<<<<<< HEAD
    opens model to javafx.base, javafx.graphics, javafx.fxml,org.json;
    opens view to javafx.graphics, javafx.fxml,org.json;
    opens controller to javafx.graphics, javafx.fxml,org.json;
    opens database to javafx.graphics, javafx.fxml,org.json;
}
=======
    opens model to javafx.base, javafx.graphics, javafx.fxml;
    opens view to javafx.graphics, javafx.fxml;
    opens controller to javafx.graphics, javafx.fxml;
    opens database to javafx.graphics, javafx.fxml;
}
>>>>>>> 088041c04172db935a172c256360037b986f0a5a
