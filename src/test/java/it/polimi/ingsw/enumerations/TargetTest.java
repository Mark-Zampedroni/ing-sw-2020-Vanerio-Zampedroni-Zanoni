package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TargetTest {

    Player player;
    Worker worker;
    Player player2;
    Worker worker2;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = new Player("TestName");
        player2 = new Player("TestName2");
        Session.getInstance().addPlayer(player);
        Session.getInstance().addPlayer(player2);
        worker = player.getWorkers().get(0);
        worker2 = player2.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Session.getInstance().removePlayer(player);
        Session.getInstance().removePlayer(player2);
    }

    @Test
    void compareWorkersTest() {
        assertTrue(Target.ENEMY.compareWorkers(worker,worker2));
        assertFalse(Target.ENEMY.compareWorkers(worker,player.getWorkers().get(1)));
        assertTrue(Target.ALLY.compareWorkers(worker,player.getWorkers().get(1)));
        assertFalse(Target.ALLY.compareWorkers(worker,worker2));
        assertTrue(Target.ANY.compareWorkers(worker,worker2));
        assertTrue(Target.ANY.compareWorkers(worker,player.getWorkers().get(1)));
        assertTrue(Target.SELF.compareWorkers(worker,worker));
        assertFalse(Target.SELF.compareWorkers(worker,player.getWorkers().get(1)));
    }

}