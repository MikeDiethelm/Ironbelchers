package ch.zhaw.pm2.ironbelchers.game.weapons;


import ch.zhaw.pm2.ironbelchers.game.Bullet;
import javafx.geometry.Point2D;

public interface IWeapon {
    Bullet fire(Point2D direction, Point2D position, int shipId);

    void reload();

    void stop();
}
