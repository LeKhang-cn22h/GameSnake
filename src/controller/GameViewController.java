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
        initializeSounds();  // Gọi phương thức này trong constructor
    }
   //test trong SnakeHead
    
    @FXML
    public void initialize() {
    	APIWeatherTime();
        this.gameState = new GameState(SharedData.getSpeed()); // 200ms là tốc độ mặc định
        newHead = new Position(10, 10);
    	modeGame = SharedData.getSelectedMode();
    	colorSnake = SharedData.getSelectedColor();
    	BgrMap = SharedData.getSelectedBgr();
    	isGameOver = false;
    	gameGrid.getChildren().clear();
        gameConfig = new GameConfig(20, 30, 30);
        stackP.setPrefWidth(gameConfig.getButtonWidth()*gameConfig.getMapSize());
        stackP.setPrefHeight(gameConfig.getButtonHeight()*gameConfig.getMapSize());
        stackP.setFocusTraversable(false);
        labelRecord.setText("Kỷ lục:"+new ScoreDAO().getHighestScore(modeGame));
     // Random vị trí mới cho food
//        Position randomPos = randomPosition();
        
//        food = new Food(randomPos.getCol(), randomPos.getRow(), FoodType.NORMAL);
        snake = new Snake(10, 10, gameConfig);
        random = new Random();
        spawnFood();
        score = new Score(0);
        updateScoreDisplay();
        createGameGrid();
        setImgGame();
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
 

    private void APIWeatherTime() {
        try {
            // Lấy dữ liệu thời tiết
            String city = "Ho Chi Minh City"; // Thành phố
            String weatherData = APIClient.getWeatherData(city);
            // Phân tích dữ liệu
            String weather = WeatherParser.parseWeather(weatherData);
            int hour = WeatherParser.parseTime(weatherData);
            // Khởi tạo GameEnvironment
            GameEnvironment gameEnvironment = new GameEnvironment(weather, hour, this);

            // Cập nhật môi trường game dựa trên thời tiết
            gameEnvironment.updateGameBasedOnWeather();
            System.out.println("chay xong");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public void updateWeather(Image img) {
        imgWeather.setImage(img);
    }
    public void updateDarkMode(String styleBoard, String styleMenu, String styleUser, String styleButton, String styleScore) {
//    	BoardMain.setStyle("-fx-background-color: lightblue;");
    	BoardMain.setStyle(styleBoard);
    	MenuInfor.setStyle(styleMenu);
    	userTxt.setStyle(styleUser);
    	menuButton.setStyle(styleButton);
    	scoreLabel.setStyle(styleScore);
    }
//    public void updateMenuInfor(Image img) {
//        imgWeather.setImage(img);
//    }

    public Position randomPosition() {
        Random random = new Random();
        int x = random.nextInt(gameConfig.getMapSize()); // MAP_WIDTH là chiều rộng của map
        int y = random.nextInt(gameConfig.getMapSize()); // MAP_HEIGHT là chiều cao của map
        return new Position(x, y);
    }


	private void createGameGrid() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = new Button();
                button.setPrefSize(gameConfig.getButtonWidth(), gameConfig.getButtonHeight());
                button.setStyle("-fx-background-color: transparent;");
                button.setFocusTraversable(false);
                gameGrid.add(button, col, row);
            }
        }
        
        System.out.println("Tạo bàn cờ");
    }
    
    private void addOb() {
    	obImage = getClass().getResource("/view/image_chuongngaivat/stone.png").toExternalForm();
        for (Position position : ob) {
            Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
            button.setStyle("-fx-background-image: url('" + obImage + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-repeat: no-repeat; " +
                    "-fx-background-color: transparent; " +
                    "-fx-background-position: center;");
        }
    }

	private void setImgGame() {
	    // Tạo ImageView nếu chưa có
	if (imgGame == null) {
	    imgGame = new ImageView();
	}
	
	// Đặt hình ảnh cho ImageView
	imgGame.setImage(BgrMap);
	
	// Điều chỉnh kích thước ImageView (nếu cần)
	imgGame.setFitWidth(gameConfig.getButtonWidth()*gameConfig.getMapSize()-16); // Chiều rộng
	imgGame.setFitHeight(gameConfig.getButtonHeight()*gameConfig.getMapSize()-16); // Chiều cao
	imgGame.setPreserveRatio(false);
    // Thêm các thành phần vào StackPane
    // Căn chỉnh các thành phần (nếu cần)
    StackPane.setAlignment(imgGame, Pos.CENTER); // Hình nền căn giữa
    StackPane.setAlignment(gameGrid, Pos.CENTER); // Bàn cờ căn giữa
	}
	
	private void reset() {
		snake = new Snake(10, 10, gameConfig);
		newHead = new Position(10,10);
        addOb();
        checkMap=false;
	}
	
	private boolean checkChangeMap() {
		if(score.getCurrentScore()==100 || score.getCurrentScore()==200 || score.getCurrentScore()==300 || score.getCurrentScore()==400) {

			System.out.println("set snake");
			return true;
		}
		return false;
	}
