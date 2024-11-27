package controller;

import DAO.QuestionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import model.Question;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuestionController {
    private GameViewController gameViewController; // Tham chiếu đến GameViewController để cập nhật điểm số

    // Thiết lập GameViewController từ bên ngoài
    public void setGameViewController(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    // Các thành phần giao diện người dùng
    @FXML
    private Label lblQuestion; // Hiển thị câu hỏi
    @FXML
    private Button btnOption1; // Nút đáp án 1
    @FXML
    private Button btnOption2; // Nút đáp án 2
    @FXML
    private Button btnOption3; // Nút đáp án 3
    @FXML
    private Button btnOption4; // Nút đáp án 4
    @FXML
    private TextField txtScore; // Hiển thị điểm số

    private AudioClip soundQuestion; // Âm thanh câu hỏi
    private MediaPlayer mediaPlayer; // MediaPlayer dùng để phát âm thanh
    private List<Question> questions; // Danh sách câu hỏi
    private int currentQuestionIndex = 0; // Chỉ số câu hỏi hiện tại
    private int score = 0; // Điểm số hiện tại

    // Constructor khởi tạo
    public QuestionController() {
        initializeSounds(); // Khởi tạo âm thanh
    }

    // Phương thức khởi tạo
    public void initialize() {
        questions = new ArrayList<>();
        loadQuestionsFromDatabase(); // Tải câu hỏi từ cơ sở dữ liệu
        displayRandomQuestions(); // Hiển thị câu hỏi ngẫu nhiên
    }

    // Phương thức tải câu hỏi từ cơ sở dữ liệu
    private void loadQuestionsFromDatabase() {
        QuestionDAO questionDAO = new QuestionDAO();
        questions = questionDAO.selectAll(); // Lấy tất cả câu hỏi từ cơ sở dữ liệu
    }

    // Phương thức hiển thị câu hỏi hiện tại
    private void displayRandomQuestions() {
        if (currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex); // Lấy câu hỏi hiện tại

            // Cập nhật câu hỏi và các lựa chọn
            lblQuestion.setText(question.getQuestion());
            List<String> options = question.getOptions();
            btnOption1.setText(options.get(0));
            btnOption2.setText(options.get(1));
            btnOption3.setText(options.get(2));
            btnOption4.setText(options.get(3));
        } else {
            // Khi đã trả lời đủ câu hỏi, kết thúc quiz
            finishQuiz();
            closeQuestionWindow(); // Đóng cửa sổ câu hỏi
        }
    }

    // Phương thức đóng cửa sổ câu hỏi
    private void closeQuestionWindow() {
        // Lấy Stage hiện tại từ giao diện và đóng cửa sổ
        Stage stage = (Stage) lblQuestion.getScene().getWindow();
        stage.close();
    }

    // Phương thức xử lý khi người chơi chọn đáp án
    public void handleAnswer(int selectedOption) {
        Question question = questions.get(currentQuestionIndex); // Lấy câu hỏi hiện tại

        // Kiểm tra đáp án và cộng điểm nếu đúng
        if (selectedOption == question.getCorrectOption()) {
            score += 10; // Cộng thêm điểm nếu đáp án đúng
        }

        txtScore.setText("Score: " + score); // Cập nhật điểm số
        currentQuestionIndex++; // Chuyển sang câu hỏi tiếp theo

        // Nếu người chơi đã trả lời hết câu hỏi
        if (currentQuestionIndex >= questions.size()) {
            finishQuiz(); // Kết thúc quiz và tính tổng điểm
        } else {
            displayRandomQuestions(); // Hiển thị câu hỏi tiếp theo
        }
    }

    // Phương thức khởi tạo âm thanh
    public void initializeSounds() {
        try {
            // Lấy URL của file âm thanh câu hỏi
            URL soundQuestionURI = getClass().getResource("/view/music/tlCauHoi.ogg");

            if (soundQuestionURI != null) {
                // Tạo Media từ URL âm thanh
                Media soundQuestion = new Media(soundQuestionURI.toString());

                // Tạo MediaPlayer từ Media
                mediaPlayer = new MediaPlayer(soundQuestion);

                // Đăng ký sự kiện khi âm thanh kết thúc
                mediaPlayer.setOnEndOfMedia(() -> {
                    mediaPlayer.seek(javafx.util.Duration.ZERO); // Đặt lại vị trí phát âm thanh về đầu
                    mediaPlayer.play(); // Phát lại âm thanh
                });

                // Phát âm thanh khi âm thanh đã sẵn sàng
                mediaPlayer.play();
            } else {
                System.out.println("Không tìm thấy file âm thanh. Kiểm tra lại đường dẫn.");
            }
        } catch (Exception e) {
            System.out.println("Đã xảy ra lỗi khi tạo MediaPlayer: " + e.getMessage());
        }
    }

    // Phương thức phát âm thanh
    public void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.play(); // Phát âm thanh nếu có
        }
    }

    // Các phương thức xử lý khi người chơi chọn đáp án
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

    // Phương thức kết thúc quiz và cập nhật điểm số vào GameViewController
    public void finishQuiz() {
        if (gameViewController != null) {
            gameViewController.updateScoreFromQuiz(score); // Truyền điểm số tổng
        }
        closeQuestionWindow(); // Đóng cửa sổ câu hỏi
    }
}
