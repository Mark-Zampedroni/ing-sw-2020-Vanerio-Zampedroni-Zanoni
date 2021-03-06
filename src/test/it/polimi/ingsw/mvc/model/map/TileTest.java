package it.polimi.ingsw.mvc.model.map;

import static it.polimi.ingsw.utility.constants.Height.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    Tile tile;

    @BeforeEach
    void setUp() {
        tile = new Tile();
    }

    // Checks if the method for placing a dome works
    @Test
    void placeHasDome() {
        assertFalse(tile.hasDome());
        tile.putDome();
        assertTrue(tile.hasDome());
    }

    // Checks if the method for improve the height works
    @Test
    void increaseGetHeight() {
        assertEquals(GROUND,tile.getHeight());
        tile.increaseHeight(); // BOTTOM
        assertEquals(BOTTOM,tile.getHeight());
        tile.increaseHeight(); // MID
        tile.increaseHeight(); // TOP
        tile.increaseHeight(); // DOME
        assertEquals(TOP,tile.getHeight());
        assertTrue(tile.hasDome());
        tile.increaseHeight();
    }

    // Tests toString method
    @Test
    void testToString() {
        tile.increaseHeight();
        tile.putDome();
        assertEquals("Tower, height: 1, dome: true",tile.toString());
    }
}