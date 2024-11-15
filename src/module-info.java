
module game {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    // Mở package 'application' để JavaFX có thể truy cập vào các class trong đó (như Main.java, các controller và view)
    opens application to javafx.graphics, javafx.fxml;

    // Mở các package của model nếu bạn sử dụng chúng trong controller hoặc view
    opens model to javafx.base, javafx.graphics, javafx.fxml;
    opens view to javafx.graphics, javafx.fxml;
    opens controller to javafx.graphics, javafx.fxml;
    opens database to javafx.graphics, javafx.fxml;
}
