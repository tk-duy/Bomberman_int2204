package uet.oop.bomberman.entities.particles;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.StyleText;

public class Particle extends Transition {
    private StyleText text;

    public Particle(String text, Duration duration, int x, int y, int fontSize, Color color) {
        this.text = new StyleText(fontSize);

        this.text.setText(text);
        this.text.setFill(color);
        this.text.setX(x);
        this.text.setY(y);

        // set properties of the transition.
        setCycleCount(1);
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);

        // add this text node to the particle layer in BombermanGame class.
        BombermanGame.particleLayer.getChildren().add(this.text);

        // remove this text node from the particle layer after this animation has ended.
        setOnFinished(e -> {
            BombermanGame.particleLayer.getChildren().remove(this.text);
        });
        play();
    }
    @Override
    protected void interpolate(double v) {
        text.setY(text.getY() - (2 * (float) v));
    }
}
