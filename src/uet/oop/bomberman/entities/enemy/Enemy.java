package uet.oop.bomberman.entities.enemy;

import javafx.scene.image.Image;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.utils.Vector2D;

public abstract class Enemy extends Entity {

    protected int point;
    protected int velocity;
    protected boolean isMoving = false;
    protected Vector2D target = null;
    protected boolean isDead = false;

    public Enemy(int x, int y, Image image, int velocity, int point) {
        super(x, y, image);
        this.velocity = velocity;
        this.point = point;
    }

    protected int getVelocity() {
        return velocity;
    }

    protected int getPoint() { return point; }

    protected abstract void findTarget();

    protected void move(Vector2D target) {
        if (target != null) {
            boolean movingRight = (target.getX() - getX()) > 0;
            boolean movingUp = (target.getY() - getY()) < 0;
            for (int i = 1; i <= velocity; i++) {
                if (x != target.getX()) {
                    x += (movingRight ? 1 : -1);
                }
                if (y != target.getY()) {
                    y += (movingUp ? -1 : 1);
                }
            }
            if (x == target.getX() && y == target.getY()) {
                isMoving = false;
            }
        }
    }

    protected abstract void setDeath();

    protected void collideWithExplosion() {
        for (Entity explosion : BombermanGame.game.getExplosions()) {
            if (getBound().intersects(explosion.getBound())) {
                setDeath();
                BombermanGame.game.addPoint(this.getPoint());
                break;
            }
        }
    }

    /** this method determine if a tile in the map is walkable or not.
     * this differs to each enemy so each enemy must implement it's owm.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return if a tile locates in x, y is walkable to this enemy or not.
     */
    protected abstract boolean isWalkable(int x, int y);
}
