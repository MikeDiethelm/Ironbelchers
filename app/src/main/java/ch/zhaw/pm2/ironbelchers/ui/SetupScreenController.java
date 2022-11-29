package ch.zhaw.pm2.ironbelchers.ui;

import ch.zhaw.pm2.ironbelchers.classloader.SystemManager;
import ch.zhaw.pm2.ironbelchers.game.GameController;
import ch.zhaw.pm2.ironbelchers.game.Island;
import ch.zhaw.pm2.ironbelchers.game.Ship;
import ch.zhaw.pm2.ironbelchers.game.ShipData;
import ch.zhaw.pm2.ironbelchers.game.weapons.IWeapon;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;


/**Controller class for the Setup screen,
 * where weapons can be loaded into the game,
 * and weapons can be chosen.  */
public class SetupScreenController {
    @FXML
    public Text errorText;
    @FXML
    public ColorPicker player1ColorPicker;
    @FXML
    public ColorPicker player2ColorPicker;
    @FXML
    public ChoiceBox<String> player1WeaponChoiceBox;
    @FXML
    public ChoiceBox<String> player2WeaponChoiceBox;

    public void initialize() {
        ObservableList<String> weaponNames = SystemManager.getInstance()
                                                          .getWeapons();
        player1WeaponChoiceBox.setItems(weaponNames);
        player2WeaponChoiceBox.setItems(weaponNames);
    }

    public void startGamePressed() {
        if (isInputValid()) {
            ObservableList<String> player1Weapons = player1WeaponChoiceBox.getItems();
            ObservableList<String> player2Weapons = player2WeaponChoiceBox.getItems();

            IWeapon player1Weapon = SystemManager.getInstance()
                                                 .loadWeapon(player1Weapons.get(
                                                         player1WeaponChoiceBox.getSelectionModel()
                                                                               .getSelectedIndex()));
            IWeapon player2Weapon = SystemManager.getInstance()
                                                 .loadWeapon(player2Weapons.get(
                                                         player2WeaponChoiceBox.getSelectionModel()
                                                                               .getSelectedIndex()));

            ShipData ship1Data = new ShipData(new Point2D(0, 0), 100, 50,
                                              player1ColorPicker.getValue(),
                                              player1Weapon);

            ShipData ship2Data = new ShipData(new Point2D(500, 350), 100, 50,
                                              player2ColorPicker.getValue(),
                                              player2Weapon);
            Ship ship1 = new Ship(ship1Data);
            Ship ship2 = new Ship(ship2Data);
            GameController gameController = new GameController(
                    new Point2D(600, 400), ship1, ship2);
            gameController.addIsland(new Island(new Point2D(150, 150), 50, 50));
            gameController.addIsland(new Island(new Point2D(250, 300), 50, 50));
            gameController.addIsland(new Island(new Point2D(450, 200), 50, 50));
            try {
                MainWindow.newGame((Stage) errorText.getScene().getWindow(),
                                   gameController);
            } catch (IOException e) {
                errorText.setVisible(true);
                errorText.setText("Error: Could not start a new game");
            }

        }
    }

    private boolean isInputValid() {
        if (player1WeaponChoiceBox.getSelectionModel().isEmpty() ||
                player2WeaponChoiceBox.getSelectionModel().isEmpty()) {
            errorText.setVisible(true);
            errorText.setText("Error: Please select a weapon");
            return false;
        }
        if (player1ColorPicker.getValue()
                              .equals(player2ColorPicker.getValue())) {
            errorText.setVisible(true);
            errorText.setText("Please choose different colors");
            return false;
        }
        return true;
    }

    public void quitPressed() {
        Platform.exit();
    }

    public void createNewWeapon() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainWindow.class.getResource("/CodeEditorWindow.fxml"));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);

            Stage window = new Stage();
            window.initModality(Modality.APPLICATION_MODAL);
            window.initOwner(errorText.getScene().getWindow());
            window.setScene(scene);
            window.showAndWait();
        } catch (IOException e) {
            errorText.setVisible(true);
            errorText.setText("Error: Could not open the code editor");
        }
    }

}
