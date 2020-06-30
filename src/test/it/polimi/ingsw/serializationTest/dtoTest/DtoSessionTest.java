package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Colors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class DtoSessionTest {

    Player one, two, three;

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        one = Setupper.addPlayer("Piero", Colors.BLUE,1);
        two = Setupper.addPlayer ("Sandro", Colors.WHITE,2);
        three = Setupper.addPlayer ("Carmelo", Colors.BROWN,3);
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

    //Tests if all is copied correctly
    @Test
    void correctlyCreated (){
        Session.getInstance().getChallenger();
        DtoSession dtOsession= new DtoSession(Session.getInstance());
        assertEquals(dtOsession.getBoard().getTile(new DtoPosition(new Position(2,2))).getHeight(), Session.getInstance().getBoard().getTile(new Position(2,2)).getHeight());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getPosition().getX(), dtOsession.getWorkers().get(2).getPosition().getX());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getPosition().getY(), dtOsession.getWorkers().get(2).getPosition().getY());
        assertEquals(Session.getInstance().getPlayers().get(1).getWorkers().get(0).getMaster().getUsername(), dtOsession.getWorkers().get(2).getMasterUsername());
    }

}
