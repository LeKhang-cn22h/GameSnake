package controller;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import model.*;

import java.util.Random;

public class GameViewController {
    @FXML
    private GridPane gameGrid;
    private Snake snake;
    private Food food;
    private GameConfig gameConfig;
    private Random random;
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

        gameGrid.setOnKeyPressed(this::handleKeyPress);
        gameGrid.requestFocus();

        // Tạo và bắt đầu thread để tự động di chuyển rắn
        startGameThread();
    }

    private void createGameGrid() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = new Button();
                button.setPrefSize(gameConfig.getButtonWidth(), gameConfig.getButtonHeight());
                button.setStyle("-fx-background-color: white;");
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
                    snake.move();
                    checkCollision();
                    drawSnake();
                });

                try {
                    Thread.sleep(500);  // Rắn di chuyển mỗi 500ms
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
        if (head.getRow() < 0 || head.getRow() >= gameConfig.getMapSize() || head.getCol() < 0 || head.getCol() >= gameConfig.getMapSize()) {
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
            snake.grow();
            score.increaseScore(10);
            spawnFood();
            updateScoreDisplay();
        }

        // Điều kiện thắng: ví dụ đạt 100 điểm
        if (score.getCurrentScore() >= 100) {
            endGame("Congratulations! You win!");
        }
    }

    private void spawnFood() {
        int row = random.nextInt(gameConfig.getMapSize());
        int col = random.nextInt(gameConfig.getMapSize());
        food.setPosition(new Position(row, col));
    }

    private void updateScoreDisplay() {
        scoreLabel.setText("Score: " + score.getCurrentScore());
    }

    // Kết thúc trò chơi
    private void endGame(String message) {
        isGameOver = true;
        System.out.println(message);
        gameThread.interrupt();
    }
}