//	private void changeMap(boolean checkChangeMap) {
//		
//	}

    private void drawSnake() {
        for (int row = 0; row < gameConfig.getMapSize(); row++) {
            for (int col = 0; col < gameConfig.getMapSize(); col++) {
                Button button = (Button) gameGrid.getChildren().get(row * gameConfig.getMapSize() + col);
                button.setStyle("-fx-background-color: transparent;");
                button.setFocusTraversable(false);
                if (modeGame==4) {
             	   addOb();
                }else if (modeGame==3) {
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

        	   
            }
        }
        
        
     // Duyệt qua tất cả các vị trí của cơ thể rắn
        for (int i = 0; i < snake.getBody().size(); i++) {
            Position position = snake.getBody().get(i);
            Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
        	drawHead();
            if(i==snake.getBody().size()-1){
                // Đuôi rắn
            	button.setStyle(
            			"-fx-background-color:  red; " + // Màu nền cho nút
            		            "-fx-background-radius: 50%;" +              // Tạo nền hình tròn
            		            "-fx-border-radius: 50%;"                    // Đảm bảo viền cũng bo tròn
            		        
            		);

               
            }else  if(i!=0) {
            	button.setStyle("-fx-background-color: " + colorSnake + ";");
            }
            
        }
    }


    private void drawHead() {
    	Position position = snake.getBody().get(0);
        Button button = (Button) gameGrid.getChildren().get(position.getRow() * gameConfig.getMapSize() + position.getCol());
            // Đầu rắn
            String headImage = getClass().getResource("/view/image_snake/up.png").toExternalForm();
            switch (snake.getCurrentDirection()) {
                case "UP":
                    headImage = getClass().getResource("/view/image_snake/up.png").toExternalForm();
                    break;
                case "DOWN":
                    headImage = getClass().getResource("/view/image_snake/down.png").toExternalForm();
                    break;
                case "LEFT":
                    headImage =getClass().getResource("/view/image_snake/left.png").toExternalForm();
                    break;
                case "RIGHT":
                    headImage = getClass().getResource("/view/image_snake/right.png").toExternalForm();
                    break;
            }
            button.setStyle("-fx-background-image:url(' " + headImage + "'); " +
                            "-fx-background-size: cover; " +
                            "-fx-background-position: center;");
    }
    private void drawFood() {
        String imageUrl;
        switch (food.getType()) {
            case NORMAL:
                imageUrl = getClass().getResource("/view/image_moi/dua_hau.png").toExternalForm();
                break;
            case SLOW:
                imageUrl = getClass().getResource("/view/image_moi/dautay.png").toExternalForm();
                break;
            case SPEED:
                imageUrl = getClass().getResource("/view/image_moi/traitao.png").toExternalForm();
                break;
            case QUIZZ:
                imageUrl = getClass().getResource("/view/image_moi/traitim.png").toExternalForm();
                break;
            default:
                imageUrl = getClass().getResource("/view/image_moi/default_food.png").toExternalForm();
        }

        Button foodButton = (Button) gameGrid.getChildren().get(
            food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());

        foodButton.setStyle("-fx-background-image: url('" + imageUrl + "'); " +
                            "-fx-background-size: 33px 33px; " +
                            "-fx-background-repeat: no-repeat; " +
                            "-fx-background-color: transparent; " +
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
                    	newHead = snake.calculateNewHeadPosition(modeGame);  // Tính toán vị trí mới của đầu rắn
//                    	APIWeatherTime();
                        checkCollision();
                        snake.move(newHead);
                        
                        drawSnake();
                        drawFood();
                        drawHead();
                        snake.updateDirectionAfterMove();
                    });
                }
                try {
                    Thread.sleep((long) gameState.getSnakeSpeed());
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
    	
        if (isPaused) {
            countdownLabel.setVisible(true);
            directionLabel.setVisible(true); // Hiển thị Label hướng di chuyển
            final int[] countdown = {3};

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

            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                countdownLabel.setText(String.valueOf(countdown[0]));
                countdown[0]--;

                // Cập nhật hướng di chuyển của rắn
                String currentDirection = snake.getCurrentDirection(); // Truy cập đối tượng Snake toàn cục
                switch (currentDirection) {
                    case "UP": directionLabel.setText("⬆"); break;
                    case "DOWN": directionLabel.setText("⬇"); break;
                    case "LEFT": directionLabel.setText("⬅"); break;
                    case "RIGHT": directionLabel.setText("➡"); break;
                    default: directionLabel.setText("N/A"); break;
                }

                scaleTransition.play();
                fadeTransition.play();

                if (countdown[0] < 0) {
                    timeline.stop();
                    countdownLabel.setText("Bắt đầu");

                    FadeTransition startFadeTransition = new FadeTransition(Duration.seconds(0.5), countdownLabel);
                    startFadeTransition.setFromValue(0.0);
                    startFadeTransition.setToValue(1.0);
                    startFadeTransition.play();

                    startFadeTransition.setOnFinished(event1 -> {
                        countdownLabel.setVisible(false);
                        directionLabel.setVisible(false); // Ẩn Label hướng
                        isPaused = false;
                        gameGrid.requestFocus();
                        System.out.println("Game Resumed");
                    });
                }
            }));
            
            timeline.setCycleCount(countdown[0] + 1);
            timeline.play();
        }
    }







    
    // Kiểm tra va chạm
    private void checkCollision() {
    	
        if (isGameOver) {
        	playGameOverSound();
        	return;
        
        }
        
        Position head = newHead;
        if(modeGame != 2) {
	        // Check collision with walls
	        if (head.getRow() < 0 || head.getRow() >= gameConfig.getMapSize() || head.getCol() < 0 || head.getCol() >= gameConfig.getMapSize() || ((modeGame == 3 || modeGame == 4)&&ob.contains(head))) {
	            playGameOverSound();  // Play game over sound
	            endGame("Game Over: You hit the wall!");  // End the game
	        }
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
            playEatSound();
            Button foodButton = (Button) gameGrid.getChildren().get(food.getPosition().getRow() * gameConfig.getMapSize() + food.getPosition().getCol());
        	onFoodEaten(foodButton); // Gọi vào phương thức onFoodEaten với tham số là foodButton

            snake.grow(newHead);
            score.increaseScore();
            checkMap=checkChangeMap();
            System.out.println("Score increased, current score: " + score.getCurrentScore());  // Kiểm tra ở đây
            
            if (modeGame == 4) {
                if (food.getType().getEffect() instanceof QuizFoodEffect) {
                    pauseGame();
                    ((QuizFoodEffect) food.getType().getEffect()).setGameViewController(this);
                }
            }
            
//            foodButton.setStyle("-fx-background-image: none");
            
            snake.move(newHead);
            drawSnake();
            food.getType().getEffect().applyEffect(snake, gameConfig, gameState);
            spawnFood();
            updateScoreDisplay();
        }

    }
