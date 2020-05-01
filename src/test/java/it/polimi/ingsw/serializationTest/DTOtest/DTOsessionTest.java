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

    /**
     * Testing if is correct toString method
     */
    @Test
    void correctWrite() {
        DTOsession dtOsession = new DTOsession(Session.getInstance());
        assertEquals (dtOsession.toString(), "(0,0) : Tower, height: 0, dome: false\n" +
                "(0,1) : Tower, height: 0, dome: false\n" +
                "(0,2) : Tower, height: 0, dome: false\n" +
                "(0,3) : Tower, height: 0, dome: false\n" +
                "(0,4) : Tower, height: 0, dome: false\n" +
                "(1,0) : Tower, height: 0, dome: false\n" +
                "(1,1) : Tower, height: 0, dome: false\n" +
                "(1,2) : Tower, height: 0, dome: false\n" +
                "(1,3) : Tower, height: 0, dome: false\n" +
                "(1,4) : Tower, height: 0, dome: false\n" +
                "(2,0) : Tower, height: 0, dome: false\n" +
                "(2,1) : Tower, height: 0, dome: false\n" +
                "(2,2) : Tower, height: 1, dome: false\n" +
                "(2,3) : Tower, height: 0, dome: false\n" +
                "(2,4) : Tower, height: 0, dome: false\n" +
                "(3,0) : Tower, height: 0, dome: false\n" +
                "(3,1) : Tower, height: 0, dome: false\n" +
                "(3,2) : Tower, height: 0, dome: false\n" +
                "(3,3) : Tower, height: 0, dome: false\n" +
                "(3,4) : Tower, height: 0, dome: false\n" +
                "(4,0) : Tower, height: 0, dome: false\n" +
                "(4,1) : Tower, height: 0, dome: false\n" +
                "(4,2) : Tower, height: 0, dome: false\n" +
                "(4,3) : Tower, height: 0, dome: false\n" +
                "(4,4) : Tower, height: 0, dome: false\n" +
                "Master: Piero , (-1,0)\n" +
                "Master: Piero , (0,-1)\n" +
                "Master: Sandro , (-2,0)\n" +
                "Master: Sandro , (0,-2)\n" +
                "Master: Sandro , (1,1)\n" +
                "Master: Carmelo , (-3,0)\n" +
                "Master: Carmelo , (0,-3)\n" +
                "Master: Carmelo , (2,3)\n");
    }

}
