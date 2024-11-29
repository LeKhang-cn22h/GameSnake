package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.Rank;
import model.Score;

public class ScoreDAO implements DAOInterface<Score> {

    public static ScoreDAO getInstance() {
        return new ScoreDAO();
    }

    /**
     * Lưu điểm của người chơi vào cơ sở dữ liệu. Nếu số điểm vượt quá 10 điểm thấp nhất, điểm thấp nhất sẽ bị xóa.
     * @param score Số điểm cần lưu
     * @param username Tên người chơi
     * @param mode Chế độ chơi
     */
    public void saveScore(int score, String username, int mode) {
        int idUser = new UserDAO().getIdByUserName(username);
        if (idUser == -1) {
            System.out.println("Không tìm thấy người dùng");
            return;
        }

        String countScoresSql = "SELECT COUNT(*) AS total FROM score WHERE user_id = ? AND idGameMode = ?";
        String getMinScoreSql = "SELECT id, score FROM score WHERE user_id = ? AND idGameMode = ? ORDER BY score ASC, date_achieved DESC LIMIT 1";
        String deleteMinScoreSql = "DELETE FROM score WHERE id = ?";
        String insertScoreSql = "INSERT INTO score (score, date_achieved, user_id, idGameMode) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu giao tác để đảm bảo tính toàn vẹn dữ liệu

            try {
                // Kiểm tra tổng số điểm trong bảng
                int totalScores = 0;
                try (PreparedStatement ps = conn.prepareStatement(countScoresSql)) {
                    ps.setInt(1, idUser);
                    ps.setInt(2, mode);
                    var rs = ps.executeQuery();
                    if (rs.next()) {
                        totalScores = rs.getInt("total");
                    }
                }

                if (totalScores < 10) {
                    // Nếu chưa đủ 10 hàng, thêm điểm mới
                    try (PreparedStatement ps = conn.prepareStatement(insertScoreSql)) {
                        ps.setInt(1, score);
                        ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
                        ps.setInt(3, idUser);
                        ps.setInt(4, mode);
                        int rowsInserted = ps.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Điểm mới đã được thêm");
                        }
                    }
                } else {
                    // Nếu đã đủ 10 hàng, lấy điểm thấp nhất
                    int minScoreId = -1;
                    int minScore = Integer.MAX_VALUE;
                    try (PreparedStatement ps = conn.prepareStatement(getMinScoreSql)) {
                        ps.setInt(1, idUser);
                        ps.setInt(2, mode);
                        var rs = ps.executeQuery();
                        if (rs.next()) {
                            minScoreId = rs.getInt("id");
                            minScore = rs.getInt("score");
                        }
                    }

                    // So sánh điểm mới với điểm thấp nhất
                    if (score > minScore) {
                        // Xóa điểm thấp nhất
                        if (minScoreId != -1) {
                            try (PreparedStatement ps = conn.prepareStatement(deleteMinScoreSql)) {
                                ps.setInt(1, minScoreId);
                                ps.executeUpdate();
                            }
                        }

                        // Thêm điểm mới
                        try (PreparedStatement ps = conn.prepareStatement(insertScoreSql)) {
                            ps.setInt(1, score);
                            ps.setDate(2, java.sql.Date.valueOf(java.time.LocalDate.now()));
                            ps.setInt(3, idUser);
                            ps.setInt(4, mode);
                            int rowsInserted = ps.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Điểm mới đã thay thế điểm thấp nhất");
                            }
                        }
                    } else {
                        System.out.println("Điểm mới không đủ cao để thay thế");
                    }
                }
                conn.commit(); // Xác nhận giao tác nếu mọi thứ thành công
            } catch (SQLException e) {
                conn.rollback(); // Hủy giao tác nếu có lỗi
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true); // Đặt lại chế độ auto-commit
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách các xếp hạng (điểm cao) của người chơi
     * @param limit Giới hạn số lượng kết quả
     * @param mode Chế độ chơi
     * @return Danh sách xếp hạng của người chơi
     */
    public List<Rank> getTopScores(int limit, int mode) {
        String sql = "SELECT RANK() OVER (ORDER BY s.score DESC) AS rank, u.username, s.score, s.date_achieved " +
                     "FROM score s JOIN users u ON s.user_id = u.id " +
                     "WHERE s.idGameMode = ? " +
                     "LIMIT ?";
        List<Rank> ranks = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mode); // Thêm điều kiện lọc theo idGameMode
            ps.setInt(2, limit); // Giới hạn số lượng kết quả
            var rs = ps.executeQuery();

            while (rs.next()) {
                int rank = rs.getInt("rank");
                String username = rs.getString("username");
                int score = rs.getInt("score");
                LocalDate date = rs.getDate("date_achieved").toLocalDate();
                ranks.add(new Rank(rank, username, score, date));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ranks;
    }

    /**
     * Lấy điểm cao nhất của một chế độ chơi
     * @param modeGame ID của chế độ chơi
     * @return Điểm cao nhất của chế độ chơi
     */
    public int getHighestScore(int modeGame) {
        String sql = "SELECT MAX(score) AS highest_score FROM score WHERE idGameMode = ?";
        int highestScore = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, modeGame); // Gán giá trị cho điều kiện modeGame
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    highestScore = rs.getInt("highest_score");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return highestScore;
    }

    @Override
    public int insert(Score s) {
        s.getCurrentScore(); // Placeholder, cần triển khai đúng cách
        return 0;
    }

    @Override
    public Score selectById(Score t) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ArrayList<Score> selectAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Score t) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(Score t) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ArrayList<Score> selectByCondition() {
        // TODO Auto-generated method stub
        return null;
    }
}
