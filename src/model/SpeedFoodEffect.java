package model;

public class SpeedFoodEffect implements FoodEffect {
    @Override
    public void applyEffect(Snake snake, GameConfig gameConfig, GameState gameState) {
        System.out.println("Speed food eaten. Snake speeds up.");
        int newSpeed = Math.max(gameState.getSnakeSpeed() - 100, 50); // Giảm tốc độ tối thiểu còn 50ms
        gameState.setSnakeSpeed(newSpeed); // Cập nhật trạng thái
    }
}
