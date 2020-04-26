package it.polimi.ingsw.serializationTest.DTOtest;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.rules.gods.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.serialization.DTO.DTOboard;
import it.polimi.ingsw.utility.serialization.DTO.DTOplayer;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class DTOsessionTest {

    Player one, two, three;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        one = Setupper.addPlayer("Piero", Colors.BLUE,1);
        two = Setupper.addPlayer ("Sandro", Colors.WHITE,2);
        three = Setupper.addPlayer ("Carmelo", Colors.GREEN,3);
        Session.getInstance().setStarted(true);
    }

    @AfterEach
    void clear() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(one);
        Setupper.removePlayer(two);
        Setupper.removePlayer(three);
    }

    /**
     * Testing if all the players are correctly copied
     */
    @Test
    void PlayerManagement (){
        Session.getInstance().getChallenger();
        DTOsession dtOsession= new DTOsession(Session.getInstance());
        assertEquals(dtOsession.playersNumber(),Session.getInstance().playersNumber());
        assertEquals(DTOsession.getInstance(), dtOsession);
        assertEquals(dtOsession.isStarted(), Session.getInstance().isStarted());
        assertEquals(dtOsession.getPlayerColor("Piero"), Session.getInstance().getPlayerColor("Piero"));
        assertEquals(dtOsession.getPlayers().get(0).toString(), (new DTOplayer(Session.getInstance().getPlayers().get(0))).toString());
        assertEquals(dtOsession.hasWinner(), Session.getInstance().hasWinner());
        assertEquals(dtOsession.getChallenger(), Session.getInstance().getChallenger());
        assertEquals(dtOsession.getOtherPlayers(dtOsession.getPlayers().get(0)).get(0).toString(), (new DTOplayer(Session.getInstance().getOtherPlayers(one).get(0))).toString());
        assertEquals((new DTOplayer(Session.getInstance().getPlayerByName("Piero"))).toString(), dtOsession.getPlayerByName("Piero").toString());
    }

    /**
     * Testing if the gods are correctly copied
     */
    @Test
    void GodsManagement() {
        one.setGod(Gods.APOLLO);
        Session.getInstance().addGod(one.getGod());
        two.setGod(Gods.ATLAS);
        Session.getInstance().addGod(two.getGod());
        DTOsession dtOsession2= new DTOsession(Session.getInstance());

        assertEquals(Session.getInstance().getGods().get(0), dtOsession2.getGods().get(0));
        assertEquals(Session.getInstance().getGods().get(1), dtOsession2.getGods().get(1));

    }
}
