package uet.oop.bomberman;

import uet.oop.bomberman.graphics.Sprite;

public class Constant {
    public static int WIDTH = 25;
    public static int HEIGHT = 15;
    public static int BAR_WIDTH = WIDTH * Sprite.SCALED_SIZE;
    public static int BAR_HEIGHT = 2 * Sprite.SCALED_SIZE / 2;

    public static int TIME = 3 * 60 * 1000;

    public static int WIDTH_PIXEL = WIDTH * Sprite.SCALED_SIZE;
    public static int HEIGHT_PIXEL = (HEIGHT + BAR_HEIGHT) * Sprite.SCALED_SIZE;

    public static int TOTAL_LEVEL = 2;
}
