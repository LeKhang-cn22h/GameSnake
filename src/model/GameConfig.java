package model;

public class GameConfig {
    private int mapSize;
    private int buttonWidth;
    private int buttonHeight;

    public GameConfig(int mapSize, int buttonWidth, int buttonHeight) {
        this.mapSize = mapSize;
        this.buttonWidth = buttonWidth;
        this.buttonHeight = buttonHeight;
    }

    public int getMapSize() { return mapSize; }
    public int getButtonWidth() { return buttonWidth; }
    public int getButtonHeight() { return buttonHeight; }
}

