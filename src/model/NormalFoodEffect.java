package model;

public class NormalFoodEffect implements FoodEffect {
    @Override
    public void applyEffect(Snake snake, GameConfig gameConfig, GameState gameState) {
        System.out.println("Normal food eaten. No special effect.");
    }
}
