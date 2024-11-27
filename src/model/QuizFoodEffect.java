package model;

import controller.GameViewController;

public class QuizFoodEffect implements FoodEffect {
    private GameViewController gameViewController;

    // Constructor mặc định
    public QuizFoodEffect() {
        // Constructor mặc định không làm gì cả
    }

    // Constructor với tham số
    public QuizFoodEffect(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    // Phương thức setter để thiết lập gameViewController
    public void setGameViewController(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

    @Override
    public void applyEffect(Snake snake, GameConfig gameConfig, GameState gameState) {
        System.out.println("Quiz food eaten. Triggering quiz challenge.");

        if (gameViewController != null) {
            gameViewController.startQuiz();
        } else {
            System.err.println("Error: gameViewController is null!");
        }
    }
}
