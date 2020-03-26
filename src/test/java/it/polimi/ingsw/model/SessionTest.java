package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;


class SessionTest {

    @Test
    void PlayerManagement (){
        assertEquals(Session.playersNumber(), 0);
        Player playerOne = new Player("Piero");
        Player playerTwo = new Player ("Sandro");
        Player playerThree = new Player ("Carmelo");
        Session.addPlayer(playerOne);
        assertEquals(Session.playersNumber(), 1);
        Session.addPlayer(playerTwo);
        assertEquals(Session.playersNumber(), 2);
        Session.addPlayer(playerThree);
        assertEquals(Session.playersNumber(), 3);
        Session.pickChallenger();
        assertTrue(playerOne.isChallenger()||playerTwo.isChallenger() || playerThree.isChallenger());
        ArrayList<Player> otherPlayer = Session.getOtherPlayers(playerOne);
        assertEquals(playerTwo.getUsername(), otherPlayer.get(0).getUsername());
        assertEquals(playerThree.getUsername(), otherPlayer.get(1).getUsername());
        Session.removePlayer(playerOne);
        assertEquals(Session.playersNumber(), 2);
        System.out.println(playerThree.toString()+ playerTwo.toString());
        Session.addPlayer(playerOne);
        playerOne.setWinner();
        assertTrue(Session.hasWinner());
    }

    @Test
    void GodsManagement() {
        Player playerOne = new Player("Piero");
        Player playerTwo = new Player ("Sandro");
        Player playerThree = new Player ("Carmelo");
        Session.addPlayer(playerOne);
        playerOne.setGod(Gods.APOLLO);
        Session.addGod(playerOne.getGod());
        Session.addPlayer(playerTwo);
        playerTwo.setGod(Gods.ATLAS);
        Session.addGod(playerTwo.getGod());
        Session.getGods();
        Session.getPlayers();
        assertEquals(Session.getPlayers().get(0).getGod(), playerOne.getGod());
        assertEquals(Session.getPlayers().get(1).getGod(), playerTwo.getGod());
    }
}


