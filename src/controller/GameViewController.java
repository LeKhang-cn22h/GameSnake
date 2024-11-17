package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Random;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import DAO.ScoreDAO;

public class GameViewController {
	
	private String colorSnake;
    @FXML
    private GridPane gameGrid;
    private Snake snake;
    private Food food;
    private GameConfig gameConfig;
    private Random random;
    @FXML
    private Label scoreLabel; 
    private Score score;
    @FXML
    private Label userTxt;
    private boolean isGameOver = false;  // Biến theo dõi trạng thái game
    private Thread gameThread;  // Thread để tự động di chuyển rắn
    @FXML
    private boolean isPaused = false;
    @FXML
    private MenuButton menuButton;

    @FXML
    public void initialize() {
    	colorSnake = SharedData.getSelectedColor();
    	isGameOver = false;
    	gameGrid.getChildren().clear();
        gameConfig = new GameConfig(20, 30, 30);
        food = new Food(5, 5);
        snake = new Snake(10, 10, food, gameConfig);
        random = new Random();
        score = new Score(0);
        updateScoreDisplay();
        
        createGameGrid();
        drawSnake();
        String username = readUsernameFromFile();
        userTxt.setText( username); 
        menuButton.setFocusTraversable(false);

        menuButton.setOnShowing(event -> pauseGame());
        // Đặt focus vào gameGrid và đăng ký sự kiện bàn phím sau khi Scene đã được khởi tạo
        Platform.runLater(() -> {
            gameGrid.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case UP: snake.changeDirection("UP"); break;
                    case DOWN: snake.changeDirection("DOWN"); break;
                    case LEFT: snake.changeDirection("LEFT"); break;
                    case RIGHT: snake.changeDirection("RIGHT"); break;
                    default: break;
                }
            });
            gameGrid.requestFocus();  // Đảm bảo nhận sự kiện bàn phím
        });
        // Tạo và bắt đầu thread để tự động di chuyển rắn
        startGameThread();
    }

    private void createGameGrid() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = new Button();
                button.setPrefSize(gameConfig.getButtonWidth(), gameConfig.getButtonHeight());
                button.setStyle("-fx-background-color: white;");
                button.setFocusTraversable(false);

                gameGrid.add(button, col, row);
            }
        }
        
        System.out.println("Tạo bàn cờ");
    }

    private void drawSnake() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = (Button) gameGrid.getChildren().get(row * gameConfig.getMapSize() + col);
                button.setStyle("-fx-background-color: white;");
            }

        }

        for (Position position : snake.getBody()) {
            Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
            button.setStyle("-fx-background-color: "+ colorSnake +";");
        }
        drawFood();
    }

    private void drawFood() {
        // Sử dụng getResource() để lấy đường dẫn ảnh chính xác
        String imageUrl = getClass().getResource("/view/image_codinh/dua_hau.png").toExternalForm();

        // Đặt style cho Button để hiển thị ảnh
        Button foodButton = (Button) gameGrid.getChildren().get(
            food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());
        
        foodButton.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                            "-fx-background-size: 33px 33px; " +
                            "-fx-background-repeat: no-repeat; " +
                            "-fx-background-color: transparent;"+
                            "-fx-background-position: center;");
    }


    @FXML
    private String readUsernameFromFile() {
        File file = new File("user.txt");
        String username = "Player"; // Mặc định nếu không tìm thấy file
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                username = reader.readLine(); // Đọc tên người dùng từ file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return username;
    }
    @FXML
    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case UP: snake.changeDirection("UP"); break;
            case DOWN: snake.changeDirection("DOWN"); break;
            case LEFT: snake.changeDirection("LEFT"); break;
            case RIGHT: snake.changeDirection("RIGHT"); break;
            default: break;
        }
    }

    // Thread để tự động di chuyển rắn
    private void startGameThread() {
        gameThread = new Thread(() -> {
            while (!isGameOver) {
                if (!isPaused) {
                    Platform.runLater(() -> {
                        snake.move();
                        checkCollision();  // Check collisions only if the game isn't over
                        drawSnake();
                    });
                }
                try {
                    Thread.sleep(200);  // Delay for snake movement
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Handle thread interruption
                    System.out.println("Game thread interrupted!");
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }
    @FXML
    private void pauseGame() {
        isPaused = true;
    }

    @FXML
    private void resumeGame() {
        isPaused = false;
        gameGrid.requestFocus();
    }
    
    // Kiểm tra va chạm
    private void checkCollision() {
        if (isGameOver) return;  // Skip collision checks if the game is already over
        
        Position head = snake.getHead();

        // Check collision with walls
        if (head.getRow() < 0 || head.getRow() >= gameConfig.getMapSize() || head.getCol() < 0 || head.getCol() >= gameConfig.getMapSize()) {
            playGameOverSound();  // Play game over sound
            endGame("Game Over: You hit the wall!");  // End the game
        }

        // Check collision with itself (the body)
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                playGameOverSound();  // Play game over sound
                endGame("Game Over: You hit yourself!");  // End the game
            }
        }

        // Kiểm tra rắn ăn mồi
        if (head.equals(food.getPosition())) {
            score.increaseScore(10);
            System.out.println("Score increased, current score: " + score.getCurrentScore());  // Kiểm tra ở đây
            spawnFood();
            updateScoreDisplay();
            playEatSound();  // Phát âm thanh khi ăn mồi
        }

        // Điều kiện thắng: ví dụ đạt 100 điểm
        if (score.getCurrentScore() == 10) {
            pauseGame();
            startQuiz();
            resumeGame();
        }}

    
    private void startQuiz() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizView.fxml"));
        try {
            Parent quizRoot = loader.load();
            QuestionController questionController = loader.getController();
            questionController.setGameViewController(this); // Truyền GameViewController vào QuestionController

            // Thiết lập giao diện dưới dạng modal
            Stage quizStage = new Stage();
            quizStage.setTitle("Câu hỏi");
            quizStage.initOwner(gameGrid.getScene().getWindow());
            quizStage.initModality(Modality.APPLICATION_MODAL);
            quizStage.setScene(new Scene(quizRoot));
            quizStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void playGameOverSound() {
        // Sử dụng getClass().getResource() để lấy đường dẫn hợp lệ
        URL resource = getClass().getResource("/view/image_codinh/Chet.ogg");
        
        // Kiểm tra nếu resource là null
        if (resource != null) {
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } else {
            System.out.println("File âm thanh không tìm thấy!");
        }
    }
    private void playEatSound() {
        // Lấy URL của file âm thanh
        URL resource = getClass().getResource("/view/image_codinh/AnMoi.ogg");

        // Kiểm tra nếu tài nguyên âm thanh tồn tại
        if (resource != null) {
            Media media = new Media(resource.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play(); // Phát âm thanh
        } else {
            System.out.println("Không tìm thấy file âm thanh!");
        }
    }


	private void spawnFood() {
    	int row;
    	int col;
    	do {
         row = random.nextInt(gameConfig.getMapSize());
        col = random.nextInt(gameConfig.getMapSize());
        
    }while (isFoodOnSnake(new Position(row, col)));
    	food.setPosition(new Position(row, col));
    	}
    private boolean isFoodOnSnake(Position foodPosition) {
      for (Position position : snake.getBody()) {
          if (position.equals(foodPosition)) {
              return true;
          }
      }
      return false;
  }
    private void updateScoreDisplay() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score.getCurrentScore());
        });
    }


    @FXML
