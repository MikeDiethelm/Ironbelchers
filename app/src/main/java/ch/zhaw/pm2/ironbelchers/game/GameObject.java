package ch.zhaw.pm2.ironbelchers.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**This class is a superclass which describes all game object on the game field */
public abstract class GameObject extends Rectangle {

    protected GameObject(Point2D position, int width, int height,
                         Color colour) {
        super(position.getX(), position.getY(), width, height);
        setFill(colour);
    }

    public Point2D getPosition() {
        return new Point2D(getX(), getY());
    }

    protected void setPosition(Point2D newPosition) {
        setX(newPosition.getX());
        setY(newPosition.getY());
    }

    /**This method checks for collision with any other gameObject */
    public boolean collidesWith(GameObject gameObject2) {
        return this.getBoundsInLocal()
                   .intersects(gameObject2.getBoundsInLocal());
    }

}
