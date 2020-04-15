package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;


class SessionTest {

    Player one, two, three;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        one = new Player("Piero");
        two = new Player ("Sandro");
        three = new Player ("Carmelo");
        Session.getInstance().addPlayer(one);
        Session.getInstance().addPlayer(two);
        Session.getInstance().addPlayer(three);
        Session.getInstance().setStarted(true);
        assertTrue(Session.getInstance().isStarted());
    }

    @AfterEach
    void clear() {
        Session.getInstance().getBoard().clear();
        Session.getInstance().removePlayer(one);
        Session.getInstance().removePlayer(two);
        Session.getInstance().removePlayer(three);
    }

    @Test
    void PlayerManagement (){
        assertEquals(3,Session.getInstance().playersNumber());
        Session.getInstance().pickChallenger();
        assertTrue((one.isChallenger() ^ two.isChallenger()) ^ three.isChallenger()); //xor
        ArrayList<Player> otherPlayer = Session.getInstance().getOtherPlayers(one);
        assertEquals(two.getUsername(), otherPlayer.get(0).getUsername());
        assertEquals(three.getUsername(), otherPlayer.get(1).getUsername());
        Session.getInstance().removePlayer(one);
        assertEquals(Session.getInstance().playersNumber(), 2);
        Session.getInstance().addPlayer(one);
        assertFalse(Session.getInstance().hasWinner());
        one.setWinner();
        assertTrue(Session.getInstance().hasWinner());
        two.removeWorker(0);
        Session.getInstance().removePlayer(two);
        assertEquals(Session.getInstance().playersNumber(), 2);
        two= new Player("Sandro");
        Session.getInstance().addPlayer(two);
    }

    @Test
    void GodsManagement() {
        one.setGod(Gods.APOLLO);
        Session.getInstance().addGod(one.getGod());
        two.setGod(Gods.ATLAS);
        Session.getInstance().addGod(two.getGod());
        Session.getInstance().getGods();
        Session.getInstance().getPlayers();
        assertEquals(Session.getInstance().getPlayers().get(0).getGod(), one.getGod());
        assertEquals(Session.getInstance().getPlayers().get(1).getGod(), two.getGod());
        Session.getInstance().removeGod(Gods.ATLAS);
        assertEquals(Session.getInstance().getPlayers().get(0).getGod(), one.getGod());
    }
}


