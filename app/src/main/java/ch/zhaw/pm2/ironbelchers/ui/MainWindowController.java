package ch.zhaw.pm2.ironbelchers.ui;

import ch.zhaw.pm2.ironbelchers.classloader.SystemManager;
import ch.zhaw.pm2.ironbelchers.game.*;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainWindowController {
    @FXML
    public Text errorText;
    @FXML
    private AnchorPane anchorPane;


    private final List<KeyCode> pressedKeys = new ArrayList<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private GameController gameController;

    public void setup(GameController gameController) {
        this.gameController = gameController;
        setupView();
        anchorPane.getChildren().addAll(gameController.getGameObjects());
        collisionTimer.start();
    }

    private void setupView() {
        List<GameObject> gameObjects = gameController.getGameObjects();
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof Ship) {
                Image img = new Image("boat.png");
                gameObject.setFill(new ImagePattern(img));
            } else if (gameObject instanceof Island) {
                Image img = new Image("island.png");
                gameObject.setFill(new ImagePattern(img));
            }
        }
    }


    AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long timeStamp) {
            moveShips();
            if (gameController.shipsCrashed() || gameController.shipSunk()) {
                deathScreen();
            }
        }

        private void deathScreen() {
            collisionTimer.stop();
            executor.shutdown();
            gameController.shutdownAllExecutors();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/DeathScreen.fxml"));
            try {
                Pane rootPane = loader.load();
                Scene scene = new Scene(rootPane);
                DeathScreenController controller = loader.getController();
                controller.displayWinner(gameController.getWinnerMsg());
                Stage window = (Stage) anchorPane.getScene().getWindow();

                window.setScene(scene);
                window.show();
            } catch (IOException e) {
                errorText.setVisible(true);
                errorText.setText("Error: Could not display the end screen");
            }
        }

        private void moveShips() {
            Point2D shootPlayer1Direction = new Point2D(0, 0);
            Point2D shootPlayer2Direction = new Point2D(0, 0);
            if (pressedKeys.contains(KeyCode.W)) {
                gameController.moveShip(0, GameController.Direction.UP);
                shootPlayer1Direction = shootPlayer1Direction.add(
                        GameController.Direction.UP.getVector());
            }
            if (pressedKeys.contains(KeyCode.S)) {
                gameController.moveShip(0, GameController.Direction.DOWN);
                shootPlayer1Direction = shootPlayer1Direction.add(
                        GameController.Direction.DOWN.getVector());
            }
            if (pressedKeys.contains(KeyCode.A)) {
                gameController.moveShip(0, GameController.Direction.LEFT);
                shootPlayer1Direction = shootPlayer1Direction.add(
                        GameController.Direction.LEFT.getVector());
            }
            if (pressedKeys.contains(KeyCode.D)) {
                gameController.moveShip(0, GameController.Direction.RIGHT);
                shootPlayer1Direction = shootPlayer1Direction.add(
                        GameController.Direction.RIGHT.getVector());
            }
            if (pressedKeys.contains(KeyCode.UP)) {
                gameController.moveShip(1, GameController.Direction.UP);
                shootPlayer2Direction = shootPlayer2Direction.add(
                        GameController.Direction.UP.getVector());
            }
            if (pressedKeys.contains(KeyCode.DOWN)) {
                gameController.moveShip(1, GameController.Direction.DOWN);
                shootPlayer2Direction = shootPlayer2Direction.add(
                        GameController.Direction.DOWN.getVector());
            }
            if (pressedKeys.contains(KeyCode.LEFT)) {
                gameController.moveShip(1, GameController.Direction.LEFT);
                shootPlayer2Direction = shootPlayer2Direction.add(
                        GameController.Direction.LEFT.getVector());
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                gameController.moveShip(1, GameController.Direction.RIGHT);
                shootPlayer2Direction = shootPlayer2Direction.add(
                        GameController.Direction.RIGHT.getVector());
            }
            if (!pressedKeys.contains(KeyCode.W) &&
                    !pressedKeys.contains(KeyCode.S) &&
                    !pressedKeys.contains(KeyCode.A) &&
                    !pressedKeys.contains(KeyCode.D)) {
                gameController.moveShip(0, GameController.Direction.STOP);
            }
            if (!pressedKeys.contains(KeyCode.UP) &&
                    !pressedKeys.contains(KeyCode.DOWN) &&
                    !pressedKeys.contains(KeyCode.LEFT) &&
                    !pressedKeys.contains(KeyCode.RIGHT)) {
                gameController.moveShip(1, GameController.Direction.STOP);
            }
            shootIfPressed(KeyCode.SPACE, shootPlayer1Direction, 0);
            shootIfPressed(KeyCode.MINUS, shootPlayer2Direction, 1);
            anchorPane.getChildren()
                      .removeIf(child -> child instanceof Bullet bullet &&
                              bullet.hasHit());
            anchorPane.getChildren().forEach(child -> {
                if (child instanceof Bullet bullet) {
                    gameController.moveBullet(bullet);
                }
            });
        }

        private void shootIfPressed(KeyCode key, Point2D direction,
                                    int shipId) {
            if (pressedKeys.contains(key)) {
                Bullet bullet = gameController.shoot(shipId, direction);
                if (bullet != null) {
                    anchorPane.getChildren().add(bullet);
                }
            }
        }
    };


    @FXML
    void restart(ActionEvent event) {
        try {
            SystemManager.getInstance().restart();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnimationTimer getCollisionTimer() {
        return collisionTimer;
    }

    public KeyPressedHandler getKeyPressedHandler() {
        return new KeyPressedHandler();
    }

    public KeyReleasedHandler getKeyReleasedHandler() {
        return new KeyReleasedHandler();
    }

    public WindowCloseHandler getWindowCloseHandler() {
        return new WindowCloseHandler();
    }


    private class KeyPressedHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            KeyCode keyCode = event.getCode();
            if (!pressedKeys.contains(keyCode)) {
                pressedKeys.add(keyCode);
            }
        }
    }

    private class KeyReleasedHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            pressedKeys.remove(event.getCode());
        }
    }

    private class WindowCloseHandler implements EventHandler<WindowEvent> {
        @Override
        public void handle(WindowEvent event) {
            executor.shutdown();
            gameController.shutdownAllExecutors();

            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
