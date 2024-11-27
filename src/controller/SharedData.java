package controller;

import javafx.scene.image.Image;

public class SharedData {
    private static String selectedColor = "#00FF00"; // Màu mặc định
    private static Image selectedBgr = null; // Màu mặc định
    private static int selectedMode = 1;
    private static double Speed=200;

    public static String getSelectedColor() {
        return selectedColor;
    }
    
    public static void setSelectedColor(String color) {
        selectedColor = color;
    }
    public static void setSpeed(double speed) {
    	Speed=speed;
    }
    public static double getSpeed() {
    	return Speed;
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
