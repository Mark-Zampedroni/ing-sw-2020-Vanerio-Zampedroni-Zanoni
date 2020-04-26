package it.polimi.ingsw.serializationTest.DTOtest;


import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.rules.gods.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.serialization.DTO.DTOplayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class DTOplayerTest {

    Player player;

    @BeforeEach
    void setUp(){
        this.player = Setupper.addPlayer("Paolo", Colors.BLUE, 1);
        player.setGod(Gods.ATLAS);
        player.setChallenger();
        player.setWinner();
        player.addWorker(new Position(1,2));
        player.addWorker(new Position(1,3));
    }

    @AfterEach
    void clear() {
        player.removeWorker(0);
        player.removeWorker(0);
        Setupper.removePlayer(player);
        player=null;
    }

    /**
     * Testing if the creation of a DTOplayer works, testing all the getters
     */
    @Test
    void correctlyCreated(){
        DTOplayer dtOplayer = new DTOplayer(player);
        assertTrue(dtOplayer.isWinner());
        assertEquals(dtOplayer.getColor(), Colors.BLUE);
        assertTrue(dtOplayer.isChallenger());
        assertTrue(dtOplayer.isWinner());
        assertEquals(dtOplayer.getUsername(), player.getUsername());
        assertEquals(dtOplayer.getGod(), Gods.ATLAS);
        assertEquals(dtOplayer.getWorkersSize(),player.getWorkersSize());
        assertEquals(dtOplayer.getWorkers().get(0).getPosition().getX(), player.getWorkers().get(0).getPosition().getX());
        assertEquals(dtOplayer.getWorkers().get(0).getPosition().getY(), player.getWorkers().get(0).getPosition().getY());
        assertEquals(dtOplayer.getWorkers().get(1).getPosition().getX(), player.getWorkers().get(1).getPosition().getX());
        assertEquals(dtOplayer.getWorkers().get(1).getPosition().getY(), player.getWorkers().get(1).getPosition().getY());
    }

}
