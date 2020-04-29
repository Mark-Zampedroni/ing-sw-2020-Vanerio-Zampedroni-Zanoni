package it.polimi.ingsw.serializationTest.DTOtest;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.rules.gods.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;

import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DTOsessionTest {

    Player one, two, three;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        one = Setupper.addPlayer("Piero", Colors.BLUE,1);
        two = Setupper.addPlayer ("Sandro", Colors.WHITE,2);
        three = Setupper.addPlayer ("Carmelo", Colors.GREEN,3);
        three.addWorker(new Position(2,3));
        two.addWorker(new Position(1,1));
        Session.getInstance().getBoard().getTile(new Position (2,2)).increaseHeight();
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
     * Testing if all is copied correctly
     */
    @Test
    void correctlyCreated (){
        Session.getInstance().getChallenger();
        DTOsession dtOsession= new DTOsession(Session.getInstance());
        assertEquals(dtOsession.getBoard().getTile(new DTOposition(new Position(2,2))).getHeight(), Session.getInstance().getBoard().getTile(new Position(2,2)).getHeight());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getPosition().getX(), dtOsession.getWorkers().get(2).getPosition().getX());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getPosition().getY(), dtOsession.getWorkers().get(2).getPosition().getY());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getMaster().getUsername(), dtOsession.getWorkers().get(2).getMasterUsername());
    }

}
