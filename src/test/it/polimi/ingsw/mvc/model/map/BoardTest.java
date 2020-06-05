package mvc.model.map;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

class BoardTest {

    @BeforeEach
    @AfterEach
    void setUp() { Session.getInstance().getBoard().clear(); }

    @Test
    void correctlyCreated() {
        for (int i=0; i<5; i++) {
            for (int j=0; j<5; j++) {
                assertEquals(Session.getInstance().getBoard().getTile(new Position(i,j)).getHeight(), 0);
                assertFalse(Session.getInstance().getBoard().getTile(new Position(i,j)).hasDome());
            }
        }
    }

    @Test
    void correctManagement() {
        Session.getInstance().getBoard().getTile(new Position(2,3)).increaseHeight();
        Session.getInstance().getBoard().getTile(new Position(4, 4)).putDome();
        assertEquals(Session.getInstance().getBoard().getTile(new Position(2,3)).getHeight(), 1);
        assertTrue(Session.getInstance().getBoard().getTile(new Position(4, 4)).hasDome());
    }
}
