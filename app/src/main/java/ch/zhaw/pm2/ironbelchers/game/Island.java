package ch.zhaw.pm2.ironbelchers.game;


import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**This class describes an island, which is an immovable game object */
public class Island extends GameObject {
    public Island(Point2D position, int width, int height) {
        super(position, width, height, Color.GREEN);
        setStroke(Color.LIME);
    }

}
