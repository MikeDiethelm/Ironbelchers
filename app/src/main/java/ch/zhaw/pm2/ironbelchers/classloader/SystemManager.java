package ch.zhaw.pm2.ironbelchers.classloader;

import ch.zhaw.pm2.ironbelchers.game.weapons.IWeapon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

public class SystemManager {
    private static SystemManager instance;

    private SystemManager() {
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    public void restart() throws IOException {
        try {
            Runtime.getRuntime().exec("../gradlew run");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public ObservableList<String> getWeapons() {
        return FXCollections.observableArrayList(Arrays.stream(
                                                               Objects.requireNonNull(new File(
                                                                       "src/main/java/ch/zhaw/pm2/ironbelchers/game/weapons/").listFiles()))
                                                       .filter(file -> file.isFile() &&
                                                               !"IWeapon.java".equals(
                                                                       file.getName()))
                                                       .map(file -> file.getName()
                                                                        .replace(
                                                                                ".java",
                                                                                ""))
                                                       .toArray(String[]::new));
    }

    public IWeapon loadWeapon(String name) {
        try {
            Class<?> clazz = Class.forName(
                    "ch.zhaw.pm2.ironbelchers.game.weapons." + name);
            return (IWeapon) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
