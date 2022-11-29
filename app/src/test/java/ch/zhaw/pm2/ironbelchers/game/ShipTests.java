package ch.zhaw.pm2.ironbelchers.game;

import ch.zhaw.pm2.ironbelchers.game.weapons.BasicWeapon;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** This class tests the behaviour of the ships
 *  Crash detection, weapon firing etc
 *
 * */
class ShipTests {

    @Test
    void ship_moves_upwards() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.UP);
        assertTrue(100 > ship.getPosition().getY());
        assertEquals(100, ship.getPosition().getX());
    }

    @Test
    void ship_moves_downwards() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.DOWN);
        assertTrue(100 < ship.getPosition().getY());
        assertEquals(100, ship.getPosition().getX());
    }

    @Test
    void ship_moves_right() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.RIGHT);
        assertEquals(100, ship.getPosition().getY());
        assertTrue(100 < ship.getPosition().getX());
    }

    @Test
    void ship_moves_left() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.LEFT);
        assertEquals(100, ship.getPosition().getY());
        assertTrue(100 > ship.getPosition().getX());
    }

    @Test
    void ship_moves_up_and_left() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.UP);
        ship.move(GameController.Direction.LEFT);
        assertTrue(100 > ship.getPosition().getY());
        assertTrue(100 > ship.getPosition().getX());
    }

    @Test
    void ship_moves_up_and_right() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.RIGHT);
        ship.move(GameController.Direction.UP);
        assertTrue(100 > ship.getPosition().getY());
        assertTrue(100 < ship.getPosition().getX());
    }

    @Test
    void ship_down_and_left() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.LEFT);
        ship.move(GameController.Direction.DOWN);
        assertTrue(100 < ship.getPosition().getY());
        assertTrue(100 > ship.getPosition().getX());
    }

    @Test
    void ship_moves_down_and_right() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.RIGHT);
        ship.move(GameController.Direction.DOWN);
        assertTrue(100 < ship.getPosition().getY());
        assertTrue(100 < ship.getPosition().getX());
    }

    @Test
    void ship_does_not_move_if_left_and_right_is_pressed_simultaneously() {
        Ship ship = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        ship.move(GameController.Direction.RIGHT);
        ship.move(GameController.Direction.LEFT);
        assertEquals(100, ship.getPosition().getY());
        assertEquals(100, ship.getPosition().getX());
    }

    @Test
    void ship_crashes_if_two_ships_start_exactly_within_each_other() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        Ship ship2 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        assertTrue(ship1.collidesWith(ship2));
    }

    @Test
    void ship_crashes_if_two_ships_start_within_each_other() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        Ship ship2 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        assertTrue(ship1.collidesWith(ship2));
    }

    @Test
    void ship_does_not_crash_when_are_away_from_each_other() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, null));
        Ship ship2 = new Ship(
                new ShipData(new Point2D(100, 200), 50, 50, Color.GREEN, null));
        assertFalse(ship1.collidesWith(ship2));
    }

    @Test
    void ship_shoots_in_direction_of_current_movement() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        ship1.move(GameController.Direction.DOWN);

        Bullet bullet = ship1.shoot(new Point2D(0, 100), 0);
        assertTrue(bullet.getDirection().getY() > 0);
        assertTrue(bullet.getDirection().getX() == 0);
    }

    @Test
    void bullet_hits_enemy_ship() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        Ship ship2 = new Ship(
                new ShipData(new Point2D(151, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        GameController gameController = new GameController(new Point2D(500, 500), ship1, ship2);
        Bullet bullet = ship1.shoot(new Point2D(100, 0), 0);
        gameController.moveBullet(bullet);
        assertTrue(bullet.hasHit());
    }

    @Test
    void several_hits_sink_enemy_ship() {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        Ship ship2 = new Ship(
                new ShipData(new Point2D(151, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        int health = ship2.getHealth();

        GameController gameController = new GameController(new Point2D(500, 500), ship1, ship2);

        Bullet bullet1 = new Bullet(new Point2D(100, 100), 1, health, new Point2D(100, 0), 0);
        gameController.moveBullet(bullet1);
        assertTrue(ship2.isDestroyed());
    }

    @Test
    void ship_needs_to_reload_after_shot () {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        Bullet bullet1 = ship1.shoot(new Point2D(100, 100), 0);
        assertNotNull(bullet1);
        Bullet bullet2 = ship1.shoot(new Point2D(100, 100), 0);
        assertNull(bullet2);
    }

    @Test
    void ship_crashes_with_island () {
        Ship ship1 = new Ship(
                new ShipData(new Point2D(100, 100), 50, 50, Color.GREEN, new BasicWeapon()));
        Island island = new Island(new Point2D(151, 100), 50, 50);
        ship1.move(GameController.Direction.RIGHT);
        assertTrue(ship1.collidesWith(island));
    }



}
