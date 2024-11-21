package model;
public enum FoodType {
    NORMAL(new NormalFoodEffect()),
    SLOW(new SlowFoodEffect()),
    SPEED(new SpeedFoodEffect()),
    QUIZZ(new QuizFoodEffect());

    private final FoodEffect effect;

    FoodType(FoodEffect effect) {
        this.effect = effect;
    }

    public FoodEffect getEffect() {
        return effect;
    }
}
