package uet.oop.bomberman.entities;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.items.ItemType;
import uet.oop.bomberman.entities.particles.Particle;
import uet.oop.bomberman.entities.stillobjects.Bomb;
import uet.oop.bomberman.entities.stillobjects.Portal;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;
import uet.oop.bomberman.utils.Audio;

import java.util.List;

public class Bomber extends Entity {

    private boolean bombPass = false;
    private boolean wallPass = false;
    private boolean flamePass = false;
    private boolean detonation = false;
    private boolean canDetonate = false;
    private int flames = 1;
    private int speed = 2;
    private int bombs = 1;

    private int availableBomb = bombs;

    private boolean isDead = false;

    private final SpriteAnimation left_animation = new SpriteAnimation(Duration.millis(200), Sprite.player_go_left_animation);
    private final SpriteAnimation right_animation = new SpriteAnimation(Duration.millis(200), Sprite.player_go_right_animation);
    private final SpriteAnimation up_animation = new SpriteAnimation(Duration.millis(200), Sprite.player_go_up_animation);
    private final SpriteAnimation down_animation = new SpriteAnimation(Duration.millis(200), Sprite.player_go_down_animation);
    private final SpriteAnimation death_animation = new SpriteAnimation(Duration.millis(500), Sprite.player_death_animation);

    /** represent the current animation, set to right_animation by default. */
    private SpriteAnimation currentAnimation = right_animation;

    /** time between the 2 closest setbomb action. */
    private final int coolDownTime = 500;

    public Bomber(int x, int y) {
        super( x, y, Sprite.player_down.getFxImage());
    }

