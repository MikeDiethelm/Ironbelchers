package ch.zhaw.pm2.ironbelchers.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class DeathScreenController {
    @FXML
    public Text winText;
    @FXML
    private Text errorText;

    /**
     * Exits application
     */
    public void quitPressed() {
        Platform.exit();
    }

    /**
     * Sets the text of the winner
     *
     * @param winningMsg to be displayed
     */
    public void displayWinner(String winningMsg) {
        winText.setText(winningMsg);
    }

    /**
     * Starts a new game
     */
    public void replayPressed() {
        try {
            MainWindow.setupGame((Stage) errorText.getScene().getWindow());
        } catch (IOException e) {
            errorText.setVisible(true);
            errorText.setText("Error: Could not setup a new game");
        }
    }

}
