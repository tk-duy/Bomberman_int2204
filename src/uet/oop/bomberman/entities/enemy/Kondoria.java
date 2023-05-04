package uet.oop.bomberman.entities.enemy;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.Constant;
import uet.oop.bomberman.entities.particles.Particle;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;
import uet.oop.bomberman.utils.Audio;
import uet.oop.bomberman.utils.Vector2D;
import uet.oop.bomberman.utils.pathfinding.Grid;
import uet.oop.bomberman.utils.pathfinding.PathFinding;

import java.util.List;
import java.util.Random;

public class Kondoria extends Enemy{
    private static int POINT = 1000;
    private static int VELOCITY = 1;

    private final SpriteAnimation forward_animation = new SpriteAnimation(Duration.millis(600), Sprite.kondoria_move_forward_animation);
    private final SpriteAnimation backward_animation = new SpriteAnimation(Duration.millis(600), Sprite.kondoria_move_backward_animation);
    private final SpriteAnimation death_animation = new SpriteAnimation(Duration.millis(600), Sprite.kondoria_death_animation);
    private SpriteAnimation currentAnimation = forward_animation;

    private Vector2D currentDir = new Vector2D(0, -1);

    public Kondoria(int x, int y, Image image) {
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
            Grid grid = generateGrid();
            List<Vector2D> path = PathFinding.findPath(grid, new Vector2D(getUnitX(), getUnitY()), new Vector2D(BombermanGame.game.getPlayer().getUnitX(), BombermanGame.game.getPlayer().getUnitY()), false);
            if (path != null && !path.isEmpty()) {
                isMoving = true;
                target = new Vector2D(path.get(0).getX() * Sprite.SCALED_SIZE, path.get(0).getY() * Sprite.SCALED_SIZE);
                currentDir = new Vector2D(path.get(0).getX() - getUnitX(), path.get(0).getY() - getUnitY());
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

        boolean canMoveForward = isWalkable(getUnitX() + currentDir.getX(), getUnitY() + currentDir.getY());
        boolean canMoveLeft = isWalkable(getUnitX() + left.getX(), getUnitY() + left.getY());
        boolean canMoveRight = isWalkable(getUnitX() + right.getX(), getUnitY() + right.getY());
        boolean canMoveBack = isWalkable(getUnitX() + back.getX(), getUnitY() + back.getY());

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

    private Grid generateGrid() {
        boolean[][] temp = new boolean[Constant.HEIGHT][Constant.WIDTH];
        for (int i = 0; i < Constant.HEIGHT; i++) {
            for (int j = 0; j < Constant.WIDTH; j++) {
                if (isWalkable(j, i)) {
                    temp[i][j] = true;
                } else {
                    temp[i][j] = false;
                }
            }
        }
        Grid grid = new Grid(Constant.WIDTH, Constant.HEIGHT, temp);
        return grid;
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

    @Override
    protected void setDeath() {
        isDead = true;

        // change current animation to death animation.
        changeAnimationState(0);
        Audio.playEntityDie();
        Particle particle = new Particle("+" + getPoint(), Duration.millis(1800), getX() + Sprite.SCALED_SIZE / 2, getY(), 18, Color.YELLOW);
    }

    @Override
    public Bounds getBound() {
        if (!isDead) {
            Rectangle rectangle = new Rectangle(x,y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
            return rectangle.getBoundsInParent();
        } else {
            return null;
        }
    }

    /**
     * @param i
     * change animation state based on the value of i.
     * if i == 1 change animation state to forward.
     * if i == 2 change animation state to backward.
     * if i == 0 change animation state to death.
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

    @Override
    protected boolean isWalkable(int x, int y) {
        int[][] map = BombermanGame.game.getStillObjectMap();
        if (map[y][x] == 0 || map[y][x] == 2) {
            if (map[y][x] == 0) {
                for (int i = 1; i <= 3; i++) {
                    if (map[y][x - i] == 4) {
                        return false;
                    }
                    if (map[y][x - i] == 1 || map[y][x - i] == 2) {
                        break;
                    }
                }
                for (int i = 1; i <= 3; i++) {
                    if (map[y][x + i] == 4) {
                        return false;
                    }
                    if (map[y][x + i] == 1 || map[y][x + i] == 2) {
                        break;
                    }
                }
                for (int i = 1; i <= 3; i++) {
                    if (map[y - i][x] == 4) {
                        return false;
                    }
                    if (map[y - i][x] == 1 || map[y - i][x] == 2) {
                        break;
                    }
                }
                for (int i = 1; i <= 3; i++) {
                    if (map[y + i][x] == 4) {
                        return false;
                    }
                    if (map[y + i][x] == 1 || map[y + i][x] == 2) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
