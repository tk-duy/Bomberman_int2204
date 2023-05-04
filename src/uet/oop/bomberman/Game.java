package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.util.Duration;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.entities.enemy.Ballom;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.enemy.Oneal;
import uet.oop.bomberman.entities.items.Item;
import uet.oop.bomberman.entities.particles.LevelTransition;
import uet.oop.bomberman.entities.stillobjects.*;
import uet.oop.bomberman.graphics.LevelData;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.utils.Audio;

import java.util.ArrayList;
import java.util.List;

public class Game extends AnimationTimer {
    private int level;
    private int timeLeft;
    private int score;

    // use to calculate elapsed time.
    private long lastTimeCalculate;

    private Bomber player = null;
    private final int[][] stillObjectMap = new int[15][25];
    private final List<Entity> portals = new ArrayList<>(1);
    private final List<Entity> bombs = new ArrayList<>();
    private final List<Entity> bricks = new ArrayList<>();
    private final List<Entity> walls = new ArrayList<>();
    private final List<Entity> items = new ArrayList<>();
    private final List<Entity> explosions = new ArrayList<>();
    private final List<Entity> enemies = new ArrayList<>();
    private final List<Entity> garbageEntities = new ArrayList<>();

    @Override
    public void handle(long l) {
        update();
        render();
    }

    public void update() {
        if (player != null) {
            player.update();
        }
        if (!portals.isEmpty()) {
            portals.get(0).update();
        }
        explosions.forEach(Entity::update);
        bombs.forEach(Entity::update);
        bricks.forEach(Entity::update);
        walls.forEach(Entity::update);
        items.forEach(Entity::update);
        enemies.forEach(Entity::update);
        clearGarbageEntity();

        // update time.
        long elapsedTime = System.currentTimeMillis() - lastTimeCalculate;
        timeLeft -= elapsedTime;
        if (timeLeft <= 0) {
            timeLeft = 0;
        }
        lastTimeCalculate = System.currentTimeMillis();

        // display time, score, and level in the UIRoot.
        BombermanGame.timeLeft.setText("TIME : " + String.valueOf(timeLeft / 1000));
        BombermanGame.level.setText("LEVEL : " + String.valueOf(level));
        BombermanGame.score.setText(String.valueOf(score));

        if (timeLeft == 0) {
            gameOver("Time out!!!");
            Audio.bomberDie();
        }
    }

    /** clear the garbage entity list. */
    public void clearGarbageEntity() {
        for (Entity entity : garbageEntities) {
            if (entity instanceof Bomb) {
                bombs.remove(entity);
            }
            if (entity instanceof Brick) {
                bricks.remove(entity);
            }
            if (entity instanceof Wall) {
                walls.remove(entity);
            }
            if (entity instanceof Item) {
                items.remove(entity);
            }
            if (entity instanceof Explosion) {
                explosions.remove(entity);
            }
            if (entity instanceof Enemy) {
                enemies.remove(entity);
            }
        }
    }

    public void render() {
        BombermanGame.gc.clearRect(0, 0, Constant.WIDTH * Sprite.SCALED_SIZE, Constant.HEIGHT * Sprite.SCALED_SIZE);

        // background image.
        BombermanGame.gc.drawImage(Sprite.getImg(new Image("C:\\Users\\ADMIN\\IdeaProjects\\classic-bomberman-master\\res\\sprites\\Ground.png")), 0, 0, Sprite.SCALED_SIZE * Constant.WIDTH, Sprite.SCALED_SIZE * Constant.HEIGHT);

        bricks.forEach(g -> g.render(BombermanGame.gc));
        walls.forEach(g -> g.render(BombermanGame.gc));
        if (!portals.isEmpty()) {
            portals.get(0).render(BombermanGame.gc);
        }
        explosions.forEach(g -> g.render(BombermanGame.gc));
        items.forEach(g -> g.render(BombermanGame.gc));
        bombs.forEach(g -> g.render(BombermanGame.gc));
        if (player != null) {
            player.render(BombermanGame.gc);
        }
        enemies.forEach(g -> g.render(BombermanGame.gc));
    }

    public void addPoint(int point) {
        this.score += point;
    }
    public void playLevel(int level) {
        // stop the game music if it's currently playing.
        Audio.stopMusic(Audio.gameMusic);

        this.level = level;
        stop();

        if (level > Constant.TOTAL_LEVEL) {
            Audio.playVictory();
            gameOver("You win!!!");
            return;
        }

        LevelTransition levelTransition = new LevelTransition(level, Duration.millis(3000));
        levelTransition.setOnFinished(e -> {
        levelTransition.selfDestroy();
        lastTimeCalculate = System.currentTimeMillis();
        start();
        // start playing the game music.
        Audio.playGameMusic();
        });
        Audio.playVictory();
        timeLeft = Constant.TIME;
        setupLevel(level);
    }

