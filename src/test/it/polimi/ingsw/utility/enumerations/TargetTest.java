package it.polimi.ingsw.utility.enumerations;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.Setupper;
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
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        player2 = Setupper.addPlayer("TestName2", Colors.BROWN,2);
        worker = player.getWorkers().get(0);
        worker2 = player2.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(player2);
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