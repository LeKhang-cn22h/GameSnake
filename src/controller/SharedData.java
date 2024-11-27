package controller;

import javafx.scene.image.Image;

public class SharedData {
    // Màu mặc định cho rắn
    private static String selectedColor = "#00FF00"; 

    // Hình nền mặc định
    private static Image selectedBgr = null; 

    // Chế độ mặc định (1: Cổ Điển, 2: Tự Do, ...)
    private static int selectedMode = 1;

    // Tốc độ mặc định
    private static double Speed = 200;

    /**
     * Lấy màu rắn đã chọn
     * @return Màu đã chọn
     */
    public static String getSelectedColor() {
        return selectedColor;
    }
    
    /**
     * Đặt màu rắn đã chọn
     * @param color Màu mới
     */
    public static void setSelectedColor(String color) {
        selectedColor = color;
    }

    /**
     * Đặt tốc độ game
     * @param speed Tốc độ mới
     */
    public static void setSpeed(double speed) {
        Speed = speed;
    }

    /**
     * Lấy tốc độ game hiện tại
     * @return Tốc độ game
     */
    public static double getSpeed() {
        return Speed;
    }

    /**
     * Lấy hình nền đã chọn
     * @return Hình nền đã chọn
     */
    public static Image getSelectedBgr() {
        return selectedBgr;
    }
    
    /**
     * Đặt hình nền đã chọn
     * @param bgr Hình nền mới
     */
    public static void setSelectedBgr(Image bgr) {
        selectedBgr = bgr;
    }

    /**
     * Lấy chế độ đã chọn
     * @return Chế độ đã chọn
     */
    public static int getSelectedMode() {
        return selectedMode;
    }
    
    /**
     * Đặt chế độ đã chọn
     * @param mode Chế độ mới
     */
    public static void setSelectedMode(int mode) {
        selectedMode = mode;
    }
}