    public void setupLevel (int level) {
        // clear the previous content.
        portals.clear();
        bombs.clear();
        bricks.clear();
        walls.clear();
        enemies.clear();
        items.clear();
        explosions.clear();
        garbageEntities.clear();

        int[][] levelData = null;
        switch (level) {
            case 1:
                levelData = LevelData.LEVEL_1;
                break;
            case 2:
                levelData = LevelData.LEVEL_2;
                break;

        }
        for (int i = 0; i < Constant.HEIGHT; i++) {
            for (int j = 0; j < Constant.WIDTH; j++) {
                switch (levelData[i][j]) {
                    case 0:
                        stillObjectMap[i][j] = 0;
                        break;
                    case 1:
                        stillObjectMap[i][j] = 1;
                        Entity wall = new Wall(j, i, Sprite.getImg(new Image("C:\\Users\\ADMIN\\IdeaProjects\\classic-bomberman-master\\res\\sprites\\Block.png")));
                        walls.add(wall);
                        break;
                    case 2:
                        stillObjectMap[i][j] = 2;
                        Entity brick = new Brick(j, i, BrickType.NORMAL);
                        bricks.add(brick);
                        break;
                    case 5:
                        if (player == null) {
                            player = new Bomber(j, i);
                        } else {
                            player.setX(j * Sprite.SCALED_SIZE);
                            player.setY(i * Sprite.SCALED_SIZE);
                        }
                        break;
                    case 6:
                        Entity ballom = new Ballom(j,i,Sprite.balloon_left3.getFxImage());
                        enemies.add(ballom);
                        break;
                    case 7:
                        Entity oneal = new Oneal(j, i,Sprite.oneal_right1.getFxImage());
                        enemies.add(oneal);
                        break;
                    case 11:
                        stillObjectMap[i][j] = 2;
                        Brick portal_brick = new Brick(j, i, BrickType.PORTAL);
                        bricks.add(portal_brick);
                        break;
                    case -1:
                        stillObjectMap[i][j] = 2;
                        Brick bomb_brick = new Brick(j, i, BrickType.BOMBS_BRICK);
                        bricks.add(bomb_brick);
                        break;
                    case -2:
                        stillObjectMap[i][j] = 2;
                        Brick speed_brick = new Brick(j, i, BrickType.SPEED_BRICK);
                        bricks.add(speed_brick);
                        break;
                    case -3:
                        stillObjectMap[i][j] = 2;
                        Brick flames_brick = new Brick(j, i,BrickType.FLAMES_BRICK);
                        bricks.add(flames_brick);
                        break;
                    case -4:
                        stillObjectMap[i][j] = 2;
                        Brick wallpass = new Brick(j,i,BrickType.WALLPASS_BRICK);
                        bricks.add(wallpass);
                        break;
                    case -5:
                        stillObjectMap[i][j] = 2;
                        Brick bombpass = new Brick(j, i, BrickType.BOMBPASS_BRICK);
                        bricks.add(bombpass);
                        break;
                    case -6:
                        stillObjectMap[i][j] = 2;
                        Brick flamepass = new Brick(j, i,BrickType.FLAMEPASS_BRICK);
                        bricks.add(flamepass);
                        break;
                    case -7:
                        stillObjectMap[i][j] = 2;
                        Brick detonator = new Brick(j, i,BrickType.DETONATOR_BRICK);
                        bricks.add(detonator);
                        break;
                }
            }
        }
    }

    public void gameOver(String message) {
        // stop the game music if it's currently playing.
        Audio.stopMusic(Audio.gameMusic);

        stop();
        boolean highScore = score > BombermanGame.getHighScore();
        if (highScore) {
            BombermanGame.saveScore(score);
        }
        BombermanGame.UIRoot.getChildren().add(BombermanGame.createEndPane(score, message, highScore));
    }

    /** getter and setter. */
    public Bomber getPlayer() {
        return player;
    }

    public List<Entity> getPortals() {
        return portals;
    }

    public int[][] getStillObjectMap() {
        return stillObjectMap;
    }

    public List<Entity> getBombs() {
        return bombs;
    }

    public List<Entity> getBricks() {
        return bricks;
    }

    public List<Entity> getWalls() {
        return walls;
    }

    public List<Entity> getItems() {
        return items;
    }

    public List<Entity> getEnemies() {
        return enemies;
    }

    public List<Entity> getExplosions() {
        return explosions;
    }

    public List<Entity> getGarbageEntities() {
        return garbageEntities;
    }

    public int getLevel() {
        return level;
    }

}