//kết thúc trò chơi bàn phím tắt
    private void end() {
    	Alert alertend = new Alert(AlertType.CONFIRMATION);
    	alertend.setTitle("Thông báo");
        alertend.setContentText("Bạn có chắc muốn chơi lại ván mới?");
        ButtonType buttonPlayAgain = new ButtonType("Chơi lại");
        ButtonType buttonExit = new ButtonType("Đóng",ButtonType.CLOSE.getButtonData());

        alertend.getButtonTypes().setAll(buttonPlayAgain, buttonExit);
        Optional<ButtonType> result = alertend.showAndWait();

        if (result.isPresent() && result.get() == buttonPlayAgain) {
        	initialize();
        }
    }
    
    // Kết thúc trò chơi
    private void endGame(String message) {
        isGameOver = true;
        String username = readUsernameFromFile();
        int diem=score.getCurrentScore();
        new ScoreDAO().saveScore(diem, username);
        // Hiển thị thông báo kết thúc game
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Kết thúc trò chơi");
        alert.setHeaderText(message);
        alert.setContentText("Bạn có muốn chơi lại hay thoát?");

        ButtonType buttonPlayAgain = new ButtonType("Chơi lại");
        ButtonType buttonExit = new ButtonType("Thoát");

        alert.getButtonTypes().setAll(buttonPlayAgain, buttonExit);

        // Hiển thị hộp thoại và chờ phản hồi của người dùng
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonPlayAgain) {
        	initialize();
        } else {
            Platform.exit();  // Thoát ứng dụng
            System.exit(0);
        }

    }
    @FXML
    private void switchMenuMain() {
    	Alert alertMain = new Alert(AlertType.CONFIRMATION);
    	alertMain.setTitle("Thông báo");
    	alertMain.setHeaderText("");
        alertMain.setContentText("Bạn có chắc chắn muốn trở về trang chủ ?");
        ButtonType buttonMain = new ButtonType("Có");
        ButtonType buttonExit = new ButtonType("Thoát",ButtonType.CLOSE.getButtonData());

        alertMain.getButtonTypes().setAll(buttonMain, buttonExit);
        Optional<ButtonType> result = alertMain.showAndWait();
        if (result.isPresent() && result.get() == buttonMain) {
        	try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) scoreLabel.getScene().getWindow();
                stage.close(); // Đóng cửa sổ đăng nhập
                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.setTitle("Game Snake");
                newStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void updateScoreFromQuiz(int points) {
        // Cộng dồn điểm từ quiz vào điểm hiện tại
        int currentScore = score.getCurrentScore();
        score.setCurrentScore(currentScore + points);
        
        // Cập nhật hiển thị điểm số
        updateScoreDisplay();
    }

}
