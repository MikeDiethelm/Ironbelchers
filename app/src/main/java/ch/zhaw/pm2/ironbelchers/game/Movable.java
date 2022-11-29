package ch.zhaw.pm2.ironbelchers.game;

import javafx.geometry.Point2D;

/**This interface defines all objects that can move on the gamefield. */
public interface Movable {
    void move(GameController.Direction direction);

    Point2D calculateNextPosition(GameController.Direction direction);
}
