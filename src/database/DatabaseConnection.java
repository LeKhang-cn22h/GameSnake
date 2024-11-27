package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    // Thông tin kết nối đến cơ sở dữ liệu
    private static final String URL = "jdbc:mysql://localhost:3306/snake";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    /**
     * Lấy kết nối đến cơ sở dữ liệu sử dụng Singleton Pattern.
     * @return Kết nối đến cơ sở dữ liệu.
     * @throws SQLException Nếu không thể tạo kết nối.
     */
    public static Connection getConnection() throws SQLException {
        // Kiểm tra xem kết nối đã tồn tại và còn mở không
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) { // Đảm bảo thread-safe
                if (connection == null || connection.isClosed()) {
                    // Tạo kết nối mới nếu chưa có hoặc kết nối đã đóng
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Kết nối được tạo!");
                }
            }
        }
        return connection;
    }

    /**
     * Đóng kết nối đến cơ sở dữ liệu.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Kết nối đã đóng!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