    @Override
    public void update() {
        if (!isDead) {
            if (BombermanGame.input.isPressed(KeyCode.B)) {
                if (detonation && canDetonate) {
                    if (!BombermanGame.game.getBombs().isEmpty()) {
                        Bomb bomb = (Bomb) BombermanGame.game.getBombs().get(BombermanGame.game.getBombs().size() - 1);
                        if (!bomb.isExploded()) {
                            bomb.explode();
                            canDetonate = false;
                        }
                    }
                }
            }
            if (BombermanGame.input.isPressed(KeyCode.SPACE)) {
                setBomb();
            }
            if (BombermanGame.input.isPressed(KeyCode.W)) {
                moveY(-speed);
                changeAnimationSate(1);
                powerUp();
                collideWithCollisionOrEnemy();
                return;
            } else if (BombermanGame.input.isPressed(KeyCode.D)) {
                moveX(speed);
                changeAnimationSate(2);
                powerUp();
                collideWithCollisionOrEnemy();
                return;
            } else if (BombermanGame.input.isPressed(KeyCode.S)) {
                moveY(speed);
                changeAnimationSate(3);
                powerUp();
                collideWithCollisionOrEnemy();
                return;
            } else if (BombermanGame.input.isPressed(KeyCode.A)) {
                moveX(-speed);
                changeAnimationSate(4);
                powerUp();
                collideWithCollisionOrEnemy();
                return;
            }
            stopAnimation();
            powerUp();
            collideWithCollisionOrEnemy();
        } else {
            // if the bomber is dead then the death animation is now performed, so just take the current image and do nothing more.
            this.img = currentAnimation.getCurrentImage();
        }
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x, y + Sprite.SCALED_SIZE / 3,
                Sprite.SCALED_SIZE - 10, Sprite.SCALED_SIZE * 2 / 3);
        return rectangle.getBoundsInParent();
    }

    /**a
     * change animation state based on type.
     * @param type
     * if type == 1 change animation to go up.
     * if type == 2 change animation to go right.
     * if type == 3 change animation to go down.
     * if type == 4 change animation to go left.
     * if type == 0 change animation to death.
     */
    private void changeAnimationSate(int type) {
        switch (type) {
            case 1:
                if (currentAnimation != up_animation) {
                    currentAnimation.stop();
                    currentAnimation = up_animation;
                }
                playAnimation();
                break;
            case 2:
                if (currentAnimation != right_animation) {
                    currentAnimation.stop();
                    currentAnimation = right_animation;
                }
                playAnimation();
                break;
            case 3:
                if (currentAnimation != down_animation) {
                    currentAnimation.stop();
                    currentAnimation = down_animation;
                }
                playAnimation();
                break;
            case 4:
                if (currentAnimation != left_animation) {
                    currentAnimation.stop();
                    currentAnimation = left_animation;
                }
                playAnimation();
                break;
            case 0:
                if (currentAnimation != death_animation) {
                    currentAnimation.stop();
                    currentAnimation = death_animation;
                    currentAnimation.setCycleCount(1);
                    currentAnimation.setOnFinished(e -> {
                        BombermanGame.game.gameOver("You died!!");
                    });
                    currentAnimation.play();
                }
        }
    }

    private void playAnimation() {
        currentAnimation.play();
        this.img = currentAnimation.getCurrentImage();
    }

    private void stopAnimation() {
        currentAnimation.stop();
        this.img = currentAnimation.getSprite().get(0);
    }

    private void setBomb() {
        if (availableBomb > 0) {
            int unitX = (getX() + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int unitY = (getY() + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            if (canPutBomb(unitX, unitY)) {
                availableBomb--;
                Bomb bomb = new Bomb(unitX, unitY,Sprite.bomb.getFxImage());
                BombermanGame.game.getBombs().add(bomb);
                BombermanGame.game.getStillObjectMap()[unitY][unitX] = 4;
                Audio.playBombDrop();

                if (detonation) {
                    canDetonate = true;
                }
            }
            if (availableBomb == 0) {
                Animation timer = new Transition() {
                    {
                        setCycleDuration(Duration.millis(coolDownTime));
                        setCycleCount(1);
                        setOnFinished(e ->{
                            availableBomb = bombs;
                        });
                    }
                    @Override
                    protected void interpolate(double v) {
                        // do nothing.
                    }
                };
                timer.play();
            }
        }
    }
    private boolean canPutBomb(int unitX, int unitY) {
        return BombermanGame.game.getStillObjectMap()[unitY][unitX] == 0;
    }
    private boolean collideInXWith(List<Entity> entities, boolean moveRight) {
        for (Entity entity : entities) {
            if (getBound().intersects(entity.getBound())) {
                double playerX = getBound().getMinX();
                double playerY = getBound().getMinY();
                double playerW = getBound().getWidth();
                double playerH = getBound().getHeight();
                double entityX = entity.getBound().getMinX();
                double entityY = entity.getBound().getMinY();
                double entityW = entity.getBound().getWidth();
                double entityH = entity.getBound().getHeight();
                if (moveRight) {
                    if ((playerX + playerW == entityX) && (playerY > entityY - playerH) && (playerY < entityY + entityH)) {
                        return true;
                    }
                } else {
                    if ((playerX == entityX +  entityW) && (playerY > entityY - playerH) && (playerY < entityY + entityH)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean collideInXWith(Entity entity, boolean moveRight) {
        if (getBound().intersects(entity.getBound())) {
            double playerX = getBound().getMinX();
            double playerY = getBound().getMinY();
            double playerW = getBound().getWidth();
            double playerH = getBound().getHeight();
            double entityX = entity.getBound().getMinX();
            double entityY = entity.getBound().getMinY();
            double entityW = entity.getBound().getWidth();
            double entityH = entity.getBound().getHeight();
            if (moveRight) {
                if ((playerX + playerW == entityX) && (playerY > entityY - playerH) && (playerY < entityY + entityH)) {
                    return true;
                }
            } else {
                if ((playerX == entityX +  entityW) && (playerY > entityY - playerH) && (playerY < entityY + entityH)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean collideInYWith(List<Entity> entities, boolean moveDown) {
        for (Entity entity : entities) {
            if (getBound().intersects(entity.getBound())) {
                double playerX = getBound().getMinX();
                double playerY = getBound().getMinY();
                double playerW = getBound().getWidth();
                double playerH = getBound().getHeight();
                double entityX = entity.getBound().getMinX();
                double entityY = entity.getBound().getMinY();
                double entityW = entity.getBound().getWidth();
                double entityH = entity.getBound().getHeight();
                if (moveDown) {
                    if ((playerY + playerH == entityY) && (playerX > entityX - playerW) && (playerX < entityX + entityW)) {
                        return true;
                    }
                } else {
                    if ((playerY == entityY + entityH) && (playerX > entityX - playerW) && (playerX < entityX + entityW)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean collideInYWith(Entity entity, boolean moveDown) {
        if (getBound().intersects(entity.getBound())) {
            double playerX = getBound().getMinX();
            double playerY = getBound().getMinY();
            double playerW = getBound().getWidth();
            double playerH = getBound().getHeight();
            double entityX = entity.getBound().getMinX();
            double entityY = entity.getBound().getMinY();
            double entityW = entity.getBound().getWidth();
            double entityH = entity.getBound().getHeight();
            if (moveDown) {
                if ((playerY + playerH == entityY) && (playerX > entityX - playerW) && (playerX < entityX + entityW)) {
                    return true;
                }
            } else {
                if ((playerY == entityY + entityH) && (playerX > entityX - playerW) && (playerX < entityX + entityW)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveX(int val) {
        boolean moveRight = val > 0;
        for (int i = 1; i <= Math.abs(val); i++) {
            if (!wallPass) {
                if (collideInXWith(BombermanGame.game.getBricks(), moveRight)) {
                    return;
                }
            }
            if (!bombPass) {
                if (collideInXWith(BombermanGame.game.getBombs(), moveRight)) {
                    return;
                }
            }
           for (Entity por : BombermanGame.game.getPortals()) {
               Portal portal = (Portal) por;
               if (!portal.isOpen()) {
                   if (collideInXWith(portal, moveRight)) {
                       return;
                   }
               }
           }
            if (collideInXWith(BombermanGame.game.getWalls(), moveRight)) {
                return;
            }
            x += (moveRight ? 1 : -1);
        }
    }

    public void moveY(int val) {
        boolean moveDown = val > 0;
        for (int i = 0; i < Math.abs(val); i++) {
            if (!wallPass) {
                if (collideInYWith(BombermanGame.game.getBricks(), moveDown)) {
                    return;
                }
            }
            if (!bombPass) {
                if (collideInYWith(BombermanGame.game.getBombs(), moveDown)) {
                    return;
                }
            }
            for (Entity por : BombermanGame.game.getPortals()) {
                Portal portal = (Portal) por;
                if (!portal.isOpen()) {
                    if (collideInYWith(portal, moveDown)) {
                        return;
                    }
                }
            }
            if (collideInYWith(BombermanGame.game.getWalls(), moveDown)) {
                return;
            }

            y += (moveDown ? 1 : -1);
        }
    }

    private void powerUp() {
        for (Entity entity : BombermanGame.game.getItems()) {
            if (getBound().intersects(entity.getBound())) {
                Item item = (Item) entity;
                String message = "";
                if (item.getItemType() == ItemType.BOMBS) {
                    bombs++;
                    message = "+1 bomb";
                }
                if (item.getItemType() == ItemType.FLAMES) {
                    flames++;
                    message = "+1 flame";
                }
                if (item.getItemType() == ItemType.SPEED) {
                    speed++;
                    message = "+1 speed";
                }
                if (item.getItemType() == ItemType.WALLPASS) {
                    wallPass = true;
                    message = "Wall pass!!";
                }
                if (item.getItemType() == ItemType.BOMBPASS) {
                    bombPass = true;
                    message = "Bomb pass!!";
                }
                if (item.getItemType() == ItemType.FLAMEPASS) {
                    flamePass = true;
                    message = "Flame pass!!";
                }
                if (item.getItemType() == ItemType.DETONATOR) {
                    detonation = true;
                    message = "Detonator!!";
                }
                item.setDeath();
                Particle particle = new Particle(message, Duration.millis(1500), item.getX() + Sprite.SCALED_SIZE / 2, item.getY(),18, Color.rgb(252, 182, 3));
                Audio.takeItem();
                break;
            }
        }
    }

    private void collideWithCollisionOrEnemy() {
        if(!flamePass) {
            for (Entity entity : BombermanGame.game.getExplosions()) {
                if (getBound().intersects(entity.getBound())) {
                    setDead();
                    return;
                }
            }
        }
        for (Entity entity : BombermanGame.game.getEnemies()) {
            if (getBound().intersects(entity.getBound())) {
                setDead();
                return;
            }
        }
    }

    private void setDead() {
        isDead = true;
        changeAnimationSate(0);
        Audio.bomberDie();
    }

    public int getFlames() {
        return flames;
    }

}
