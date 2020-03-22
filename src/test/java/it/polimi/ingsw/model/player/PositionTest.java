package it.polimi.ingsw.model.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position position;

    @BeforeEach
    void setUp() {
        position = new Position(2,2);
    }

    @Test
    public void positionIsValid() {
        assertTrue(position.isValid());
        System.out.println("Position "+position+" is within (5,5)");
    }

    @Test
    public void positionIsNotValid() {
        assertFalse((new Position(6,5)).isValid());
    }

    @Test
    void getDistanceFrom() {
        for(int j = 0; j <= 2; j++) {
            for(int i = -2+j; i <= 2-j; i++) {
                assertEquals(position.getDistanceFrom(new Position(position.getX()+i,0+j)), 2-j);
                assertEquals(position.getDistanceFrom(new Position(0+j,position.getY()+i)), 2-j);
            }
        }
    }
}