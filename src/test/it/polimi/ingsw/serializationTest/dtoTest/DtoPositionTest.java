package it.polimi.ingsw.serializationTest.dtoTest;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.enumerations.Colors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DtoPositionTest {

    Position position;

    @BeforeEach
    void setUp(){
        Session.getInstance().addPlayer("Paolo", Colors.BLUE);
        Session.getInstance().getPlayers().get(0).addWorker(new Position(2,3));
        this.position= new Position(2,3);
    }

    @AfterEach
    void clear() {
        Setupper.removePlayer(Session.getInstance().getPlayerByName("Paolo"));
        position = null;
    }

    //Tests if the creation of a DTOposition works, testing all the getters
    @Test
    void correctlyCreated(){
        DtoPosition dtOposition = new DtoPosition(position);
        assertEquals(position.getX(), dtOposition.getX());
        assertEquals(position.getY(), dtOposition.getY());
        assertEquals(position.isBoundary(), dtOposition.isBoundary());
        assertEquals(position.isValid(), dtOposition.isValid());
    }

    //Tests if distanceFrom works
    @Test
    void correctDistanceFrom(){
        DtoPosition dtOposition = new DtoPosition(position);
        Position position2 = new Position(2,3);
        DtoPosition dtoPosition2 = new DtoPosition(position2);
    }


    //Tests if equals methods works
    @Test
    void correctlyRelated() {
        DtoPosition dtOposition = new DtoPosition(position);
        Position position2 = new Position(2, 3);
        DtoPosition dtoPosition2 = new DtoPosition(position2);
        assertTrue(dtOposition.isSameAs(dtoPosition2));
        assertTrue(position.isSameAs(position2));
        assertTrue(dtOposition.isSameAs(position));
    }


    //Tests if is correct toString method
    @Test
    void correctWrite() {
        DtoPosition dtOposition = new DtoPosition(position);
        assertEquals (dtOposition.toString(), position.toString());
    }

}
