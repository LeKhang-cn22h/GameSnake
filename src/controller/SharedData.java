package controller;

import javafx.scene.image.Image;

public class SharedData {
    private static String selectedColor = "yellow"; // Màu mặc định
    private static Image selectedBgr = null; // Màu mặc định
    private static int selectedMode = 1;

    public static String getSelectedColor() {
        return selectedColor;
    }
    
    public static void setSelectedColor(String color) {
        selectedColor = color;
    }
    
    public static Image getSelectedBgr() {
        return selectedBgr;
    }
    
    public static void setSelectedBgr(Image bgr) {
    	selectedBgr = bgr;
    }
    public static int getSelectedMode() {
        return selectedMode;
    }
    
    public static void setSelectedMode(int mode) {
        selectedMode = mode;
    }
}
