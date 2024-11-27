package DAO;

import java.sql.*;
import java.util.ArrayList;

import database.DatabaseConnection;
import model.User;

public class UserDAO implements DAOInterface<User> {

    /**
     * Thêm người dùng mới vào cơ sở dữ liệu.
     * @param user Đối tượng người dùng cần thêm vào cơ sở dữ liệu.
     * @return Số lượng bản ghi đã được thêm vào (thường là 1 nếu thành công).
     */
    @Override
    public int insert(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0; // Nếu có lỗi, trả về 0
        }
    }

    /**
     * Lấy ID người dùng từ tên người dùng.
     * @param username Tên người dùng cần tra cứu ID.
     * @return ID người dùng nếu tìm thấy, -1 nếu không tìm thấy.
     */
    public int getIdByUserName(String username) {
        String sql = "SELECT id FROM users WHERE username=?";
        int idUser = -1;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                idUser = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idUser;
    }

    /**
     * Kiểm tra xem tên người dùng đã tồn tại trong cơ sở dữ liệu hay chưa.
     * @param username Tên người dùng cần kiểm tra.
     * @return true nếu tên người dùng đã tồn tại, false nếu không.
     */
    public boolean checkUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0; // Nếu kết quả lớn hơn 0, nghĩa là tên người dùng đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Lỗi khi kiểm tra
        }
    }

    @Override
    public User selectById(User t) {
        // Phương thức này cần được triển khai nếu có yêu cầu.
        return null;
    }

    @Override
    public ArrayList<User> selectAll() {
        // Phương thức này cần được triển khai nếu có yêu cầu.
        return null;
    }

    @Override
    public int update(User t) {
        // Phương thức này cần được triển khai nếu có yêu cầu.
        return 0;
    }

    @Override
    public int delete(User t) {
        // Phương thức này cần được triển khai nếu có yêu cầu.
        return 0;
    }

    @Override
    public ArrayList<User> selectByCondition() {
        // Phương thức này cần được triển khai nếu có yêu cầu.
        return null;
    }
}