//hiệu ứng khi ăn mồi
    public void onFoodEaten(Button foodButton) {
        // Hiệu ứng fade (nhấp nháy)
        FadeTransition fade = new FadeTransition(Duration.seconds(0.5), foodButton);
        fade.setFromValue(1.0); // Bắt đầu từ độ mờ hoàn toàn
        fade.setToValue(0.0);   // Mờ dần đến 0
        fade.setCycleCount(2);  // Lặp lại hai lần (mờ đi rồi hiện lại)
        fade.setAutoReverse(true); // Tự động đảo ngược hiệu ứng

        // Khi hiệu ứng kết thúc, ẩn mồi

        // Bắt đầu hiệu ứng
        fade.play();
        }
    
    
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
        }}
    private void playEatSound() {
        if (eatSound != null) {
            System.out.println("Đang phát âm thanh ăn mồi...");
            eatSound.play();  // Phát âm thanh đã được tải sẵn
        } else {
            System.out.println("Không có âm thanh để phát.");
        }
    }
    private void playGameOverSound() {
    	if (gameOverSound != null) {
            gameOverSound.play();  // Phát âm thanh khi game over
        }
    }
    public void startQuiz() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/QuizView.fxml"));
        try {
            Parent quizRoot = loader.load();
            QuestionController questionController = loader.getController();
            questionController.setGameViewController(this); // Truyền GameViewController vào QuestionController
            // Thiết lập giao diện dưới dạng modal
            Stage quizStage = new Stage();
            quizStage.setTitle("Câu hỏi");
            quizStage.initStyle(StageStyle.UNDECORATED);
            quizStage.initOwner(gameGrid.getScene().getWindow());
            quizStage.initModality(Modality.APPLICATION_MODAL);
            quizStage.setScene(new Scene(quizRoot));
            QuestionController questionController2 = new QuestionController();
			questionController2.playSound();
            quizStage.showAndWait();
            imagePause();
            resumeGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void imagePause() {
		snake.getBody();
	}
	private void spawnFood() {
        int row, col;
        do {
            row = random.nextInt(gameConfig.getMapSize());
            col = random.nextInt(gameConfig.getMapSize());
        } while (isFoodOnSnake(new Position(row, col)));
//        System.out.println(modeGame);
        if (modeGame == 4) {
            food = FoodFactory.createRandomFood(row, col);
//            System.out.println(row+" "+col);
        } else  {
            food = FoodFactory.createNormalFood(row, col);
        }
        System.out.println("food spawn: "+row * gameConfig.getMapSize() + col);
    }

	
    private boolean isFoodOnSnake(Position foodPosition) {
      return ob.contains(foodPosition)||snake.getBody().contains(foodPosition);
  }
    private void updateScoreDisplay() {
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score.getCurrentScore());
            
        });
    }

    
    @FXML
//kết thúc trò chơi bàn phím tắt
    private void end() {
    	playGameOverSound();
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
        new ScoreDAO().saveScore(diem, username,modeGame);
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
