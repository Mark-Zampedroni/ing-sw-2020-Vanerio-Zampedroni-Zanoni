package mvc.model;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.mvc.model.player.Player;
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
    Session session;

    @BeforeEach
    void setUp() {
        session = Session.getInstance();
        session.getBoard().clear();
        one = Setupper.addPlayer("Piero", Colors.BLUE,1);
        two = Setupper.addPlayer ("Sandro", Colors.WHITE,2);
        three = Setupper.addPlayer ("Carmelo", Colors.BROWN,3);
        session.setStarted(true);
        assertTrue(session.isStarted());
    }

    @AfterEach
    void clear() {
        session.getBoard().clear();
        Setupper.removePlayer(one);
        Setupper.removePlayer(two);
        Setupper.removePlayer(three);
    }

    @Test
    void PlayerManagement (){
        assertEquals(3,session.playersNumber());
        session.getChallenger();
        session.getChallenger();
        assertTrue((one.isChallenger() ^ two.isChallenger()) ^ three.isChallenger()); //xor
        ArrayList<Player> otherPlayer = session.getOtherPlayers(one);
        assertEquals(two.getUsername(), otherPlayer.get(0).getUsername());
        assertEquals(three.getUsername(), otherPlayer.get(1).getUsername());
        session.removePlayer(one);
        assertEquals(session.playersNumber(), 2);
        session.addPlayer(one);
        assertFalse(session.hasWinner());
        one.setStarter();
        assertTrue(session.hasWinner());
        two.removeWorker(0);
        session.removePlayer(two);
        assertEquals(session.playersNumber(), 2);
        two = new Player("Sandro", Colors.BLUE);
        session.addPlayer(two);
        session.addPlayer("Luca", Colors.WHITE);
        assertEquals(session.getPlayerColor("Luca"), Colors.WHITE);
        assertNull(session.getPlayerByName("Marco"));
        session.removePlayer("Luca");
    }

    @Test
    void GodsManagement() {
        one.setGod(Gods.APOLLO);
        two.setGod(Gods.ATLAS);
        session.getPlayers();
        assertEquals(session.getPlayers().get(0).getGod(), one.getGod());
        assertEquals(session.getPlayers().get(1).getGod(), two.getGod());
        assertEquals(session.getPlayers().get(0).getGod(), one.getGod());

    }
}


