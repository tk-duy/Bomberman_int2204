package uet.oop.bomberman.entities.stillobjects;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends Entity {
    private boolean isOpen = false;
    public Portal(int x, int y) {
        super(x, y, Sprite.portal.getFxImage());
    }

    @Override
    public void update() {
        // if the enemy at the current level is 0 then open the portal that allow player to go to the next level.
        if (BombermanGame.game.getEnemies().isEmpty()) {
            isOpen = true;
        }
        goToNextLevel();
    }

    // if the player and the portal are close enough and the portal is open then go to the next level.
    private void goToNextLevel() {
        if (isOpen() && Math.abs(getX() - BombermanGame.game.getPlayer().getX()) <= 6 && Math.abs(getY() - BombermanGame.game.getPlayer().getY()) <= 6) {
            BombermanGame.game.playLevel(BombermanGame.game.getLevel() + 1);
        }
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x, y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        return rectangle.getBoundsInParent();
    }

    public boolean isOpen() {
        return this.isOpen;
    }
}
