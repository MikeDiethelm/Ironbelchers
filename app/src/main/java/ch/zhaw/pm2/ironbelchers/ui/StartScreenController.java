package ch.zhaw.pm2.ironbelchers.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


/**Controller class for the Startscreen menu */
public class StartScreenController {
    @FXML
    public Text errorText;

    /**
     * Displays the config menu in the current window
     */
    public void playPressed() {
        try {
            MainWindow.setupGame((Stage) errorText.getScene().getWindow());
        } catch (IOException e) {
            errorText.setVisible(true);
            errorText.setText("Error: Could not setup a new game");
        }
    }

    /**
     * Exits application
     */
    public void quitPressed() {
        Platform.exit();
    }
}
