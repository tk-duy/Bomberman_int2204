package uet.oop.bomberman;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uet.oop.bomberman.graphics.*;
import uet.oop.bomberman.utils.Audio;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BombermanGame extends Application {
    public static SceneManager sceneManager = new SceneManager();
    public static Input input;

    public static Game game;


    public static GraphicsContext gc;
    // contains all text particle effect.
    public static Group particleLayer;
    public static Group UIRoot;

    public static StyleText timeLeft;
    public static StyleText score;
    public static StyleText level;

    public static Scene createGameScene() {
        Canvas canvas = new Canvas(Sprite.SCALED_SIZE * Constant.WIDTH, Sprite.SCALED_SIZE * Constant.HEIGHT);
        gc = canvas.getGraphicsContext2D();

        particleLayer = new Group();

        UIRoot = new Group();
        Pane pane = new Pane();
        pane.setPrefSize(Constant.BAR_WIDTH, Constant.BAR_HEIGHT);
        pane.setStyle("-fx-background-color: #000000;");

        timeLeft = new StyleText(15);
        score = new StyleText(15);
        level = new StyleText(15);

        pane.getChildren().addAll(timeLeft, score, level);
        timeLeft.setTranslateX(50);
        timeLeft.setTranslateY(20);

        score.setTranslateX(375);
        score.setTranslateY(20);

        level.setTranslateX(650);
        level.setTranslateY(20);

        UIRoot.getChildren().add(pane);

        Group gameRoot = new Group();
        gameRoot.getChildren().addAll(canvas, particleLayer);
        gameRoot.setTranslateX(0);
        gameRoot.setTranslateY(Constant.BAR_HEIGHT);

        Group container = new Group();
        container.getChildren().addAll(gameRoot, UIRoot);

        Scene scene = new Scene(container);
        scene.setOnKeyPressed(e -> {
            input.updateKeys(e, true);
        });
        scene.setOnKeyReleased(e -> {
            input.updateKeys(e, false);
        });
        return scene;
    }

    public static Pane createEndPane(int score, String message, boolean highScore) {
        Pane pane = new Pane();
        pane.setPrefSize(Constant.WIDTH_PIXEL, Constant.HEIGHT_PIXEL);
        pane.setStyle("-fx-background-color: rgba(230, 240, 240, 0.5);");

        VBox messageBox = new VBox();
        messageBox.setTranslateX(Constant.WIDTH_PIXEL / 2 - 200);
        messageBox.setTranslateY(100);
        messageBox.setPrefSize(400, 100);
        messageBox.setAlignment(Pos.CENTER);

        StyleText text1 = new StyleText(40);
        text1.setText(message);
        text1.setFill(Color.rgb(0, 0, 0));
        StyleText text2 = new StyleText(30);
        if (highScore) {
            text2.setText("High score: " + score);
        } else {
            text2.setText("Your score: " + score);
        }
        text2.setFill(Color.rgb(0, 0, 0));

        messageBox.getChildren().addAll(text1, text2);


        VBox menuBox = new VBox();
        menuBox.setTranslateX(Constant.WIDTH_PIXEL / 2 - 125);
        menuBox.setTranslateY(300);

        EndGameMenuButton playAgain = new EndGameMenuButton("PLAY AGAIN");
        EndGameMenuButton exit = new EndGameMenuButton("EXIT");

        exit.setOnMouseClicked(e -> {
            Audio.playMenuMove();
            Platform.exit();
            System.exit(0);
        });
        // play again behavior.
        playAgain.setOnMouseClicked(e -> {
            Audio.playMenuMove();
            startGame();
        });

        menuBox.getChildren().addAll(playAgain, exit);
        pane.getChildren().addAll(messageBox, menuBox);

        return pane;
    }

    public static void startGame() {
        sceneManager.changeScene(createGameScene());
        game = new Game();
        input = new Input();
        game.playLevel(1);
    }
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> {
            Platform.exit();
            System.exit(0);
        });
        stage.setTitle("Bomberman");
        sceneManager.setStage(stage);
        sceneManager.changeScene(createGameScene());
        startGame();
    }

    public static int getHighScore() {
        try {
            FileReader reader = new FileReader("res/highScore.txt");
            BufferedReader bufferedReader =  new BufferedReader(reader);
            String line = bufferedReader.readLine();
            if (line == null) {
                reader.close();
                return 0;
            } else {
                reader.close();
                return Integer.valueOf(line);
            }
        } catch (IOException e) {
            System.out.println("something went wrong when try to open high score file");
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveScore(int score) {
        try {
            FileWriter writer = new FileWriter("res/highScore.txt");
            writer.write(String.valueOf(score));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("something went wrong when try to save score");
        }
    }
}
