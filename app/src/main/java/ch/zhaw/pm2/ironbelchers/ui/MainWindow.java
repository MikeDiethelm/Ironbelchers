package ch.zhaw.pm2.ironbelchers.ui;

import ch.zhaw.pm2.ironbelchers.game.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Helper class to provide easy access to start, setup and play a new game.
 */
public class MainWindow extends Application {

    /**
     * Opens a new game
     *
     * @param primaryStage   Stage to be used
     * @param gameController to be used
     * @throws IOException if the stage could not be initialised
     */
    public static void newGame(Stage primaryStage,
                               GameController gameController)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainWindow.class.getResource("/MainWindow.fxml"));
        Pane rootPane = loader.load();
        Scene scene = new Scene(rootPane);
        MainWindowController controller = loader.getController();
        controller.setup(gameController);
        scene.setOnKeyPressed(controller.getKeyPressedHandler());
        scene.setOnKeyReleased(controller.getKeyReleasedHandler());

        Stage window = (Stage) primaryStage.getScene().getWindow();

        window.setOnCloseRequest(controller.getWindowCloseHandler());
        window.setScene(scene);
        window.show();
    }

    /**
     * Opens the configurator for the game
     *
     * @param primaryStage Stage to be used
     * @throws IOException If the stage could not be initialised
     */
    public static void setupGame(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                MainWindow.class.getResource("/SetupScreen.fxml"));
        Pane rootPane = loader.load();
        Scene scene = new Scene(rootPane);

        Stage window = (Stage) primaryStage.getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

    /**
     * Opens the start menu
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if the stage could not be initialised
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/StartScreen.fxml"));
        Pane rootPane = loader.load();
        Scene scene = new Scene(rootPane);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ironbelchers");
        primaryStage.show();
    }

}
