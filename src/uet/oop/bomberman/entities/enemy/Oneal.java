package uet.oop.bomberman.entities.enemy;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.Constant;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.particles.Particle;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;
import uet.oop.bomberman.utils.Audio;
import uet.oop.bomberman.utils.Vector2D;
import uet.oop.bomberman.utils.pathfinding.Grid;
import uet.oop.bomberman.utils.pathfinding.PathFinding;

import java.util.List;
import java.util.Random;

public class Oneal extends Enemy {
    private static final int CHASE_RANGE = 5;
    private static final int POINT = 200;
    private static final int VELOCITY = 1;

    private SpriteAnimation forward_animation = new SpriteAnimation(Duration.millis(600), Sprite.oneal_move_forward_animation);
    private SpriteAnimation backward_animation = new SpriteAnimation(Duration.millis(600), Sprite.oneal_move_backward_animation);
    private SpriteAnimation death_animation = new SpriteAnimation(Duration.millis(700), Sprite.oneal_death_animation);
    private SpriteAnimation currentAnimation = forward_animation;

    private Vector2D currentDir = new Vector2D(0, -1);

    public Oneal(int x, int y, Image image) {
        super(x, y, image, VELOCITY, POINT);
    }

    @Override
    public void update() {
        if (!isDead) {
            if (!isMoving) {
                findTarget();
            } else {
                move(target);
            }
            if (currentDir.equals(new Vector2D(0,-1)) || currentDir.equals(new Vector2D(1, 0))) {
                changeAnimationState(1);
            }
            if (currentDir.equals(new Vector2D(0,1)) || currentDir.equals(new Vector2D(-1, 0))) {
                changeAnimationState(2);
            }
            collideWithExplosion();
        }
        playAnimation();
    }

    @Override
    protected void findTarget() {

        // if oneal is close to the player enough then it will follow the path that chase the player.
        // or else it will follow a random move.
        if (getDistanceInUnit(BombermanGame.game.getPlayer()) <= CHASE_RANGE) {
            Grid grid = generateGrid();
            List<Vector2D> path = PathFinding.findPath(grid, new Vector2D(getUnitX(), getUnitY()), new Vector2D(BombermanGame.game.getPlayer().getUnitX(), BombermanGame.game.getPlayer().getUnitY()), false);
            if (path != null && !path.isEmpty() && path.size() <= 7) {
                isMoving = true;
                target = new Vector2D(path.get(0).getX() * Sprite.SCALED_SIZE, path.get(0).getY() * Sprite.SCALED_SIZE);
                currentDir = new Vector2D(path.get(0).getX() - getUnitX(), path.get(0).getY() - getUnitY());
            } else {
                findRandomMove();
            }
        } else {
           findRandomMove();
        }

    }

    private void findRandomMove() {
        int unitX = getUnitX();
        int unitY = getUnitY();
        Vector2D left = getLeft(currentDir);
        Vector2D right = getRight(currentDir);
        Vector2D back = getBack(currentDir);

        boolean canMoveForward = isWalkable(unitX + currentDir.getX(), unitY + currentDir.getY());
        boolean canMoveLeft = isWalkable(unitX + left.getX(), unitY + left.getY());
        boolean canMoveRight = isWalkable(unitX + right.getX(), unitY + right.getY());
        boolean canMoveBack =  isWalkable(unitX + back.getX(), unitY + back.getY());

        if (!canMoveForward && !canMoveLeft && !canMoveRight) {
            if (canMoveBack) {
                isMoving = true;
                currentDir = back;
                target = new Vector2D((unitX + back.getX()) * Sprite.SCALED_SIZE, (unitY + back.getY()) * Sprite.SCALED_SIZE);
            }
        } else {
            // if oneal can move to both 3 of these direction
            // then chance to move forward is 70%, change to move right or left is 30%.
            if (canMoveForward && canMoveLeft && canMoveRight) {
                int chance = new Random().nextInt(101);
                // if chance is in range 1 - 40 then we will not change the current direction.
                if (chance >= 70 && chance <= 85) {
                    currentDir = left;
                }
                if (chance >= 86 && chance <= 100) {
                    currentDir = right;
                }
            }
            if (!canMoveForward && canMoveLeft && canMoveRight) {
                int chance = new Random().nextInt(2);
                if (chance == 0) {
                    currentDir = left;
                } else {
                    currentDir = right;
                }
            }
            if (canMoveForward && !canMoveLeft && canMoveRight) {
                int chance = new Random().nextInt(101);
                if (chance >= 70 && chance <= 100) {
                    currentDir = right;
                }
            }
            if (canMoveForward && canMoveLeft && !canMoveRight) {
                int chance = new Random().nextInt(101);
                if (chance >= 70 && chance <= 100) {
                    currentDir = left;
                }
            }
            if (!canMoveForward && canMoveLeft && !canMoveRight) {
                currentDir = left;
            }
            if (!canMoveForward && !canMoveLeft && canMoveRight) {
                currentDir = right;
            }

            isMoving = true;
            target = new Vector2D((unitX + currentDir.getX()) * Sprite.SCALED_SIZE, (unitY + currentDir.getY()) * Sprite.SCALED_SIZE);
        }
    }

    @Override
    protected void setDeath() {
        isDead = true;

        // change current animation to death animation.
        changeAnimationState(0);
        Audio.playEntityDie();
    }

    @Override
    public Bounds getBound() {
        if (!isDead) {
            Rectangle rectangle = new Rectangle(x, y,Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            return rectangle.getBoundsInParent();
        } else {
            return null;
        }
    }

    /**
     * change animation state based on the value of i.
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

    private void playAnimation() {
        if (currentAnimation != null) {
            if (currentAnimation != death_animation) {
                currentAnimation.play();
            }
            this.img = currentAnimation.getCurrentImage();
        }
    }

    private Vector2D getLeft(Vector2D forward) {
        if (forward.equals(new Vector2D(0, -1))) {
            return new Vector2D(-1, 0);
        }
        if (forward.equals(new Vector2D(1,0))) {
            return new Vector2D(0, -1);
        }
        if (forward.equals(new Vector2D(0, 1))) {
            return new Vector2D(1, 0);
        }
        if (forward.equals(new Vector2D(-1, 0))) {
            return new Vector2D(0, 1);
        }
        return null;
    }

    private Vector2D getRight(Vector2D forward) {
        if (forward.equals(new Vector2D(0, -1))) {
            return new Vector2D(1, 0);
        }
        if (forward.equals(new Vector2D(1, 0))) {
            return new Vector2D(0, 1);
        }
        if (forward.equals(new Vector2D(0, 1))) {
            return new Vector2D(-1, 0);
        }
        if (forward.equals(new Vector2D(-1, 0))) {
            return new Vector2D(0, -1);
        }
        return null;
    }

    private Vector2D getBack(Vector2D forward) {
        return new Vector2D(0 - forward.getX(), 0 - forward.getY());
    }

    /** used for pathfinding. */
    private Grid generateGrid() {
        return new Grid(Constant.WIDTH, Constant.HEIGHT, BombermanGame.game.getStillObjectMap());
    }

    private int getDistanceInUnit(Entity entity) {
        int offsetX = Math.abs(getUnitX() - entity.getUnitX());
        int offsetY = Math.abs(getUnitY() - entity.getUnitY());
        return (int) Math.sqrt(offsetX * offsetX + offsetY * offsetY);
    }

    @Override
    protected boolean isWalkable(int x, int y) {
        return BombermanGame.game.getStillObjectMap()[y][x] == 0;
    }
}
