package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

class WorkerTest {

    Player player;
    Worker worker;

    @BeforeEach
    void setUp() {
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
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
    void print() {
        worker.setPosition(1,2);
        assertEquals("{Master: {Username: TestName, Color: BLUE, God: null} X: 1 Y: 2}", worker.toString());
    }
}