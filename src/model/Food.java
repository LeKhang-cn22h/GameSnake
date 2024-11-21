package model;

public class Food {
    private int row;
    private int col;
    private FoodType type;

    public Food(int row, int col, FoodType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Position getPosition() {
        return new Position(row, col);
    }

    public FoodType getType() {
        return type;
    }
}
