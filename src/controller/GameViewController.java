package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import model.*;

import java.util.Optional;
import java.util.Random;

public class GameViewController {
    @FXML
    private GridPane gameGrid;
    private Snake snake;
    private Food food;
    private GameConfig gameConfig;
    private Random random;
    @FXML
    private Label scoreLabel; 
    private Score score;
    
    private boolean isGameOver = false;  // Biến theo dõi trạng thái game
    private Thread gameThread;  // Thread để tự động di chuyển rắn

    @FXML
    public void initialize() {
        gameConfig = new GameConfig(20, 30, 30);
        food = new Food(5, 5);
        snake = new Snake(10, 10, food, gameConfig);
        random = new Random();
        score = new Score(0);
        
        createGameGrid();
        drawSnake();
        drawFood();

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
            button.setStyle("-fx-background-color: yellow;");
        }

        drawFood();
    }

    private void drawFood() {
        Button foodButton = (Button) gameGrid.getChildren().get(food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());
        foodButton.setStyle("-fx-background-color: red;");
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
                Platform.runLater(() -> {
                	checkCollision();
                	snake.move();
                    drawSnake();
                });

                try {
                    Thread.sleep(200);  // Rắn di chuyển mỗi 500ms
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        gameThread.setDaemon(true);
        gameThread.start();
    }

    // Kiểm tra va chạm
    private void checkCollision() {
        Position head = snake.getHead();

        // Kiểm tra va chạm với tường
        if (head.getRow() < 0 || 
        		head.getRow() >= gameConfig.getMapSize() || 
        		head.getCol() < 0 || 
        		head.getCol() >= gameConfig.getMapSize()) {
            endGame("Game Over: You hit the wall!");
        }

        // Kiểm tra va chạm với chính rắn
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                endGame("Game Over: You hit yourself!");
            }
        }

        // Kiểm tra rắn ăn mồi
        if (head.equals(food.getPosition())) {
            score.increaseScore(10);
            System.out.println("Score increased, current score: " + score.getCurrentScore());  // Kiểm tra ở đây
            spawnFood();
            updateScoreDisplay();
            
        }

        // Điều kiện thắng: ví dụ đạt 100 điểm
        if (score.getCurrentScore() > 100000) {
            endGame("Congratulations! You win!");
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


    // Kết thúc trò chơi
    private void endGame(String message) {
        isGameOver = true;

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
//            restartGame();  // Hàm để khởi động lại trò chơi
        } else {
            Platform.exit();  // Thoát ứng dụng
            System.exit(0);
        }

    }
}
