package DAO;

import model.Question;
import database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDAO implements DAOInterface<Question> {

    @Override
    public int insert(Question question) {
        String sql = "INSERT INTO questions (question, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, question.getQuestion());
            List<String> options = question.getOptions();
            for (int i = 0; i < options.size(); i++) {
                statement.setString(i + 2, options.get(i));
            }
            statement.setInt(6, question.getCorrectOption());
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

    @Override
    public Question selectById(Question question) {
        // Cần triển khai phương thức này
        return null;
    }

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


    @Override
    public int update(Question question) {
        // Cần triển khai phương thức này
        return 0;
    }

    @Override
    public int delete(Question question) {
        // Cần triển khai phương thức này
        return 0;
    }

    @Override
    public ArrayList<Question> selectByCondition() {
        // Cần triển khai phương thức này
        return null;
    }
}