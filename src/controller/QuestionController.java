package controller;

import DAO.QuestionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionController {
	 private GameViewController gameViewController; // Tham chiếu đến GameViewController

	    public void setGameViewController(GameViewController gameViewController) {
	        this.gameViewController = gameViewController;
	    }
    @FXML
    private Label lblQuestion;

    @FXML
    private Button btnOption1;

    @FXML
    private Button btnOption2;

    @FXML
    private Button btnOption3;

    @FXML
    private Button btnOption4;

    @FXML
    private TextField txtScore;

    private List<Question> questions;
    private int currentQuestionIndex = 0; // Chỉ số câu hỏi hiện tại
    private int score = 0; // Điểm số hiện tại

    public void initialize() {
        questions = new ArrayList<>();
        loadQuestionsFromDatabase();
        displayRandomQuestions();
    }

    private void loadQuestionsFromDatabase() {
        QuestionDAO questionDAO = new QuestionDAO();
        questions = questionDAO.selectAll(); // Lấy tất cả câu hỏi từ cơ sở dữ liệu
    }

 
    private void displayRandomQuestions() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex); // Lấy câu hỏi hiện tại

            lblQuestion.setText(question.getQuestion());
            List<String> options = question.getOptions();
            btnOption1.setText(options.get(0));
            btnOption2.setText(options.get(1));
            btnOption3.setText(options.get(2));
            btnOption4.setText(options.get(3));
        } else {
            // Đóng giao diện của QuestionController khi trả lời đủ 3 câu hỏi
            finishQuiz();
            closeQuestionWindow();
        }
    }

    private void closeQuestionWindow() {
        // Lấy Stage hiện tại từ bất kỳ Node nào trong giao diện (ví dụ là lblQuestion)
        Stage stage = (Stage) lblQuestion.getScene().getWindow();
        stage.close(); // Đóng cửa sổ
    }

    public void handleAnswer(int selectedOption) {
        Question question = questions.get(currentQuestionIndex);
        
        // Kiểm tra đáp án và cộng điểm nếu đúng
        if (selectedOption == question.getCorrectOption()) {
            score += 10; // Tăng điểm số nếu đáp án đúng
        }

        txtScore.setText("Score: " + score);
        currentQuestionIndex++; // Chuyển sang câu hỏi tiếp theo

        // Nếu người chơi đã trả lời hết câu hỏi
        if (currentQuestionIndex >= questions.size()) {
            finishQuiz(); // Gọi phương thức finishQuiz để tính tổng số câu trả lời đúng
        } else {
            displayRandomQuestions(); // Hiển thị câu hỏi tiếp theo
        }
    }

    @FXML
    public void handleOption1() {
        handleAnswer(1); // Gọi phương thức xử lý với đáp án 1
    }

    @FXML
    public void handleOption2() {
        handleAnswer(2); // Gọi phương thức xử lý với đáp án 2
    }

    @FXML
    public void handleOption3() {
        handleAnswer(3); // Gọi phương thức xử lý với đáp án 3
    }

    @FXML
    public void handleOption4() {
        handleAnswer(4); // Gọi phương thức xử lý với đáp án 4
    }

    // Phương thức này gọi sau khi quiz hoàn thành
    public void finishQuiz() {
        if (gameViewController != null) {
            gameViewController.updateScoreFromQuiz(score); // Truyền tổng điểm
        }
        closeQuestionWindow(); // Đóng cửa sổ câu hỏi
    }
}

