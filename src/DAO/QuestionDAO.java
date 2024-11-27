package DAO;

import model.Question;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO implements DAOInterface<Question> {

    /**
     * Thêm một câu hỏi mới vào cơ sở dữ liệu
     * @param question Câu hỏi cần thêm
     * @return ID của câu hỏi vừa chèn vào cơ sở dữ liệu, hoặc -1 nếu không thành công
     */
    @Override
    public int insert(Question question) {
        String sql = "INSERT INTO questions (question, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Gán giá trị cho câu hỏi và các lựa chọn
            statement.setString(1, question.getQuestion());
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                statement.setString(i + 2, options.get(i));
            }
            statement.setInt(6, question.getCorrectOption());
            
            // Thực thi truy vấn và lấy ID của câu hỏi mới
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Trả về ID của câu hỏi vừa chèn
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Trả về -1 nếu không thành công
    }

    /**
     * Lấy câu hỏi theo ID (phương thức chưa được triển khai)
     * @param question Câu hỏi cần tìm
     * @return Câu hỏi tìm thấy, hoặc null nếu không tìm thấy
     */
    @Override
    public Question selectById(Question question) {
        // Cần triển khai phương thức này
        return null;
    }

    /**
     * Lấy tất cả câu hỏi từ cơ sở dữ liệu, trả về một danh sách câu hỏi ngẫu nhiên
     * @return Danh sách câu hỏi ngẫu nhiên
     */
    @Override
    public ArrayList<Question> selectAll() {
        ArrayList<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM questions ORDER BY RAND() LIMIT 3"; // Truy vấn lấy ngẫu nhiên 3 câu hỏi
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String questionText = resultSet.getString("question");
                List<String> options = new ArrayList<>();
                options.add(resultSet.getString("option1"));
                options.add(resultSet.getString("option2"));
                options.add(resultSet.getString("option3"));
                options.add(resultSet.getString("option4"));
                int correctOption = resultSet.getInt("correct_option");
                questions.add(new Question(questionText, options, correctOption));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    /**
     * Cập nhật thông tin câu hỏi (phương thức chưa được triển khai)
     * @param question Câu hỏi cần cập nhật
     * @return Số lượng bản ghi bị ảnh hưởng, hoặc 0 nếu không thành công
     */
    @Override
    public int update(Question question) {
        // Cần triển khai phương thức này
        return 0;
    }

    /**
     * Xóa câu hỏi khỏi cơ sở dữ liệu (phương thức chưa được triển khai)
     * @param question Câu hỏi cần xóa
     * @return Số lượng bản ghi bị ảnh hưởng, hoặc 0 nếu không thành công
     */
    @Override
    public int delete(Question question) {
        // Cần triển khai phương thức này
        return 0;
    }

    /**
     * Lấy câu hỏi theo điều kiện nhất định (phương thức chưa được triển khai)
     * @return Danh sách câu hỏi thỏa mãn điều kiện
     */
    @Override
    public ArrayList<Question> selectByCondition() {
        // Cần triển khai phương thức này
        return null;
    }
}
