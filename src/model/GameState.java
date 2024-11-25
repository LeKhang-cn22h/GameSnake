package model;

public class GameState {
    private int snakeSpeed;

    public GameState(int initialSpeed) {
        this.snakeSpeed = initialSpeed;
    }

    public int getSnakeSpeed() {
        return snakeSpeed;
    }

    public void setSnakeSpeed(int snakeSpeed) {
        this.snakeSpeed = snakeSpeed;
        System.out.println("Snake speed updated to: " + snakeSpeed);
    }
}
