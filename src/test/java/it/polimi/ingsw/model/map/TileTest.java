package it.polimi.ingsw.model.map;

import static it.polimi.ingsw.constants.Height.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    Tile tile;

    @BeforeEach
    void setUp() {
        tile = new Tile();
    }

    @Test
    void placeHasDome() {
        assertFalse(tile.hasDome());
        tile.placeDome();
        assertTrue(tile.hasDome());
    }

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

    @Test
    void testToString() {
        tile.increaseHeight();
        tile.placeDome();
        assertEquals("Tower, height: 1, dome: true",tile.toString());
    }
}