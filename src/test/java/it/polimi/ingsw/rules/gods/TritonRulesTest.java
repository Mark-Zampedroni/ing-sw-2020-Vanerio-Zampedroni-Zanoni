package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.AtlasRules;
import it.polimi.ingsw.rules.gods.TritonRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TritonRulesTest {

    Player player;
    Worker worker;
    TritonRules test= (TritonRules) Gods.TRITON.createRules();

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

    @Test
    void executeMove() {
        worker.setPosition(0,1);
        Position position = new Position(0,2);
        test.executeMove(worker, position);
        assertTrue(position.equals(worker.getPosition()));
        assertTrue(test.getEvent());

        position = new Position (3,3);
        test.executeMove(worker, position);
        assertTrue(position.equals(worker.getPosition()));
        assertFalse(test.getEvent());
    }

    @Test
    void afterMove() {
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