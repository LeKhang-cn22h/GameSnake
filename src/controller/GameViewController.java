package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import DAO.APIClient;
import DAO.ScoreDAO;
import DAO.WeatherParser;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Food;
import model.FoodFactory;
import model.GameConfig;
import model.GameState;
import model.Obstacle;
import model.Position;
import model.QuizFoodEffect;
import model.Score;
import model.Snake;

public class GameViewController {
    private List<Position> ob = Obstacle.map1;
    private boolean checkMap = false;
    private String colorSnake;
    private Image BgrMap;
    private int modeGame;
    private Position newHead;
    @FXML
    private Label labelRecord;
    @FXML
    private Label directionLabel; // Label hiển thị hướng di chuyển của rắn

    @FXML
    private Label countdownLabel;
    @FXML
    private AnchorPane BoardMain;
    @FXML
    private StackPane stackP;
    @FXML
    private GridPane gameGrid;
    @FXML
    private ImageView imgGame;
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
    private GameState gameState;
    @FXML
    private VBox MenuInfor;
    @FXML
    private AudioClip eatSound;
    @FXML
    private AudioClip gameOverSound;
    @FXML
    private AudioClip moveSound;
    @FXML
    private ImageView imgWeather = new ImageView();
    private String obImage;

    public GameViewController() {
        initializeSounds();  // Gọi phương thức này trong constructor để khởi tạo âm thanh
    }
   
    @FXML
    public void initialize() {
        APIWeatherTime();  // Khởi tạo dữ liệu thời tiết khi bắt đầu game
        this.gameState = new GameState(SharedData.getSpeed()); // Khởi tạo trạng thái game với tốc độ mặc định
        newHead = new Position(10, 10);  // Khởi tạo vị trí của đầu rắn
        
        // Lấy dữ liệu từ SharedData
        modeGame = SharedData.getSelectedMode();
        colorSnake = SharedData.getSelectedColor();
        BgrMap = SharedData.getSelectedBgr();
        
        isGameOver = false;  // Đặt trạng thái game là chưa kết thúc
        gameGrid.getChildren().clear();  // Xóa toàn bộ các nút trong gameGrid
        gameConfig = new GameConfig(20, 30, 30);  // Khởi tạo cấu hình game (kích thước bàn cờ)
        stackP.setPrefWidth(gameConfig.getButtonWidth() * gameConfig.getMapSize());
        stackP.setPrefHeight(gameConfig.getButtonHeight() * gameConfig.getMapSize());
        stackP.setFocusTraversable(false);  // Đặt focus cho StackPane
        labelRecord.setText("Kỷ lục:" + new ScoreDAO().getHighestScore(modeGame));  // Hiển thị kỷ lục

        snake = new Snake(10, 10, gameConfig);  // Khởi tạo rắn
        random = new Random();  // Tạo đối tượng Random để tạo vị trí ngẫu nhiên cho thực phẩm
        spawnFood();  // Gọi hàm spawnFood để xuất hiện thực phẩm mới
        score = new Score(0);  // Khởi tạo điểm số
        updateScoreDisplay();  // Cập nhật hiển thị điểm số
        createGameGrid();  // Tạo bàn cờ
        setImgGame();  // Cài đặt hình ảnh cho game
        String username = readUsernameFromFile();  // Đọc tên người chơi từ file
        userTxt.setText(username);  // Hiển thị tên người chơi
        menuButton.setFocusTraversable(false);  // Đặt focus cho MenuButton

        menuButton.setOnShowing(event -> pauseGame());  // Dừng game khi menu hiện ra
        
        Platform.runLater(() -> {
            gameGrid.getScene().setOnKeyPressed(event -> {
                // Lắng nghe sự kiện bàn phím và thay đổi hướng rắn
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
        
        startGameThread();  // Tạo và bắt đầu thread tự động di chuyển rắn
    }


    // Gọi API lấy dữ liệu thời tiết và cập nhật môi trường game
    private void APIWeatherTime() {
        try {
            String city = "Ho Chi Minh City"; // Thành phố
            String weatherData = APIClient.getWeatherData(city);  // Lấy dữ liệu thời tiết từ API
            String weather = WeatherParser.parseWeather(weatherData);  // Phân tích thời tiết
            int hour = WeatherParser.parseTime(weatherData);  // Phân tích thời gian
            GameEnvironment gameEnvironment = new GameEnvironment(weather, hour, this);  // Tạo đối tượng môi trường game
            gameEnvironment.updateGameBasedOnWeather();  // Cập nhật môi trường game dựa trên thời tiết
            System.out.println("chay xong");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());  // Bắt lỗi nếu có
        }
    }

    public void updateWeather(Image img) {
        imgWeather.setImage(img);  // Cập nhật hình ảnh thời tiết
    }

    public void updateDarkMode(String styleBoard, String styleMenu, String styleUser, String styleButton, String styleScore) {
        BoardMain.setStyle(styleBoard);  // Cập nhật màu nền cho bảng chơi
        MenuInfor.setStyle(styleMenu);  // Cập nhật kiểu dáng menu
        userTxt.setStyle(styleUser);  // Cập nhật kiểu dáng cho tên người chơi
        menuButton.setStyle(styleButton);  // Cập nhật kiểu dáng menu button
        scoreLabel.setStyle(styleScore);  // Cập nhật kiểu dáng điểm số
    }

    // Hàm tạo vị trí ngẫu nhiên cho thực phẩm
    public Position randomPosition() {
        Random random = new Random();
        int x = random.nextInt(gameConfig.getMapSize());  // Tạo vị trí ngẫu nhiên theo chiều ngang
        int y = random.nextInt(gameConfig.getMapSize());  // Tạo vị trí ngẫu nhiên theo chiều dọc
        return new Position(x, y);  // Trả về đối tượng Position
    }

    // Tạo lưới game (game grid)
    private void createGameGrid() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = new Button();
                button.setPrefSize(gameConfig.getButtonWidth(), gameConfig.getButtonHeight());
                button.setStyle("-fx-background-color: transparent;");
                button.setFocusTraversable(false);
                gameGrid.add(button, col, row);  // Thêm button vào lưới game
            }
        }
        System.out.println("Tạo bàn cờ");
    }

