package uet.oop.bomberman;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private Stage stage;

    public void changeScene(Scene scene) {
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
