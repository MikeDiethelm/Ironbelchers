package ch.zhaw.pm2.ironbelchers.ui;

import ch.zhaw.pm2.ironbelchers.classloader.ClassCompiler;
import ch.zhaw.pm2.ironbelchers.classloader.ClassCreator;
import ch.zhaw.pm2.ironbelchers.classloader.SystemManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;


public class CodeEditorWindowController {
    @FXML
    public TextField weaponNameTextField;
    @FXML
    public TextArea weaponCodeTextArea;

    @FXML
    public void initialize() {
        weaponNameTextField.setText("NewWeapon");
        weaponCodeTextArea.setText(
                "package ch.zhaw.pm2.ironbelchers.game.weapons;" +
                        System.lineSeparator() + System.lineSeparator() +
                        "public class " + weaponNameTextField.getText() +
                        " extends BasicWeapon {" + System.lineSeparator() +
                        System.lineSeparator() + "    public " +
                        weaponNameTextField.getText() + "() {" +
                        System.lineSeparator() +
                        "        super(/* Enter bullet speed */, /* Enter damage */, /* Enter reloading time */);" +
                        System.lineSeparator() + "    }" +
                        System.lineSeparator() + System.lineSeparator() +
                        System.lineSeparator() + "}" + System.lineSeparator());
    }

    public void compilePressed() throws IOException {
        ClassCreator classCreator = new ClassCreator(
                "src/main/java/ch/zhaw/pm2/ironbelchers/game/weapons/");
        File newFile = classCreator.save(weaponNameTextField.getText(),
                                         weaponCodeTextArea.getText());
        ClassCompiler classCompiler = new ClassCompiler(newFile);
        if (classCompiler.compile()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Compilation successful");
            alert.setHeaderText("Compilation successful");
            alert.setContentText("The class has been compiled successfully.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }
            SystemManager.getInstance().restart();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Compilation failed");
            alert.setHeaderText("Compilation failed!");
            alert.setContentText("Please check your code and try again.");
            alert.showAndWait();
            Files.delete(newFile.toPath());
        }

    }

    public void quitPressed() {
        weaponNameTextField.getScene().getWindow().hide();
    }
}
