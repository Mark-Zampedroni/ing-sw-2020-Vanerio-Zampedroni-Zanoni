package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.Session;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class BoardTest {

    @BeforeEach
    @AfterEach
    void setUp() { Session.getBoard().clear(); }

    @Test
    void correctlyCreated() {
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                assertEquals(Session.getBoard().getTile(new Position(i,j)).getHeight(), 0);
                assertFalse(Session.getBoard().getTile(new Position(i,j)).hasDome());
            }
        }
    }

    @Test
    void correctManagement() {
        Session.getBoard().getTile(new Position(2,3)).increaseHeight();
        Session.getBoard().getTile(new Position(4, 4)).placeDome();
        assertEquals(Session.getBoard().getTile(new Position(2,3)).getHeight(), 1);
        assertTrue(Session.getBoard().getTile(new Position(4, 4)).hasDome());
    }
}
