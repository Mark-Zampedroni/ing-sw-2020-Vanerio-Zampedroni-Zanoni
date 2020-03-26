package it.polimi.ingsw.model.player;

import it.polimi.ingsw.enumerations.Gods;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;


class PlayerTest {

    @Test
    void correctlyFlagged() {
        Player player = new Player("Paolo");
        assertFalse(player.isChallenger());
        assertFalse(player.isChallenger());
        player.setChallenger();
        assertTrue(player.isChallenger());
        player.setWinner();
        assertTrue(player.isWinner());
    }

    @Test
    void correctlyCreated () {
        Player player = new Player("Sandro");
        assertNull(player.getGod());
        player.setGod(Gods.APOLLO);
        String string = player.toString();
        System.out.println(string);
        assertEquals("{Username: Paolo, Color: BLUE, God: APOLLO}", string);
        ArrayList<Worker> workers = player.getWorkers();
        assertEquals(workers.size(), 2);
        assertEquals(workers.get(0).getMaster(), player);
        assertEquals(workers.get(1).getMaster(), player);
    }


}
