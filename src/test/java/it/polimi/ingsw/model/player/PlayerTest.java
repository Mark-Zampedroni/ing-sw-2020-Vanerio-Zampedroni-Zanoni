package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.CommonRules;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.gods.Setupper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


class PlayerTest {

    @Test
    void correctlyFlagged() {
        Player player = new Player("Paolo", Colors.BLUE);
        assertFalse(player.isChallenger());
        player.setChallenger();
        assertTrue(player.isChallenger());
        player.setWinner();
        assertTrue(player.isWinner());
    }

    @Test
    void correctlyCreated () {
        Player player = Setupper.addPlayer("Paolo", Colors.BLUE, 1);
        assertEquals(player.getUsername(),"Paolo");
        assertNull(player.getGod());
        player.setGod(Gods.APOLLO);
        ArrayList<Worker> workers = player.getWorkers();
        assertEquals(workers.size(), 2);
        assertEquals(workers.get(0).getMaster(), player);
        assertEquals(workers.get(1).getMaster(), player);
        GodRules rules = Gods.APOLLO.createRules();
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
