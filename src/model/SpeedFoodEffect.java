package model;

public class SpeedFoodEffect implements FoodEffect {
    @Override
    public void applyEffect(Snake snake, GameConfig gameConfig, GameState gameState) {
        System.out.println("Speed food eaten. Snake speeds up.");
        double newSpeed = Math.max(gameState.getSnakeSpeed() - 100, 50); // Giảm thời gian chạy của thread tối thiểu còn 50
        gameState.setSnakeSpeed(newSpeed); // Cập nhật trạng thái
    }
}
