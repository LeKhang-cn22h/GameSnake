package model;

import controller.GameViewController;
public class QuizFoodEffect implements FoodEffect {
    private GameViewController gameViewController;

    // Constructor mặc định
    public QuizFoodEffect() {
        // Không làm gì ở đây
    }

    // Constructor với tham số
    public QuizFoodEffect(GameViewController gameViewController) {
        this.gameViewController = gameViewController;
    }

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

