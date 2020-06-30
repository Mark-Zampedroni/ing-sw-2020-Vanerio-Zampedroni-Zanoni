package it.polimi.ingsw.mvc.model.player;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;


class PlayerTest {

    Player player;

    @BeforeEach
    void setUp(){
        player = new Player("Paolo", Colors.BLUE);
    }

    @AfterEach
    void clear(){
        player=null;
    }

    // Checks if the the flags for Challenger and Starter work properly
    @Test
    void correctlyFlagged() {
        assertFalse(player.isChallenger());
        player.setChallenger();
        assertTrue(player.isChallenger());
        player.setStarter();
        assertTrue(player.isStarter());
        assertEquals(player.getColor(), Colors.BLUE);
    }

    // Checks the removal of workers in case a player loses
    @Test
    void correctlyLoser(){
        assertFalse(player.isLoser());
        player.addWorker(new Position(1,1));
        player.addWorker(new Position(2,2));
        player.setRules(GodRules.getInstance(Gods.HERA));
        assertEquals(player.getWorkers().size(), 2);
        player.setLoser();
        assertTrue(player.isLoser());
        assertEquals(player.getWorkers().size(),0);
    }

    //Checks if during the creation the workers are added and the remove of them
    @Test
    void correctlyCreated () {
        //Remove workers by index
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

        //Removes the workers by position on board
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
