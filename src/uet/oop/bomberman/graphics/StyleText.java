package uet.oop.bomberman.graphics;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class StyleText extends Text {

    public StyleText(int fontSize) {
        Font font = Font.loadFont(getClass().getResourceAsStream("/textures/upheavtt.ttf"), fontSize);
        setFont(font);
        setFill(Color.WHITE);
    }
}
