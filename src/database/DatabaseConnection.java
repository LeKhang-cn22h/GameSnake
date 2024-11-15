package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;

    private static final String URL = "jdbc:mysql://localhost:3306/snake";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    // Sử dụng Singleton Pattern
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (DatabaseConnection.class) { // Đảm bảo thread-safe
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    System.out.println("Kết nối được tạo!");
                }
            }
        }
        return connection;
    }

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