    // Thêm chướng ngại vật vào bàn cờ
    private void addOb() {
        obImage = getClass().getResource("/view/image_chuongngaivat/stone.png").toExternalForm();  // Lấy đường dẫn hình ảnh
        for (Position position : ob) {
            Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
            button.setStyle("-fx-background-image: url('" + obImage + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-color: transparent; " +
                    "-fx-background-position: center;");
        }
    }
 // Hàm để thiết lập hình ảnh cho game (background)
    private void setImgGame() {
        // Khởi tạo ImageView nếu chưa có
        if (imgGame == null) {
            imgGame = new ImageView();
        }

        // Đặt hình ảnh cho ImageView và điều chỉnh kích thước
        imgGame.setImage(BgrMap);
        imgGame.setFitWidth(gameConfig.getButtonWidth() * gameConfig.getMapSize() - 16); // Chiều rộng
        imgGame.setFitHeight(gameConfig.getButtonHeight() * gameConfig.getMapSize() - 16); // Chiều cao
        imgGame.setPreserveRatio(false);

        // Căn giữa các thành phần trong StackPane
        StackPane.setAlignment(imgGame, Pos.CENTER); // Hình nền
        StackPane.setAlignment(gameGrid, Pos.CENTER); // Bàn cờ
    }

    // Hàm reset game (khởi tạo lại rắn và các thành phần)
    private void reset() {
        snake = new Snake(10, 10, gameConfig); // Khởi tạo lại rắn
        newHead = new Position(10, 10); // Khởi tạo lại vị trí đầu rắn
        addOb(); // Thêm chướng ngại vật
        checkMap = false; // Reset điều kiện kiểm tra bản đồ
    }

    // Kiểm tra xem có thay đổi bản đồ hay không dựa trên điểm số
    private boolean checkChangeMap() {
        if (score.getCurrentScore() == 100 || score.getCurrentScore() == 200 || score.getCurrentScore() == 300 || score.getCurrentScore() == 400) {
            System.out.println("set snake");
            return true;
        }
        return false;
    }

    // Hàm vẽ rắn lên bàn cờ
    private void drawSnake() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = (Button) gameGrid.getChildren().get(row * gameConfig.getMapSize() + col);
                button.setStyle("-fx-background-color: transparent;");
                button.setFocusTraversable(false);

