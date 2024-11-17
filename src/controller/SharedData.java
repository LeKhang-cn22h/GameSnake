package controller;

public class SharedData {
    private static String selectedColor = "yellow"; // Màu mặc định

    public static String getSelectedColor() {
        return selectedColor;
    }

    public static void setSelectedColor(String color) {
        selectedColor = color;
    }
}
