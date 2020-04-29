package it.polimi.ingsw.MVC.model.player;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.rules.GodRules;
import it.polimi.ingsw.MVC.model.rules.gods.Setupper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;


class PlayerTest {

    @Test
    void correctlyFlagged() {
        Player player = new Player("Paolo", Colors.BLUE);
        assertFalse(player.isChallenger());
        player.setChallenger();
        assertTrue(player.isChallenger());
        player.setStarter();
        assertTrue(player.isStarter());
        assertEquals(player.getColor(), Colors.BLUE);
    }

    @Test
    void correctlyCreated () {
        Player player = Setupper.addPlayer("Paolo", Colors.BLUE, 1);
        assertEquals(player.getUsername(),"Paolo");
        assertNull(player.getGod());
        player.setGod(Gods.APOLLO);
        List<Worker> workers = player.getWorkers();
        assertEquals(workers.size(), 2);
        assertEquals(workers.get(0).getMaster(), player);
        assertEquals(workers.get(1).getMaster(), player);
        GodRules rules = GodRules.getInstance(Gods.APOLLO);
        player.setRules(rules);
        assertNotEquals(player.getRules(), null);
        player.removeWorker(0);
        assertEquals(player.getWorkers().size(), 1);
        player.removeWorker(0);
        assertEquals(player.getWorkers().size(), 0);
        Setupper.removePlayer(player);
        player = Setupper.addPlayer("Paolo", Colors.BLUE,2);
        Position position = new Position(2,3);
        player.getWorkers().get(0).setPosition(position);
        player.removeWorker(position);
        assertEquals(player.getWorkers().size(), 1);
        Setupper.removePlayer(player);
        player = Setupper.addPlayer("Paolo", Colors.BLUE,3);
        player.getWorkers().get(1).setPosition(position);
        player.removeWorker(position);
        assertEquals(player.getWorkers().size(), 1);
        Setupper.removePlayer(player);
    }


}
