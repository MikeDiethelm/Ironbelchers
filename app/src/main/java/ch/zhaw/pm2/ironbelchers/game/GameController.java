package ch.zhaw.pm2.ironbelchers.game;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

/**This class controls the flow of the game.checks for victory conditions, ship crashes etc. */
public class GameController {
    private final List<Ship> ships;
    private final Point2D fieldSize;
    private final List<GameObject> gameObjects;
    private List<Island> islands;

    public GameController(Point2D fieldSize, Ship ship1, Ship ship2) {
        this.fieldSize = fieldSize;
        ships = new ArrayList<>();
        gameObjects = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        islands = new ArrayList<>();
    }

    public void moveShip(int shipId, Direction direction) {
        Ship ship = ships.get(shipId);
        Point2D nextPos = ship.calculateNextPosition(direction);
        if (outOfBounds(ship, nextPos)) {
            return;
        }
        ship.move(direction);
    }

    public void moveBullet(Bullet bullet) {
        bullet.move();
        int targetShipId = (bullet.getShipId() + 1) % 2;
        if (bullet.collidesWith(ships.get(targetShipId))) {
            ships.get(targetShipId).hit(bullet.getDamage());
            bullet.setHasHit(true);
        }
        if (outOfBounds(bullet)) {
            bullet.setHasHit(true);
        }
    }

    public Bullet shoot(int shipId, Point2D direction) {
        Ship ship = ships.get(shipId);
        return ship.shoot(direction, shipId);
    }

    /**
     * Helper method to check if an GameObject next position is out of bounds
     *
     * @param gameObject Object to be checked
     * @param position   new Position of the Object
     * @return True if the Object is outside the map
     */
    public boolean outOfBounds(GameObject gameObject, Point2D position) {
        return (position.getX() + gameObject.getWidth() > fieldSize.getX() ||
                position.getY() + gameObject.getHeight() > fieldSize.getY() ||
                position.getX() < 0 || position.getY() < 0);
    }

    /**
     * Helper method to check if an GameObject is out of bounds
     *
     * @param gameObject Object to be checked
     * @return True if the Object is outside the map
     */
    public boolean outOfBounds(GameObject gameObject) {
        return gameObject.getX() + gameObject.getWidth() > fieldSize.getX() ||
                gameObject.getY() + gameObject.getHeight() > fieldSize.getY() ||
                gameObject.getX() < 0 || gameObject.getY() < 0;
    }

    public boolean addIsland(Island island) {
        islands.add(island);
        if (outOfBounds(island)) {
            return false;
        }
        return gameObjects.add(island);
    }

    public boolean addIslands(List<Island> islands) {
        islands.addAll(islands);
        return islands.stream()
                      .map(this::addIsland)
                      .toList()
                      .stream()
                      .allMatch(n -> n);
    }

    public boolean addShip(Ship ship) {
        if (outOfBounds(ship)) {
            return false;
        }
        return ships.add(ship);
    }

    public boolean addShips(List<Ship> ships) {
        return ships.stream()
                    .map(this::addShip)
                    .toList()
                    .stream()
                    .allMatch(n -> n);

    }

    public List<GameObject> getGameObjects() {
        List<GameObject> allObj = new ArrayList<>();
        allObj.addAll(ships);
        allObj.addAll(gameObjects);
        return allObj;
    }

    public boolean shipsCrashed() {
        if (ships.get(0).collidesWith(ships.get(1))) {
            ships.get(0).crash();
            ships.get(1).crash();
            return true;
        }

        for (Ship ship : ships) {
            for (Island island : islands) {
                if (ship.collidesWith(island)) {
                    ship.crash();
                    return true;
                }
            }
        }

        return false;
    }

    public boolean shipSunk() {
        return ships.stream().anyMatch(Ship::isDestroyed);
    }

    public String getWinnerMsg() {
        if (ships.get(0).getCrashStatus() && ships.get(1).getCrashStatus()) {
            return "Draw, both ships crashed";
        } else if (ships.get(0).isDestroyed() || ships.get(0).getCrashStatus()) {
            return "Player 2 won!";
        } else if (ships.get(1).isDestroyed() || ships.get(1).getCrashStatus()) {
            return "Player 1 won!";
        } else {
            return "\\(^_^)/";
        }
    }

    public void shutdownAllExecutors() {
        ships.forEach(Ship::shutdownExecutor);
    }

    public enum Direction {
        UP(new Point2D(0, -1)), DOWN(new Point2D(0, 1)),
        LEFT(new Point2D(-1, 0)), RIGHT(new Point2D(1, 0)),

        NONE(new Point2D(0, 0)), STOP(new Point2D(0, 0));

        private final Point2D vector;

        Direction(Point2D direction) {
            this.vector = direction;
        }

        public Point2D getVector() {
            return vector;
        }
    }

}
