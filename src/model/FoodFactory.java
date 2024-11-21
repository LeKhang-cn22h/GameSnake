package model;

import java.util.Random;
public class FoodFactory {
    public static Food createNormalFood(int row, int col) {
        return new Food(row, col, FoodType.NORMAL);
    }

    public static Food createRandomFood(int row, int col) {
        int chance = new Random().nextInt(100); // Xác suất xuất hiện
        if (chance < 50) {
            return new Food(row, col, FoodType.SPEED);
        } else if (chance < 80) {
            return new Food(row, col, FoodType.SLOW);
        } else {
            return new Food(row, col, FoodType.QUIZZ);
        }
    }
}

