package ch.zhaw.pm2.ironbelchers.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

/**This class describes a bullet, a gameObject that is created when a ship shoots */
public class Bullet extends GameObject implements Movable {
    private final int velocity;
    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    private final int damage;

    private final Point2D direction;

    private final int shipId;

    private boolean hasHit = false;

    public Bullet(Point2D position, int velocity, int damage, Point2D direction,
                  int shipId) {
        super(position, WIDTH, HEIGHT, Color.GRAY);
        this.velocity = velocity;
        this.damage = damage;
        this.direction = direction;
        this.shipId = shipId;
    }

    public Point2D getDirection() {
        return direction;
    }

    public int getShipId() {
        return shipId;
    }

    public void move() {
        setPosition(calculateNextPosition());
    }

    @Override
    public void move(GameController.Direction direction) {
        setPosition(calculateNextPosition(direction));
    }

    private Point2D calculateNextPosition() {
        double x = direction.getX() * velocity;
        double y = direction.getY() * velocity;
        return getPosition().add(new Point2D(x, y));
    }

    public Point2D calculateNextPosition(GameController.Direction direction) {
        double x = direction.getVector().getX() * velocity;
        double y = direction.getVector().getY() * velocity;
        return getPosition().add(new Point2D(x, y));
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return String.format("Bullet(%s, %s, %s)", getPosition(), velocity,
                             damage);
    }

    public boolean hasHit() {
        return hasHit;
    }

    public void setHasHit(boolean hasHit) {
        this.hasHit = hasHit;
    }
}
