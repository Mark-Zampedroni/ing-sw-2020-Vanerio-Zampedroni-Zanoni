package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.utility.constants.Height;
import it.polimi.ingsw.utility.serialization.dto.DtoTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class dtoTileTest {

    Tile tile;
    DtoTile dtOtile;

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
        dtOtile = new DtoTile(tile);
        assertTrue(dtOtile.hasDome());
    }

    /**
     * Testing if is correctly stored the height of the tower in DTOtile
     */
    @Test
    void getHeight() {
        tile.increaseHeight();
        tile.increaseHeight();
        dtOtile = new DtoTile(tile);
        assertEquals(dtOtile.getHeight(), Height.MID);
    }

    /**
     * Testing if the toString is the same for DTO and normal tile versions
     */
    @Test
    void toStringTest() {
        tile.increaseHeight();
        tile.putDome();
        dtOtile = new DtoTile(tile);
        assertEquals(tile.toString(),dtOtile.toString());
    }
}
