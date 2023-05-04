package uet.oop.bomberman.entities.particles;

import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.Constant;
import uet.oop.bomberman.graphics.Sprite;


public class LevelTransition extends Transition {
    private Pane board;

    public LevelTransition (int level, Duration duration) {
        // create font for the text.
        Font font = Font.loadFont(getClass().getResourceAsStream("/textures/upheavtt.ttf"), 30);
        // create text.
        Text text = new Text();
        text.setText("LEVEL " + String.valueOf(level));
        text.setFont(font);
        text.setFill(Color.WHITE);
        text.setX(Constant.WIDTH * Sprite.SCALED_SIZE / 2 -  50);
        text.setY(Constant.HEIGHT * Sprite.SCALED_SIZE / 2 - 20);

        // create the black board.
        board = new Pane();
        board.setStyle("-fx-background-color: black;");
        board.setPrefSize(Constant.WIDTH * Sprite.SCALED_SIZE, Constant.HEIGHT * Sprite.SCALED_SIZE + Constant.BAR_HEIGHT);

        board.getChildren().add(text);

        BombermanGame.UIRoot.getChildren().add(board);

        // set properties of the transition.
        setCycleDuration(duration);
        setCycleCount(1);
        play();
    }

    @Override
    protected void interpolate(double v) {
        // do nothing.
    }

    public void selfDestroy() {
        BombermanGame.UIRoot.getChildren().remove(board);
    }

}
