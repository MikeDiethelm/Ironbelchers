package ch.zhaw.pm2.ironbelchers.game;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameControllerTests {

    GameController gameController;
    ShipData shipData;

    @BeforeEach
    void init() {
        shipData = new ShipData(new Point2D(0, 0), 0, 0, Color.AZURE, null);
        Ship ship1 = new Ship(shipData);
        Ship ship2 = new Ship(shipData);
        gameController = new GameController(new Point2D(100, 100), ship1,
                                            ship2);
    }

    @Test
    void addShip() {
        Ship ship = new Ship(shipData);
        assertTrue(gameController.addShip(ship));
    }

    @Test
    void addShips() {
        Ship ship1 = new Ship(shipData);
        Ship ship2 = new Ship(shipData);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        assertTrue(gameController.addShips(ships));
    }

    @Test
    void addIsland() {
        Island island = new Island(new Point2D(0, 0), 0, 0);
        assertTrue(gameController.addIsland(island));
    }

    @Test
    void addIslands() {
        Island island1 = new Island(new Point2D(0, 0), 0, 0);
        Island island2 = new Island(new Point2D(0, 0), 0, 0);
        List<Island> islands = new ArrayList<>();
        islands.add(island1);
        islands.add(island2);
        assertTrue(gameController.addIslands(islands));
    }

    @Test
    void outOfBounds() {
        //Island pos < bounds
        Island island1 = new Island(new Point2D(-10, -10), 10, 10);
        assertTrue(gameController.outOfBounds(island1));
        //Island pos inside bounds
        Island island2 = new Island(new Point2D(10, 10), 10, 10);
        assertFalse(gameController.outOfBounds(island2));
        //Island pos > bounds
        Island island3 = new Island(new Point2D(100, 100), 10, 10);
        assertTrue(gameController.outOfBounds(island3));
    }

    @Test
    void outOfBounds2() {
        //Ships neg < bounds
        ShipData testShipData = new ShipData(new Point2D(50, 50), 10, 10,
                                             Color.AZURE, null);
        Ship ship1 = new Ship(testShipData);
        Point2D nextPos1 = new Point2D(-100, -100);
        assertTrue(gameController.outOfBounds(ship1, nextPos1));
        //Ship inside bounds
        Ship ship2 = new Ship(testShipData);
        Point2D nextPos2 = new Point2D(10, 10);
        assertFalse(gameController.outOfBounds(ship2, nextPos2));
        //Ships pos > bounds
        Ship ship3 = new Ship(testShipData);
        Point2D nextPos3 = new Point2D(100, 100);
        assertTrue(gameController.outOfBounds(ship3, nextPos3));
    }

    @Test
    void getWinnerMessage() {
        Ship ship1 = mock(Ship.class);
        Ship ship2 = mock(Ship.class);

        GameController testGameController = new GameController(
                new Point2D(100, 100), ship1, ship2);

        when(ship1.getCrashStatus()).thenReturn(true);
        when(ship2.getCrashStatus()).thenReturn(true);

        assertEquals("Draw, both ships crashed",
                     testGameController.getWinnerMsg());

        when(ship1.getCrashStatus()).thenReturn(false);
        when(ship2.getCrashStatus()).thenReturn(false);

        when(ship1.isDestroyed()).thenReturn(true);
        when(ship2.isDestroyed()).thenReturn(false);

        assertEquals("Player 2 won!", testGameController.getWinnerMsg());

        when(ship1.isDestroyed()).thenReturn(false);
        when(ship2.isDestroyed()).thenReturn(true);

        assertEquals("Player 1 won!", testGameController.getWinnerMsg());

        when(ship1.isDestroyed()).thenReturn(false);
        when(ship2.isDestroyed()).thenReturn(false);

        assertEquals("\\(^_^)/", testGameController.getWinnerMsg());
    }

}
