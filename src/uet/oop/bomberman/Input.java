package uet.oop.bomberman;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class Input {
    private Map<KeyCode, Boolean> keys;

    public Input() {
        keys = new HashMap<>();
    }

    public void updateKeys(KeyEvent event, Boolean b) {
        keys.put(event.getCode(), b);
    }

    public Boolean isPressed(KeyCode key) {
        return keys.getOrDefault(key, false);
    }
}
