package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Session;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;

class PositionTest {

    Position position, that;

    @Test
    void isValid() {
        for(int x = 0; x<6; x++) {
            for(int y = 0; y<6; y++) {
                assertTrue((new Position(x,y)).isValid());
            }
        }
        for(int x = -1; x>-6; x--) {
            for(int y = -1; y>-6; y--) {
                assertFalse((new Position(x,y)).isValid());
                assertFalse((new Position(x,y*(-1))).isValid());
                assertFalse((new Position(x*(-1),y)).isValid());
            }
        }
    }

    // Asserts the correct calculation of the distance between two positions
    @Test
    void getDistanceFrom() {
        position = new Position(2,2);
        for(int j = 0; j <= 2; j++) {
            for(int i = -2+j; i <= 2-j; i++) {
                assertEquals(position.getDistanceFrom(new Position(position.getX()+i,j)), 2-j);
                assertEquals(position.getDistanceFrom(new Position(j,position.getY()+i)), 2-j);
            }
        }
    }

    // Tests if Worker is correctly retrievable from Position
    @Test
    void getWorker() {
        Player player = new Player("TestName");
        Session.addPlayer(player);

        Worker worker = player.getWorkers().get(0);
        for(int x = 0; x < 6; x++) {
            for(int y = 0; y < 6; y++) {
                worker.setPosition(new Position(x,y));
                assertEquals((new Position(x,y)).getWorker(),worker);
                assertNull((new Position(x+1,y)).getWorker());
                assertNull((new Position(x,y+1)).getWorker());
            }
        }
    }

    // Checks if setValue correctly reassigns the coordinates
    @Test
    void setValue() {
        position = new Position(2,2);
        position.setValue(3,3);
        assertEquals(position.getX(),3);
        assertEquals(position.getY(),3);
    }

    // Confirms that the returned value is a copy of the object
    @Test
    void copy() {
        position = new Position(2,3);
        that = position.copy();
        assertTrue(that.equals(position));
        assertNotEquals(position,that);
    }

    // Checks the correct return of equals() within the Board boundaries
    @Test
    void equals() {
        for(int x = 0; x < 6;x++) {
            for(int y = 0; y < 6; y++) {
                for(int tx = 0; tx < 6; tx++) {
                    for (int ty = 0; ty < 6; ty++) {
                        position = new Position(x, y);
                        that = new Position(tx, ty);
                        if(x == tx && y == ty) {
                            assertTrue(position.equals(that));
                        }
                        else {
                            assertFalse(position.equals(that));
                        }
                    }
                }
            }
        }
    }
}