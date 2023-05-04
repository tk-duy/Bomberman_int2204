package uet.oop.bomberman.entities.stillobjects;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Wall extends Entity {

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {
        // do nothing.
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x, y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        return rectangle.getBoundsInParent();
    }
}
