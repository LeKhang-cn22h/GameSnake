package model;

public class GameState {
    private double snakeSpeed;

    public GameState(double initialSpeed) {
        this.snakeSpeed = initialSpeed;
    }

    public double getSnakeSpeed() {
        return snakeSpeed;
    }

    public void setSnakeSpeed(double snakeSpeed) {
        this.snakeSpeed = snakeSpeed;
    }
}
