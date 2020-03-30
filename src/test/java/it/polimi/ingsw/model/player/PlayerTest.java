package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.rules.CommonRules;
import it.polimi.ingsw.rules.GodRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


class PlayerTest {

    @Test
    void correctlyFlagged() {
        Player player = new Player("Paolo");
        assertFalse(player.isChallenger());
        player.setChallenger();
        assertTrue(player.isChallenger());
        player.setWinner();
        assertTrue(player.isWinner());
    }

    @Test
    void correctlyCreated () {
        Player player = new Player("Paolo");
        assertEquals(player.getUsername(),"Paolo");
        assertNull(player.getGod());
        player.setGod(Gods.APOLLO);
        player.setColor(Colors.WHITE);
        String string = player.toString();
        System.out.println(string);
        assertEquals("{Username: Paolo, Color: WHITE, God: APOLLO}", string);
        ArrayList<Worker> workers = player.getWorkers();
        assertEquals(workers.size(), 2);
        assertEquals(workers.get(0).getMaster(), player);
        assertEquals(workers.get(1).getMaster(), player);
        GodRules rules = new CommonRules();
        player.setRules(rules);
        assertNotEquals(player.getRules(), null);
    }


}
