package it.polimi.ingsw.mvc.model.player;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.utility.enumerations.Colors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    //Controls if the position of a worker is correctly assigned
    @Test
    void getPosition() {
        for(int x = 0; x < 6; x++) {
            for(int y = 0; y < 6; y++) {
                worker.setPosition(new Position(x, y));
                assertTrue(worker.getPosition().isSameAs(new Position(x, y)));
                assertFalse(worker.getPosition().isSameAs(new Position(x + 1, y)));
                assertFalse(worker.getPosition().isSameAs(new Position(x, y + 1)));
            }
        }
    }

    //Checks if the toString method works
    @Test
    void testToString() {
        worker.setPosition(1,2);
        assertEquals("{Master: {Username: TestName, Color: BLUE, God: null} X: 1 Y: 2}", worker.toString());
    }

    //Controls if the master is correctly assigned to the worker
    @Test
    void getMaster() {
        Worker worker2 = new Worker(new Position(-3,-3));
        assertTrue(worker2.getPosition().isSameAs(new Position(-3, -3)));
        assertEquals(worker.getMaster(), player);
        worker2 = new Worker(new Position(1,2));
        worker2.setPosition(3,4);
        assertNull(worker2.getMaster());
    }
}