package uet.oop.bomberman.entities.enemy;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.particles.Particle;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;
import uet.oop.bomberman.utils.Audio;
import uet.oop.bomberman.utils.Vector2D;

import java.util.Random;

public class Ballom extends Enemy{
    private static final int VEL = 1;
    private static final int POINT = 100;
    private boolean forward = true;

    private final SpriteAnimation forward_animation = new SpriteAnimation(Duration.millis(600), Sprite.ballom_move_forward_animation);
    private final SpriteAnimation backward_animation = new SpriteAnimation(Duration.millis(600), Sprite.ballom_move_backward_animation);
    private final SpriteAnimation death_animation = new SpriteAnimation(Duration.millis(700), Sprite.ballom_death_animation);
    private SpriteAnimation currentAnimation = forward_animation;

    /**
     *  decide whether it's a vertical or horizontal ballom.
     *  if type == 0 then it will move horizontally
     *  if type == 1 then it will move vertically.
     */
    private int type;

    public Ballom(int x, int y, Image image) {
        super(x, y, image, VEL, POINT);

        // choose whether it's a vertical or horizontal ballom.
        type = new Random().nextInt(2);
    }

    @Override
    public void update() {
        if (!isDead) {
            if (!isMoving) {
                findTarget();
            } else {
                move(target);
            }
            // decide which animation to play.
            if (forward) {
                changeAnimationState(1);
            } else {
                changeAnimationState(2);
            }
            collideWithExplosion();
        }
        playAnimation();
    }

    @Override
    protected void findTarget() {
        if (type == 1) {
            int unitX = getUnitX();
            int unitY = getUnitY();
            if (forward) {
                // check is there any static object above the enemy.
                if (isWalkable(unitX, unitY - 1)) {
                    target = new Vector2D(unitX * Sprite.SCALED_SIZE, (unitY - 1) * Sprite.SCALED_SIZE);
                    isMoving = true;
                } else {
                    if (isWalkable(unitX,unitY + 1)) {
                        forward = false;
                        target = new Vector2D(unitX * Sprite.SCALED_SIZE, (unitY + 1) * Sprite.SCALED_SIZE);
                        isMoving = true;
                    } else {
                        target = null;
                    }
                }
            } else {
                if (isWalkable(unitX, unitY + 1)) {
                    target = new Vector2D(unitX * Sprite.SCALED_SIZE, (unitY + 1) * Sprite.SCALED_SIZE);
                    isMoving = true;
                } else {
                    if (isWalkable(unitX, unitY - 1)) {
                        forward = true;
                        target = new Vector2D(unitX * Sprite.SCALED_SIZE, (unitY - 1) * Sprite.SCALED_SIZE);
                        isMoving = true;
                    } else {
                        target = null;
                    }
                }
            }
        }
        if (type == 0) {
            int unitX = getUnitX();
            int unitY = getUnitY();
            if (forward) {
                // check is there any static object above the enemy.
                if (isWalkable(unitX + 1, unitY)) {
                    target = new Vector2D((unitX + 1) * Sprite.SCALED_SIZE, unitY * Sprite.SCALED_SIZE);
                    isMoving = true;
                } else {
                    if (isWalkable(unitX - 1, unitY)) {
                        forward = false;
                        target = new Vector2D((unitX - 1) * Sprite.SCALED_SIZE, unitY * Sprite.SCALED_SIZE);
                        isMoving = true;
                    } else {
                        target = null;
                    }
                }
            } else {
                if (isWalkable(unitX - 1, unitY)) {
                    target = new Vector2D((unitX - 1) * Sprite.SCALED_SIZE, unitY * Sprite.SCALED_SIZE);
                    isMoving = true;
                } else {
                    if (isWalkable(unitX + 1, unitY)) {
                        forward = true;
                        target = new Vector2D((unitX + 1) * Sprite.SCALED_SIZE, unitY * Sprite.SCALED_SIZE);
                        isMoving = true;
                    } else {
                        target = null;
                    }
                }
            }
        }
    }

    private void playAnimation() {
        if (currentAnimation != null) {
            if (currentAnimation != death_animation) {
                currentAnimation.play();
            }
            this.img = currentAnimation.getCurrentImage();
        }
    }

    /**
     * change animation state based on value of i.
     * if i == 1 change animation state to move forward.
     * if i == 2 change animation state to move backward.
     * if i == 0 change animation state to death.
     * @param i
     */
    private void changeAnimationState(int i) {
        if (i == 1) {
            if (currentAnimation != forward_animation) {
                currentAnimation.stop();
                currentAnimation = forward_animation;
                currentAnimation.setCycleCount(1);
            }
        }
        if (i == 2) {
            if (currentAnimation != backward_animation) {
                currentAnimation.stop();
                currentAnimation = backward_animation;
                currentAnimation.setCycleCount(1);
            }
        }
        if (i == 0) {
            if (currentAnimation != death_animation) {
                currentAnimation.stop();
                currentAnimation = death_animation;
                currentAnimation.setCycleCount(1);
                currentAnimation.setOnFinished(e -> {
                    BombermanGame.game.getGarbageEntities().add(this);
                });
                currentAnimation.play();
            }
        }
    }

    @Override
    protected void setDeath() {
       isDead = true;

       // change animation state to death animation.
       changeAnimationState(0);
        Audio.playEntityDie();
        Particle particle = new Particle("+" + getPoint(),Duration.millis(1500), getX() + Sprite.SCALED_SIZE / 2, getY(), 18, Color.YELLOW);
    }

    @Override
    public Bounds getBound() {
        if (!isDead) {
            Rectangle rectangle = new Rectangle(x, y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            return rectangle.getBoundsInParent();
        } else {
            return null;
        }
    }

    @Override
    protected boolean isWalkable(int x, int y) {
        return BombermanGame.game.getStillObjectMap()[y][x] == 0;
    }
}
