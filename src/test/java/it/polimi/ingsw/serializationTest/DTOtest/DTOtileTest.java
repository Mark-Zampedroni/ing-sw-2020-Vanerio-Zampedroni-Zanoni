package it.polimi.ingsw.serializationTest.DTOtest;

import it.polimi.ingsw.MVC.model.map.Tile;
import it.polimi.ingsw.utility.constants.Height;
import it.polimi.ingsw.utility.serialization.DTO.DTOtile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.utility.constants.Height.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DTOtileTest {

    Tile tile;
    DTOtile dtOtile;

    @BeforeEach
    void setUp() {
        tile = new Tile();
    }

    /**
     * Testing if the flag hasDome is passed
     */
    @Test
    void hasDomeTest() {
        tile.putDome();
        dtOtile = new DTOtile(tile);
        assertTrue(dtOtile.hasDome());
    }

    /**
     * Testing if is correctly stored the height of the tower in DTOtile
     */
    @Test
    void getHeight() {
        tile.increaseHeight();
        tile.increaseHeight();
        dtOtile = new DTOtile(tile);
        assertEquals(dtOtile.getHeight(), Height.MID);
    }

    /**
     * Testing if the toString is the same for DTO and normal tile versions
     */
    @Test
    void toStringTest() {
        tile.increaseHeight();
        tile.putDome();
        dtOtile = new DTOtile(tile);
        assertEquals(tile.toString(),dtOtile.toString());
    }
}
