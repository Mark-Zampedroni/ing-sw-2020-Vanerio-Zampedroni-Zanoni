package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TritonRulesTest {

    private Player player;
    private Worker worker;
    private final TritonRules test = (TritonRules) GodRules.getInstance(Gods.TRITON);

    @BeforeEach
    void setUp(){
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    //Tests a double movement
    @Test
    void executeMove() {
        worker.setPosition(0,1);
        Position position = new Position(0,2);
        assert test != null;
        test.executeMove(worker, position);
        assertTrue(position.isSameAs(worker.getPosition()));
        assertTrue(test.getEvent());

        position = new Position (3,3);
        test.executeMove(worker, position);
        assertTrue(position.isSameAs(worker.getPosition()));
        assertFalse(test.getEvent());
    }

    //Checks the possible actions after a generic move and a move on a boundary position
    @Test
    void afterMove() {
        assert test != null;
        test.setEvent(false);
        List<Action> list = test.afterMove();
        assertEquals(list.get(0), Action.BUILD);
        assertEquals(list.size(),1);
        test.setEvent(true);
        list= test.afterMove();
        assertEquals(list.get(0), Action.BUILD);
        assertEquals(list.get(1), Action.MOVE);
        assertEquals(list.size(),2);
    }
}