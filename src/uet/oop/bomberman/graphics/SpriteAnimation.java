package uet.oop.bomberman.graphics;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.*;

public class SpriteAnimation extends Transition {
    private Image currentImage;
    private List<Image> sprite;
    private Duration duration;

    public SpriteAnimation(Duration duration, Image[] imageList) {
        this.duration = duration;
        sprite = new ArrayList<>(Arrays.asList(imageList));
        setCycleDuration(duration);
        setCycleCount(Animation.INDEFINITE);
        setInterpolator(Interpolator.LINEAR);
    }

    public void addFrame(Image frame) {
        sprite.add(frame);
    }

    public Image getCurrentImage() {
        return currentImage;
    }

    public List<Image> getSprite() {
        return sprite;
    }

    public void setSprite(ArrayList sprite) {
        this.sprite = sprite;
    }

    @Override
    public void interpolate(double v) {
        int size = sprite.size();
        int index = Math.min((int) (size * v), size - 1);
        currentImage = sprite.get(index);
    }
}
