package ch.zhaw.pm2.ironbelchers.game;

import ch.zhaw.pm2.ironbelchers.game.weapons.IWeapon;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

import static java.util.Objects.requireNonNull;

public record ShipData(Point2D position, int width, int height, Color color,
                       IWeapon weapon) {
    public ShipData {
        requireNonNull(position);
        requireNonNull(color);
    }
}
