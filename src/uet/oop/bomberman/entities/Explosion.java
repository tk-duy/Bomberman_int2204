package uet.oop.bomberman.entities;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;

public class Explosion extends Entity {
    private SpriteAnimation animation;
    private final ExplosionType type;
    private final int duration = 300;
    private long startTime;

    public Explosion(int x, int y, ExplosionType type) {
        super(x, y);
        this.type = type;
        switch (type) {
            case CENTER:
                animation = new SpriteAnimation(Duration.millis(300), Sprite.explosion_center_animation);
                break;
            case VERTICLE:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_vertical_animation);
                break;
            case VERTICLE_TOP:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_vertical_top_animation);
                break;
            case VERTICLE_DOWN:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_vertical_down_animation);
                break;
            case HORIZONTAL:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_horizontal_animation);
                break;
            case HORIZONTAL_RIGHT:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_horizontal_right_animation);
                break;
            case HORIZONTAL_LEFT:
                animation = new SpriteAnimation(Duration.millis(400), Sprite.explosion_horizontal_left_animation);
                break;
        }
        animation.setCycleCount(1);
        startTime = System.currentTimeMillis();
    }

    private void playAnimation() {
        animation.play();
        this.img = animation.getCurrentImage();
    }

    private void setDeath() {
        animation.stop();
        BombermanGame.game.getGarbageEntities().add(this);
    }

    @Override
    public void update() {
        playAnimation();
        if (System.currentTimeMillis() - startTime >= duration) {
            setDeath();
        }
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x - 8, y - 8, Sprite.SCALED_SIZE - 16, Sprite.SCALED_SIZE - 16);
        return rectangle.getBoundsInParent();
    }
}
