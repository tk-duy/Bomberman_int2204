package uet.oop.bomberman.entities;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    protected int x;

    protected int y;

    protected Image img;

    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    public Entity(int xUnit, int yUnit) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = null;
    }

    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getUnitX() {
        return (int) x / Sprite.SCALED_SIZE;
    }

    public int getUnitY() {
        return (int) y / Sprite.SCALED_SIZE;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract void update();

    public abstract Bounds getBound();
}
