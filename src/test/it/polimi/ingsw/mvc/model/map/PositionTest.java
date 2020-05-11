package it.polimi.ingsw.mvc.model.map;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

class PositionTest {

    Position position, that;

    @BeforeEach
    @AfterEach
    void setUp() {
        Session.getInstance().getBoard().clear();
    }

    @Test
    void isValid() {
        for(int x = 0; x<5; x++) {
            for(int y = 0; y<5; y++) {
                assertTrue((new Position(x,y)).isValid());
            }
        }
        for(int x = -1; x>-5; x--) {
            for(int y = -1; y>-5; y--) {
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
        Player player = new Player("TestName", Colors.BLUE);
        player.addWorker(new Position(0,0));
        player.addWorker(new Position(0,0));
        Session.getInstance().addPlayer(player);

        Worker worker = player.getWorkers().get(0);
        for(int x = 1; x < 4; x++) {
            for(int y = 0; y < 4; y++) {
                worker.setPosition(x,y);
                assertNull((new Position(x+1,y)).getWorker());
                assertNull((new Position(x,y+1)).getWorker());
                assertSame((new Position(x,y)).getWorker(),worker);
            }
        }
        Session.getInstance().removePlayer(player);
    }

    @Test
    void isBoundary() {
        assertTrue((new Position(0,1)).isBoundary());
        assertFalse((new Position(1,1)).isBoundary());
    }

    // toString test
    @Test
    void testToString() {
        for(int x = 0; x < 5;x++) {
            for (int y = 0; y < 5; y++) {
                assertEquals("("+x+","+y+")",(new Position(x,y).toString()));
            }
        }
    }

    // Checks the correct return of equals() within the Board boundaries
    @Test
    void equals() {
        for(int x = 0; x < 5;x++) {
            for(int y = 0; y < 5; y++) {
                for(int tx = 0; tx < 5; tx++) {
                    for (int ty = 0; ty < 5; ty++) {
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