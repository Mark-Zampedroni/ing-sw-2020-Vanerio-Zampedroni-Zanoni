package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.utility.constants.Height;
import it.polimi.ingsw.utility.dto.DtoTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DtoTileTest {

    Tile tile;
    DtoTile dtoTile;

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
        dtoTile = new DtoTile(tile);
        assertTrue(dtoTile.hasDome());
    }

    /**
     * Testing if is correctly stored the height of the tower in DTOtile
     */
    @Test
    void getHeight() {
        tile.increaseHeight();
        tile.increaseHeight();
        dtoTile = new DtoTile(tile);
        assertEquals(dtoTile.getHeight(), Height.MID);
    }

    /**
     * Testing if the toString is the same for DTO and normal tile versions
     */
    @Test
    void toStringTest() {
        tile.increaseHeight();
        tile.putDome();
        dtoTile = new DtoTile(tile);
        assertEquals(tile.toString(), "Tower, height: 1, dome: true");
    }
}
