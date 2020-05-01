package it.polimi.ingsw.serializationTest.DTOtest;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.rules.gods.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.serialization.DTO.DTOposition;
import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DTOpositionTest {

    Position position;

    @BeforeEach
    void setUp(){
        Session.getInstance().addPlayer("Paolo", Colors.BLUE);
        Session.getInstance().getPlayers().get(0).addWorker(new Position(2,3));
        this.position= new Position(2,3);
    }

    @AfterEach
    void clear() {
        Setupper.removePlayer(Session.getInstance().getPlayers().get(0));
        position = null;
    }

    /**
     * Testing if the creation of a DTOposition works, testing all the getters
     */
    @Test
    void correctlyCreated(){
        DTOposition dtOposition = new DTOposition(position);
        assertEquals(position.getX(), dtOposition.getX());
        assertEquals(position.getY(), dtOposition.getY());
        assertEquals(position.isBoundary(), dtOposition.isBoundary());
        assertEquals(position.isValid(), dtOposition.isValid());
    }

    /**
     * Testing if distanceFrom works
     */
    @Test
    void correctDistanceFrom(){
        DTOposition dtOposition = new DTOposition(position);
        Position position2 = new Position(2,3);
        DTOposition dtOposition2 = new DTOposition(position2);
        assertEquals(dtOposition.getDistanceFrom(dtOposition2), position.getDistanceFrom(position2));
    }

    /**
     * Testing if equals methods works
     */
    @Test
    void correctlyRelated(){
        DTOposition dtOposition = new DTOposition(position);
        Position position2 = new Position(2,3);
        DTOposition dtOposition2 = new DTOposition(position2);
        assertTrue(dtOposition.equals(dtOposition2));
        assertTrue(position.equals(position2));
        assertTrue(dtOposition.equals(position));
    }

    /**
     * Testing if is correct toString method
     */
    @Test
    void correctWrite() {
        DTOposition dtOposition = new DTOposition(position);
        assertEquals (dtOposition.toString(), position.toString());
    }

}
