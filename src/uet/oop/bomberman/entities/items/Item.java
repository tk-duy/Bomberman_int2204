package uet.oop.bomberman.entities.items;

import javafx.geometry.Bounds;
import javafx.scene.shape.Rectangle;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Item extends Entity {
    private ItemType itemType;

    public Item(int x, int y, ItemType itemType) {
        super(x, y);
        this.itemType = itemType;
        switch (itemType) {
            case BOMBS:
                this.img = Sprite.powerup_bombs.getFxImage();
                break;
            case FLAMES:
                this.img = Sprite.powerup_flames.getFxImage();
                break;
            case SPEED:
                this.img = Sprite.powerup_speed.getFxImage();
                break;
            case WALLPASS:
                this.img = Sprite.powerup_wallpass.getFxImage();
                break;
            case BOMBPASS:
                this.img =Sprite.powerup_bombpass.getFxImage();
                break;
            case FLAMEPASS:
                this.img = Sprite.powerup_flamepass.getFxImage();
                break;
            case DETONATOR:
                this.img = Sprite.powerup_detonator.getFxImage();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.itemType);
        }
    }

    @Override
    public void update() {
        // do nothing.
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setDeath() {
        BombermanGame.game.getGarbageEntities().add(this);
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x, y, Sprite.SCALED_SIZE, Sprite.SCALED_SIZE);
        return rectangle.getBoundsInParent();
    }
}
