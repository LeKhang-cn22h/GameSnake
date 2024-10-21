package model;

public class Food {
    private Position position;

    public Food(int row, int col) {
        position = new Position(row, col);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position newPosition) {
        position = newPosition;
    }
}
