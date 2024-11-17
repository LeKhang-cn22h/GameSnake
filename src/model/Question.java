package model;

import java.util.List;

public class Question {
    private String question;
    private List<String> options;
    private int correctOption;
    private Integer userAnswer; // Lưu câu trả lời của người chơi (-1 nếu chưa trả lời)

    public Question(String question, List<String> options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
        this.userAnswer = -1; // Đặt giá trị ban đầu là -1 (chưa trả lời)
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOption() {
        return correctOption;
    }

    public Integer getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(Integer userAnswer) {
        this.userAnswer = userAnswer;
    }

    // Kiểm tra người chơi có trả lời đúng không
    public boolean isAnsweredCorrectly() {
        return userAnswer != -1 && userAnswer == correctOption;
    }

    // Kiểm tra người chơi có trả lời câu hỏi chưa
    public boolean isAnswered() {
        return userAnswer != -1;
    }
}
