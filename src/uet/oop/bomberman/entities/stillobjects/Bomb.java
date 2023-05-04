package uet.oop.bomberman.entities.stillobjects;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.particles.Particle;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;
import uet.oop.bomberman.utils.Audio;


public class Bomb extends Entity {

    private final SpriteAnimation animation = new SpriteAnimation(Duration.millis(600), Sprite.bomb_animation);
    private final int bombDuration = 2000;
    private long startTime;

    // because after exploded the bomb will not instantly be removed from the bombs list.
    // so we still need an instance that tell if the bomb was exploded or not.
    private boolean isExploded = false;

    public Bomb(int x, int y, Image image) {
        super(x, y, image);
        startTime = System.currentTimeMillis();
    }

    private void playAnimation() {
        animation.play();
        this.img = animation.getCurrentImage();
    }

    public void explode() {
        isExploded = true;
        // play bomb exploded sound.
        Audio.playBombExplode();

        // clear previous bomb position on the static object map.
        int x = getUnitX();
        int y = getUnitY();
        BombermanGame.game.getStillObjectMap()[y][x] = 0;

        animation.stop();
        // add this element to the garbage list so that it will be deleted from the staticObject list later.
        BombermanGame.game.getGarbageEntities().add(this);

        Entity center = new Explosion(x,y, ExplosionType.CENTER);
        BombermanGame.game.getExplosions().add(center);
        int power = BombermanGame.game.getPlayer().getFlames();
        for (int i = 1; i <= power; i++) {
            if (BombermanGame.game.getStillObjectMap()[y][x - i] == 0 || BombermanGame.game.getStillObjectMap()[y][x - i] == 4) {
                Entity explosion = null;
                if (i == power) {
                    explosion = new Explosion(x - i, y, ExplosionType.HORIZONTAL_LEFT);
                } else {
                    explosion = new Explosion(x - i, y, ExplosionType.HORIZONTAL);
                }
                BombermanGame.game.getExplosions().add(explosion);
            } else {
                if (BombermanGame.game.getStillObjectMap()[y][x - i] == 2) {
                    for (Entity entity : BombermanGame.game.getBricks()) {
                        if (entity.getUnitX() == x - i && entity.getUnitY() == y) {
                            Brick brick = (Brick) entity;
                            brick.setExploded();
                            break;
                        }
                    }
                }
                break;
            }
        }

        for (int i = 1; i <= power; i++) {
            if (BombermanGame.game.getStillObjectMap()[y][x + i] == 0 || BombermanGame.game.getStillObjectMap()[y][x + i] == 4) {
                Entity explosion = null;
                if (i == power) {
                    explosion = new Explosion(x + i, y, ExplosionType.HORIZONTAL_RIGHT);
                } else {
                    explosion = new Explosion(x + i, y, ExplosionType.HORIZONTAL);
                }
                BombermanGame.game.getExplosions().add(explosion);
            } else {
                if (BombermanGame.game.getStillObjectMap()[y][x + i] == 2) {
                    for (Entity entity : BombermanGame.game.getBricks()) {
                        if (entity.getUnitX() == x + i && entity.getUnitY() == y) {
                            Brick brick = (Brick) entity;
                            brick.setExploded();
                            break;
                        }
                    }
                }
                break;
            }
        }

        for (int i = 1; i <= power; i++) {
            if (BombermanGame.game.getStillObjectMap()[y + i][x] == 0 || BombermanGame.game.getStillObjectMap()[y + i][x] == 4) {
                Entity explosion = null;
                if (i == power) {
                    explosion = new Explosion(x, y + i, ExplosionType.VERTICLE_DOWN);
                } else {
                    explosion = new Explosion(x, y + i, ExplosionType.VERTICLE);
                }
                BombermanGame.game.getExplosions().add(explosion);
            } else {
                if (BombermanGame.game.getStillObjectMap()[y + i][x] == 2) {
                    for (Entity entity : BombermanGame.game.getBricks()) {
                        if (entity.getUnitX() == x && entity.getUnitY() == y + i) {
                            Brick brick = (Brick) entity;
                            brick.setExploded();
                            break;
                        }
                    }
                }
                break;
            }
        }

        for (int i = 1; i <= power; i++) {
            if (BombermanGame.game.getStillObjectMap()[y - i][x] == 0 || BombermanGame.game.getStillObjectMap()[y - i][x] == 4) {
                Entity explosion = null;
                if (i == power) {
                    explosion = new Explosion(x, y - i, ExplosionType.VERTICLE_TOP);
                } else {
                    explosion = new Explosion(x, y - i, ExplosionType.VERTICLE);
                }
                BombermanGame.game.getExplosions().add(explosion);
            } else {
                if (BombermanGame.game.getStillObjectMap()[y - i][x] == 2) {
                    for (Entity entity : BombermanGame.game.getBricks()) {
                        if (entity.getUnitX() == x && entity.getUnitY() == y - i) {
                            Brick brick = (Brick) entity;
                            brick.setExploded();
                            break;
                        }
                    }
                }
                break;
            }
        }
    }

    private void collideWithExplosion() {
        for (Entity ex : BombermanGame.game.getExplosions()) {
            if (getBound().intersects(ex.getBound())) {
                explode();
                break;
            }
        }
    }

    @Override
    public void update() {
        playAnimation();
        long elapsedTime = System.currentTimeMillis() - startTime;
        if (elapsedTime > bombDuration) {
            explode();
        }
        collideWithExplosion();
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x + 8, y + 8, Sprite.SCALED_SIZE - 8, Sprite.SCALED_SIZE - 8);
        return rectangle.getBoundsInParent();
    }

    public boolean isExploded() {
        return isExploded;
    }
}
