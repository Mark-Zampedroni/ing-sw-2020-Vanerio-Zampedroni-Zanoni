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
        Session.getBoard().clear();
        one = new Player("Piero");
        two = new Player ("Sandro");
        three = new Player ("Carmelo");
        Session.addPlayer(one);
        Session.addPlayer(two);
        Session.addPlayer(three);
    }

    @AfterEach
    void clear() {
        Session.getBoard().clear();
        Session.removePlayer(one);
        Session.removePlayer(two);
        Session.removePlayer(three);
    }

    @Test
    void PlayerManagement (){
        assertEquals(3,Session.playersNumber());
        Session.pickChallenger();
        assertTrue((one.isChallenger() ^ two.isChallenger()) ^ three.isChallenger()); //xor
        ArrayList<Player> otherPlayer = Session.getOtherPlayers(one);
        assertEquals(two.getUsername(), otherPlayer.get(0).getUsername());
        assertEquals(three.getUsername(), otherPlayer.get(1).getUsername());
        Session.removePlayer(one);
        assertEquals(Session.playersNumber(), 2);
        Session.addPlayer(one);
        one.setWinner();
        assertTrue(Session.hasWinner());
    }

    @Test
    void GodsManagement() {
        one.setGod(Gods.APOLLO);
        Session.addGod(one.getGod());
        two.setGod(Gods.ATLAS);
        Session.addGod(two.getGod());
        Session.getGods();
        Session.getPlayers();
        assertEquals(Session.getPlayers().get(0).getGod(), one.getGod());
        assertEquals(Session.getPlayers().get(1).getGod(), two.getGod());
    }
}


