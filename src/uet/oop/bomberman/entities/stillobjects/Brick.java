package uet.oop.bomberman.entities.stillobjects;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.items.ItemType;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.graphics.SpriteAnimation;

public class Brick extends Entity {
    private SpriteAnimation exploded_animation = new SpriteAnimation(Duration.millis(300), Sprite.brick_exploded_animation);
    private boolean alive = true;
    private BrickType type;

    public Brick(int x, int y, BrickType type) {
        super(x, y, Sprite.getImg(new Image("C:\\Users\\ADMIN\\IdeaProjects\\classic-bomberman-master\\res\\sprites\\Brick (1).png")));
        this.type = type;
    }

    public void setExploded() {
        alive = false;
        exploded_animation.setCycleCount(1);
        exploded_animation.play();
        exploded_animation.setOnFinished(e -> {
            BombermanGame.game.getStillObjectMap()[getUnitY()][getUnitX()] = 0;
            BombermanGame.game.getGarbageEntities().add(this);
            instantiateItem();
        });
    }

    private void instantiateItem() {
        switch (this.type) {
            case NORMAL:
                break;
            case BOMBS_BRICK:
                Item bomb = new Item(getUnitX(), getUnitY(), ItemType.BOMBS);
                BombermanGame.game.getItems().add(bomb);
                break;
            case SPEED_BRICK:
                Item speed = new Item(getUnitX(), getUnitY(), ItemType.SPEED);
                BombermanGame.game.getItems().add(speed);
                break;
            case FLAMES_BRICK:
                Item flames = new Item(getUnitX(), getUnitY(), ItemType.FLAMES);
                BombermanGame.game.getItems().add(flames);
                break;
            case BOMBPASS_BRICK:
                Item bombpass = new Item(getUnitX(), getUnitY(), ItemType.BOMBPASS);
                BombermanGame.game.getItems().add(bombpass);
                break;
            case WALLPASS_BRICK:
                Item wallpass = new Item(getUnitX(), getUnitY(), ItemType.WALLPASS);
                BombermanGame.game.getItems().add(wallpass);
                break;
            case FLAMEPASS_BRICK:
                Item flamepass = new Item(getUnitX(),getUnitY(), ItemType.FLAMEPASS);
                BombermanGame.game.getItems().add(flamepass);
                break;
            case DETONATOR_BRICK:
                Item detonator = new Item(getUnitX(), getUnitY(), ItemType.DETONATOR);
                BombermanGame.game.getItems().add(detonator);
                break;
            case PORTAL:
                BombermanGame.game.getStillObjectMap()[getUnitY()][getUnitX()] = 11;
                Entity portal = new Portal(getUnitX(), getUnitY());
                BombermanGame.game.getPortals().add(portal);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + this.type);
        }
    }
    @Override
    public void update() {
        if (!alive) {
            this.img = exploded_animation.getCurrentImage();
        }
    }

    @Override
    public Bounds getBound() {
        Rectangle rectangle = new Rectangle(x, y, Sprite.SCALED_SIZE,Sprite.SCALED_SIZE);
        return rectangle.getBoundsInParent();
    }
}
