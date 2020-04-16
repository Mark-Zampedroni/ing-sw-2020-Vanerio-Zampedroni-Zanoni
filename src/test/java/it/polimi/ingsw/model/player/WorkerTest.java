package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.gods.Setupper;
import org.junit.jupiter.api.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    Player player;
    Worker worker;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    @Test
    void getPosition() {
        for(int x = 0; x < 6; x++) {
            for(int y = 0; y < 6; y++) {
                worker.setPosition(new Position(x,y));
                assertTrue(worker.getPosition().equals(new Position(x,y)));
                assertFalse(worker.getPosition().equals(new Position(x+1,y)));
                assertFalse(worker.getPosition().equals(new Position(x,y+1)));
            }
        }
    }

    @Test
    void testToString() {
        worker.setPosition(1,2);
        assertEquals("{Master: {Username: TestName, Color: BLUE, God: null} X: 1 Y: 2}", worker.toString());
    }

    @Test
    void getMaster() {
        Worker worker2 = new Worker(new Position(-3,-3));
        assertTrue(worker2.getPosition().equals(new Position(-3,-3)));
        assertEquals(worker.getMaster(), player);
    }
}