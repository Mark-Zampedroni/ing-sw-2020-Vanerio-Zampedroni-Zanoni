package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRules;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.gods.ArtemisRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ArtemisRulesTest {
    Player player;
    Worker worker;
    ArtemisRules test= (ArtemisRules) Gods.ARTEMIS.createRules();

    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }


    @Test
    void consentMovement() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(2,2)));
        test.setEvent(true);
        test.setPos(new Position(1,1));
        assertThrows(CantActException.class, ()->test.consentMovement(worker, new Position(1,1)));
        test.clear();

    }

    @Test
    void afterMove(){
        List<Action> list = test.afterMove();
        assertEquals(list.get(0), Action.BUILD);
        assertEquals(list.get(1), Action.MOVE);
        assertEquals(list.size(),2);
        test.setEvent(true);
        list = test.afterMove();
        assertEquals(list.get(0), Action.BUILD);
        assertEquals(list.size(),1);
    }

    @Test
    void executeMove(){
        //correct double move
        worker.setPosition(1,1);
        Position oldPosition = worker.getPosition();
        test.executeMove(worker, new Position(1,2));
        assertTrue(test.getEvent());
        assertTrue(oldPosition.equals(test.getPos()));
        test.executeMove(worker, new Position(1,3));
        assertTrue(worker.getPosition().equals(new Position(1,3)));
    }
}