                // Thêm chướng ngại vật tùy theo chế độ chơi
                if (modeGame == 4) {
                    addOb();
                } else if (modeGame == 3) {
                    setObstacleBasedOnScore();
                }
            }
        }

        // Duyệt qua các vị trí của cơ thể rắn để vẽ
        for (int i = 0; i < snake.getBody().size(); i++) {
            Position position = snake.getBody().get(i);
            Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
            drawHead(); // Vẽ đầu rắn

            if (i == snake.getBody().size() - 1) {
                // Vẽ đuôi rắn
                button.setStyle("-fx-background-color: red; -fx-background-radius: 50%; -fx-border-radius: 50%;");
            } else if (i != 0) {
                // Vẽ thân rắn
                button.setStyle("-fx-background-color: " + colorSnake + ";");
            }
        }
    }
    
    //Điều kiện để đổi map
    private void setObstacleBasedOnScore() {
  	   if(score.getCurrentScore() < 100) {
 		   ob = Obstacle.map1;
 	   }
 	   else if (score.getCurrentScore() < 200) {
 		   ob = Obstacle.map2;
 	   }
 	   else if (score.getCurrentScore() < 300) {
 		   ob = Obstacle.map3;
 	   }
 	   else if (score.getCurrentScore() < 400) {
 		   ob = Obstacle.map4;
 	   }
 	   else {
 		   ob = Obstacle.map5;
 	   }
        if(checkMap == true) {
        	pauseGame();
     	   reset();
     	   resumeGame();
;
        }else	addOb();
		
	}

	// Hàm vẽ đầu rắn
    private void drawHead() {
        Position position = snake.getBody().get(0); // Vị trí đầu rắn
        Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
        String headImage = getHeadImageBasedOnDirection(); // Lấy hình ảnh đầu rắn theo hướng di chuyển

        // Áp dụng hình ảnh cho đầu rắn
        button.setStyle("-fx-background-image:url('" + headImage + "'); -fx-background-size: cover; -fx-background-position: center;");
    }

    // Hàm lấy hình ảnh đầu rắn dựa trên hướng di chuyển
    private String getHeadImageBasedOnDirection() {
        String headImage = getClass().getResource("/view/image_snake/up.png").toExternalForm();
        switch (snake.getCurrentDirection()) {
            case "UP": headImage = getClass().getResource("/view/image_snake/up.png").toExternalForm(); break;
            case "DOWN": headImage = getClass().getResource("/view/image_snake/down.png").toExternalForm(); break;
            case "LEFT": headImage = getClass().getResource("/view/image_snake/left.png").toExternalForm(); break;
            case "RIGHT": headImage = getClass().getResource("/view/image_snake/right.png").toExternalForm(); break;
        }
        return headImage;
    }

    // Hàm vẽ thực phẩm lên bàn cờ
    private void drawFood() {
        String imageUrl = getFoodImageUrl(); // Lấy URL hình ảnh thực phẩm

        Button foodButton = (Button) gameGrid.getChildren().get(food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());
        foodButton.setStyle("-fx-background-image: url('" + imageUrl + "'); -fx-background-size: 33px 33px; -fx-background-repeat: no-repeat; -fx-background-color: transparent; -fx-background-position: center;");
    }

    // Hàm lấy URL của hình ảnh thực phẩm
    private String getFoodImageUrl() {
        String imageUrl;
        switch (food.getType()) {
            case NORMAL: imageUrl = getClass().getResource("/view/image_moi/dua_hau.png").toExternalForm(); break;
            case SLOW: imageUrl = getClass().getResource("/view/image_moi/dautay.png").toExternalForm(); break;
            case SPEED: imageUrl = getClass().getResource("/view/image_moi/traitao.png").toExternalForm(); break;
            case QUIZZ: imageUrl = getClass().getResource("/view/image_moi/traitim.png").toExternalForm(); break;
            default: imageUrl = getClass().getResource("/view/image_moi/default_food.png").toExternalForm();
        }
        return imageUrl;
    }

    // Hàm đọc tên người chơi từ file
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

    // Xử lý sự kiện bàn phím để thay đổi hướng rắn
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
            // Vòng lặp game, chạy cho đến khi game kết thúc
            while (!isGameOver) {
                if (!isPaused) {
                    Platform.runLater(() -> {
                        // Tính toán vị trí mới của đầu rắn dựa trên hướng hiện tại và chế độ chơi
                        newHead = snake.calculateNewHeadPosition(modeGame);
                        
                        // Kiểm tra va chạm, di chuyển rắn và cập nhật giao diện
                        checkCollision();
                        snake.move(newHead);

                        drawSnake();  // Vẽ lại rắn trên grid
                        drawFood();   // Vẽ lại mồi
                        drawHead();   // Vẽ lại đầu rắn
                        snake.updateDirectionAfterMove();  // Cập nhật hướng đi sau mỗi lần di chuyển
                    });
                }
                try {
                    // Ngủ để kiểm soát tốc độ di chuyển của rắn
                    Thread.sleep((long) gameState.getSnakeSpeed());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // Xử lý gián đoạn của thread
                    System.out.println("Game thread interrupted!");
                }
            }
        });
        gameThread.setDaemon(true);  // Thiết lập thread là daemon để tự động kết thúc khi ứng dụng chính đóng
        gameThread.start();  // Khởi động thread game
    }

    // Dừng game
    @FXML
    private void pauseGame() {
        isPaused = true;  // Đặt trạng thái game thành tạm dừng
    }

    // Tiếp tục game sau khi tạm dừng
    @FXML
    private void resumeGame() {
        if (isPaused) {
            // Hiển thị đồng hồ đếm ngược và nhãn hướng di chuyển khi tiếp tục game
            countdownLabel.setVisible(true);
            directionLabel.setVisible(true);

            final int[] countdown = {3};  // Giá trị đếm ngược

            // Hiệu ứng phóng to và mờ cho đồng hồ đếm ngược
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.65), countdownLabel);
            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(2);
            scaleTransition.setToY(2);
            scaleTransition.setCycleCount(2);

            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), countdownLabel);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.setCycleCount(1);

            // Timeline để xử lý đồng hồ đếm ngược và cập nhật nhãn hướng di chuyển
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                countdownLabel.setText(String.valueOf(countdown[0]));
                countdown[0]--;

                // Cập nhật nhãn hướng di chuyển của rắn dựa trên hướng hiện tại
                String currentDirection = snake.getCurrentDirection();
                switch (currentDirection) {
                    case "UP": directionLabel.setText("⬆"); break;
                    case "DOWN": directionLabel.setText("⬇"); break;
                    case "LEFT": directionLabel.setText("⬅"); break;
                    case "RIGHT": directionLabel.setText("➡"); break;
                    default: directionLabel.setText("N/A"); break;
                }

                // Phát hiệu ứng phóng to và mờ
                scaleTransition.play();
                fadeTransition.play();

                // Dừng đồng hồ đếm ngược khi nó đạt đến 0 và tiếp tục game
                if (countdown[0] < 0) {
                    timeline.stop();
                    countdownLabel.setText("Bắt đầu");  // Hiển thị "Bắt đầu" khi đếm ngược kết thúc

                    // Hiệu ứng fade để hiển thị nhãn "Bắt đầu"
                    FadeTransition startFadeTransition = new FadeTransition(Duration.seconds(0.5), countdownLabel);
                    startFadeTransition.setFromValue(0.0);
                    startFadeTransition.setToValue(1.0);
                    startFadeTransition.play();

                    startFadeTransition.setOnFinished(event1 -> {
                        // Ẩn nhãn đếm ngược và nhãn hướng, và tiếp tục game
                        countdownLabel.setVisible(false);
                        directionLabel.setVisible(false);
                        isPaused = false;
                        gameGrid.requestFocus();  // Đặt focus vào grid game để nhận sự kiện từ bàn phím
                        System.out.println("Game Resumed");
                    });
                }
            }));

            timeline.setCycleCount(countdown[0] + 1);  // Đặt số bước đếm ngược
            timeline.play();  // Bắt đầu timeline đồng hồ đếm ngược
        }
    }

    // Kiểm tra va chạm với tường, thân rắn và mồi
    private void checkCollision() {
        if (isGameOver) {
            playGameOverSound();  // Phát âm thanh game over khi game kết thúc
            return;
        }

        Position head = newHead;

        // Kiểm tra va chạm với tường (trừ chế độ 2)
        if (modeGame != 2) {
            if (head.getRow() < 0 || head.getRow() >= gameConfig.getMapSize() || head.getCol() < 0 || head.getCol() >= gameConfig.getMapSize() || ((modeGame == 3 || modeGame == 4) && ob.contains(head))) {
                playGameOverSound();  // Phát âm thanh game over
                endGame("Game Over: You hit the wall!");  // Kết thúc game nếu rắn đâm vào tường
            }
        }

        // Kiểm tra va chạm với chính nó (thân rắn)
        for (int i = 1; i < snake.getBody().size(); i++) {
            if (head.equals(snake.getBody().get(i))) {
                playGameOverSound();  // Phát âm thanh game over
                endGame("Game Over: You hit yourself!");  // Kết thúc game nếu rắn đâm vào chính mình
            }
        }

        // Kiểm tra xem rắn có ăn mồi không
        if (head.equals(food.getPosition())) {
            playEatSound();  // Phát âm thanh khi rắn ăn mồi

            // Tìm nút mồi và áp dụng hiệu ứng fade
            Button foodButton = (Button) gameGrid.getChildren().get(food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());
            onFoodEaten(foodButton);  // Gọi phương thức để áp dụng hiệu ứng fade

            // Tăng trưởng rắn, tăng điểm số và tạo mồi mới
            snake.grow(newHead);
            score.increaseScore();
            checkMap = checkChangeMap();  // Kiểm tra nếu cần thay đổi bản đồ dựa trên điểm số
            System.out.println("Score increased, current score: " + score.getCurrentScore());

            // Xử lý đặc biệt cho chế độ 4 khi ăn mồi quiz
            if (modeGame == 4) {
                if (food.getType().getEffect() instanceof QuizFoodEffect) {
                    pauseGame();  // Dừng game khi ăn mồi quiz
                    ((QuizFoodEffect) food.getType().getEffect()).setGameViewController(this);  // Thiết lập controller game cho quiz
                }
            }

            // Di chuyển rắn và áp dụng hiệu ứng của mồi
            snake.move(newHead);
            drawSnake();  // Vẽ lại rắn
            food.getType().getEffect().applyEffect(snake, gameConfig, gameState);  // Áp dụng hiệu ứng của mồi
            spawnFood();  // Tạo mồi mới
            updateScoreDisplay();  // Cập nhật hiển thị điểm số
        }
    }

    // Áp dụng hiệu ứng fade cho mồi khi bị ăn
    public void onFoodEaten(Button foodButton) {
        // Hiệu ứng fade để làm mồi biến mất rồi hiện lại
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), foodButton);
        fade.setFromValue(1.0);  // Bắt đầu với độ mờ hoàn toàn
        fade.setToValue(0.0);    // Mờ dần đến độ mờ 0
        fade.setCycleCount(2);   // Lặp lại hiệu ứng fade hai lần
        fade.setAutoReverse(true);  // Tự động đảo ngược hiệu ứng

        fade.play();  // Bắt đầu hiệu ứng fade
    }
    
 // Phương thức khởi tạo âm thanh cho game
    private void initializeSounds() {
        // Âm thanh khi ăn mồi
        URL eatResource = getClass().getResource("/view/music/score.ogg");
        if (eatResource != null) {
            eatSound = new AudioClip(eatResource.toString());
        } else {
            System.out.println("Không tìm thấy file âm thanh khi ăn mồi! Kiểm tra lại đường dẫn.");
        }

        // Âm thanh khi game over
        URL gameOverResource = getClass().getResource("/view/music/Chet.ogg");
        if (gameOverResource != null) {
            gameOverSound = new AudioClip(gameOverResource.toString());
        } else {
            System.out.println("Không tìm thấy file âm thanh khi game over! Kiểm tra lại đường dẫn.");
        }
    }

    // Phát âm thanh khi ăn mồi
    private void playEatSound() {
        if (eatSound != null) {
            System.out.println("Đang phát âm thanh ăn mồi...");
            eatSound.play();  // Phát âm thanh ăn mồi
        } else {
            System.out.println("Không có âm thanh để phát.");
        }
    }

    // Phát âm thanh khi game over
    private void playGameOverSound() {
        if (gameOverSound != null) {
            gameOverSound.play();  // Phát âm thanh khi game over
        }
    }

    // Phương thức khởi động quiz khi rắn ăn mồi
    public void startQuiz() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizView.fxml"));
        try {
            Parent quizRoot = loader.load();
            QuestionController questionController = loader.getController();
            questionController.setGameViewController(this); // Truyền GameViewController vào QuestionController
            // Thiết lập giao diện quiz dưới dạng modal
            Stage quizStage = new Stage();
            quizStage.setTitle("Câu hỏi");
            quizStage.initStyle(StageStyle.UNDECORATED);
            quizStage.initOwner(gameGrid.getScene().getWindow());
            quizStage.initModality(Modality.APPLICATION_MODAL);
            quizStage.setScene(new Scene(quizRoot));
            
            // Phát âm thanh quiz khi quiz bắt đầu
            QuestionController questionController2 = new QuestionController();
            questionController2.playSound();
            
            quizStage.showAndWait();
            resumeGame();  // Tiếp tục game sau khi quiz kết thúc
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sinh mồi cho game
    private void spawnFood() {
        int row, col;
        do {
            row = random.nextInt(gameConfig.getMapSize());
            col = random.nextInt(gameConfig.getMapSize());
        } while (isFoodOnSnake(new Position(row, col)));

        // Kiểm tra chế độ game để quyết định loại mồi
        if (modeGame == 4) {
            food = FoodFactory.createRandomFood(row, col);  // Tạo mồi ngẫu nhiên
        } else  {
            food = FoodFactory.createNormalFood(row, col);  // Tạo mồi bình thường
        }
        System.out.println("food spawn: " + row * gameConfig.getMapSize() + col);  // In ra vị trí của mồi
    }

    // Kiểm tra xem mồi có trùng với vị trí thân rắn hay không
    private boolean isFoodOnSnake(Position foodPosition) {
        return ob.contains(foodPosition) || snake.getBody().contains(foodPosition);
    }

    // Cập nhật hiển thị điểm số trên giao diện
    private void updateScoreDisplay() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score.getCurrentScore());  // Cập nhật điểm số
        });
    }

    // Kết thúc trò chơi khi người dùng nhấn phím tắt
    @FXML
    private void end() {
        playGameOverSound();  // Phát âm thanh game over
        Alert alertend = new Alert(AlertType.CONFIRMATION);
        alertend.setTitle("Thông báo");
        alertend.setContentText("Bạn có chắc muốn chơi lại ván mới?");
        
        // Các nút cho phép chơi lại hoặc đóng trò chơi
        ButtonType buttonPlayAgain = new ButtonType("Chơi lại");
        ButtonType buttonExit = new ButtonType("Đóng", ButtonType.CLOSE.getButtonData());
        alertend.getButtonTypes().setAll(buttonPlayAgain, buttonExit);
        
        Optional<ButtonType> result = alertend.showAndWait();
        if (result.isPresent() && result.get() == buttonPlayAgain) {
            initialize();  // Khởi tạo lại trò chơi nếu chọn chơi lại
        }
    }

    // Kết thúc trò chơi và hiển thị thông báo kết thúc
    private void endGame(String message) {
        isGameOver = true;
        String username = readUsernameFromFile();
        int diem = score.getCurrentScore();
        
        // Lưu điểm số và tên người chơi vào cơ sở dữ liệu
        new ScoreDAO().saveScore(diem, username, modeGame);

        // Hiển thị thông báo kết thúc trò chơi
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Kết thúc trò chơi");
        alert.setHeaderText(message);
        alert.setContentText("Bạn có muốn chơi lại hay thoát?");
        
        ButtonType buttonPlayAgain = new ButtonType("Chơi lại");
        ButtonType buttonExit = new ButtonType("Trang chủ");
        alert.getButtonTypes().setAll(buttonPlayAgain, buttonExit);
        
        // Hiển thị hộp thoại và chờ phản hồi của người dùng
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonPlayAgain) {
            initialize();  // Khởi tạo lại trò chơi nếu chọn chơi lại
        } else {
        	showMenuMain();
        }
    }

    // Quay về menu chính khi người dùng chọn
    @FXML
    private void switchMenuMain() {
        Alert alertMain = new Alert(AlertType.CONFIRMATION);
        alertMain.setTitle("Thông báo");
        alertMain.setHeaderText("");
        alertMain.setContentText("Bạn có chắc chắn muốn trở về trang chủ?");
        
        ButtonType buttonMain = new ButtonType("Có");
        ButtonType buttonExit = new ButtonType("Thoát", ButtonType.CLOSE.getButtonData());
        alertMain.getButtonTypes().setAll(buttonMain, buttonExit);
        
        Optional<ButtonType> result = alertMain.showAndWait();
        if (result.isPresent() && result.get() == buttonMain) {
        	showMenuMain();
        }
    }
    
    private void showMenuMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/menuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) scoreLabel.getScene().getWindow();
            stage.close();  // Đóng cửa sổ hiện tại

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.setTitle("Game Snake");
            newStage.show();  // Hiển thị cửa sổ chính
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Cập nhật điểm số từ quiz vào điểm hiện tại
    public void updateScoreFromQuiz(int points) {
        // Cộng dồn điểm từ quiz vào điểm hiện tại
        int currentScore = score.getCurrentScore();
        score.setCurrentScore(currentScore + points);

        // Cập nhật hiển thị điểm số
        updateScoreDisplay();
    }

}
