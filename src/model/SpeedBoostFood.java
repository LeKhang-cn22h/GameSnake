package model;

public class SpeedBoostFood extends Food {
    public SpeedBoostFood(int row, int col) {
        super(row, col, FoodType.SPEED); // Gọi constructor của lớp cha (Food)
    }
}
