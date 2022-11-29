package ch.zhaw.pm2.ironbelchers.game;


import ch.zhaw.pm2.ironbelchers.game.weapons.IWeapon;
import javafx.geometry.Point2D;

/**This class describes the ship models. It includes all the data that
 * a ship needs for its movement to shooting its weapon.
 */
public class Ship extends GameObject implements Movable {

    private boolean isCrashed;

    private boolean isDestroyed = false;

    private int health;

    private final IWeapon weapon;

    public Ship(ShipData shipData) {
        super(shipData.position(), shipData.width(), shipData.height(),
              shipData.color());
        setStroke(shipData.color());
        isCrashed = false;
        health = 100;
        weapon = shipData.weapon();
    }

    /**
     * Calculates the next position for a given direction and time
     *
     * @param direction Direction of the move
     * @return next Position
     */
    @Override
    public Point2D calculateNextPosition(GameController.Direction direction) {
        double x = direction.getVector().getX();
        double y = direction.getVector().getY();
        return getPosition().add(new Point2D(x, y));
    }

    /**
     * Moves the ship in the given direction
     *
     * @param direction Direction of the move
     */
    @Override
    public void move(GameController.Direction direction) {
        if (isCrashed) {
            return;
        }
        setPosition(calculateNextPosition(direction));
    }

    public void hit(int damage) {
        health -= damage;
        if (health <= 0) {
            isDestroyed = true;
        }
    }

    public void stop() {
        setPosition(getPosition().add(new Point2D(0, 0)));
    }

    public void crash() {
        isCrashed = true;
    }

    public boolean getCrashStatus() {
        return isCrashed;
    }

    public Bullet shoot(Point2D direction, int shipId) {
        return weapon.fire(direction, getPosition(), shipId);
    }

    public void shutdownExecutor() {
        weapon.stop();
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public IWeapon getWeapon() {
        return weapon;
    }

    public int getHealth() {
        return health;
    }
}
