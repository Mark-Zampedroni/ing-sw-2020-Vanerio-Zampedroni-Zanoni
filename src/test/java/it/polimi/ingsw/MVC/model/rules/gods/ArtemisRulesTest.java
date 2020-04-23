package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ArtemisRulesTest {
    Player player;
    Worker worker;
    ArtemisRules test= (ArtemisRules) Gods.ARTEMIS.createRules();

    @BeforeEach
    void setUp() {
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