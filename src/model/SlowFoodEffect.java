package model;

public class SlowFoodEffect implements FoodEffect {
    @Override
    public void applyEffect(Snake snake, GameConfig gameConfig, GameState gameState) {
        System.out.println("Slow food eaten. Snake slows down.");

        double newSpeed = gameState.getSnakeSpeed() + 100; // Tăng thời gian chạy của thread
        gameState.setSnakeSpeed(newSpeed); // Cập nhật trạng thái
        
    }
}
