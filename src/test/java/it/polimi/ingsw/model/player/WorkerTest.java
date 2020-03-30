package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import org.junit.jupiter.api.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    Player player;
    Worker worker;

    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }

    @Test
    void getPosition() {
        System.out.println(Session.getPlayers());
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
        Worker worker2 = new Worker(player, new Position(2,2));
        assertTrue(worker2.getPosition().equals(new Position(2,2)));
        assertEquals(player,worker.getMaster());
    }
}