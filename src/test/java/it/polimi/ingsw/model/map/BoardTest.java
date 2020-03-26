package it.polimi.ingsw.model.map;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class BoardTest {

    @Test
    void correctlyCreated() {
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                assertEquals(Board.getTile(new Position(i,j)).getHeight(), 0);
                assertFalse(Board.getTile(new Position(i,j)).hasDome());
            }
        }
    }

    @Test
    void correctManagment() {
        Board.getTile(new Position(2,3)).increaseHeight();
        Board.getTile(new Position(4, 4)).placeDome();
        assertEquals(Board.getTile(new Position(2,3)).getHeight(), 1);
        assertTrue(Board.getTile(new Position(4, 4)).hasDome());
    }
}
