package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//non ho idea del perchè mi dia il 92% di coverage

class PoseidonRulesTest {
    private Player player;
    private Worker worker;
    private final PoseidonRules test = (PoseidonRules) GodRules.getInstance(Gods.POSEIDON);

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        worker.setPosition(0,0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    @Test
    void executeMove() {
        Worker worker2 = player.getWorkers().get(1);
        worker.setPosition(1,1);
        worker2.setPosition(0,0);
        assert test != null;
        test.executeMove(worker, new Position(1,2));
        assertEquals(test.getMovedWorker(), worker);
        assertDoesNotThrow(()->test.consentSelect(player.getUsername(),worker2));

        test.executeMove(worker2, new Position(3,4));
        assertEquals(test.getMovedWorker(), worker2);
        assertDoesNotThrow(()->test.consentSelect(player.getUsername(),worker));
    }

    @Test
    void consentSelect() {
        Worker worker1 = player.getWorkers().get(1);
        worker.setPosition(new Position(2,2));
        worker1.setPosition(3,4);
        assert test != null;
        test.setEvent(false);
        assertDoesNotThrow(()->test.consentSelect(player.getUsername(),worker));
        test.setEvent(true);
        test.executeMove(worker, new Position(2,3));
        assertThrows(CantActException.class, ()->test.consentSelect(player.getUsername(),worker));
        assertDoesNotThrow(()->test.consentSelect(player.getUsername(),worker1));
    }

    @Test
    void afterBuild(){

        //not correct position for second worker and multibuild
        Position position = new Position(3,3);
        Session.getInstance().getBoard().getTile(position).increaseHeight();
        assert test != null;
        test.executeMove(worker, new Position(2,3));
        Worker worker1 = player.getWorkers().get(1);
        worker1.setPosition(position);
        test.setEvent(false);
        List<Action> list =  test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
        assertFalse(test.getEvent());

        //correct position for multibuild, firstbuild
        position = new Position(2,2);
        worker1.setPosition(position);

        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.SELECT_WORKER);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());

        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());


        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());


        list = test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
        assertTrue(test.getEvent());

    }

}