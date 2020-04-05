package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.PoseidonRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//non ho idea del perchè mi dia il 92% di coverage

class PoseidonRulesTest {
    Player player;
    Worker worker;
    PoseidonRules test =new PoseidonRules();

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
    void setUnmovedWorker() {
        Worker worker2 = player.getWorkers().get(1);
        test.setMovedWorker(worker);
        test.setUnmovedWorker();
        assertEquals(test.getUnmovedWorker(),worker2);
        assertEquals(test.getMovedWorker(), worker);
        test.setMovedWorker(worker2);
        test.setUnmovedWorker();
        assertEquals(test.getUnmovedWorker(),worker);
        assertEquals(test.getMovedWorker(), worker2);
    }

    @Test
    void executeMove() {
        Worker worker2 = player.getWorkers().get(1);
        worker.setPosition(1,1);
        test.executeMove(worker, new Position(1,2));
        assertTrue(worker.getPosition().equals(new Position(1,2)));
        assertEquals(test.getUnmovedWorker(), worker2);
        assertEquals(test.getMovedWorker(), worker);
    }

    @Test
    void consentSelect() {
        Worker worker1 = player.getWorkers().get(1);
        worker.setPosition(new Position(2,2));
        worker1.setPosition(3,4);
        test.setEvent(false);
        assertDoesNotThrow(()->test.consentSelect(worker));
        test.setEvent(true);
        test.setMovedWorker(worker);
        test.setUnmovedWorker();
        assertThrows(CantActException.class, ()->test.consentSelect(worker));
        assertDoesNotThrow(()->test.consentSelect(worker1));
    }

    @Test
    void afterBuild(){

        //not correct position for second worker and multibuild
        Position position = new Position(3,3);
        Session.getBoard().getTile(position).increaseHeight();
        test.executeMove(worker, new Position(2,3));
        test.getUnmovedWorker().setPosition(position);
        test.setEvent(false);
        List<Action> list =  test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
        assertFalse(test.getEvent());

        //correct position for multibuild, firstbuild
        position = new Position(2,2);
        test.getUnmovedWorker().setPosition(position);
        test.clearCounter();
        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.SELECT_WORKER);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());
        assertEquals(test.getCounter(),0);

        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());
        assertEquals(test.getCounter(),1);

        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(), 2);
        assertTrue(test.getEvent());
        assertEquals(test.getCounter(),2);

        list = test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
        assertTrue(test.getEvent());
        assertEquals(test.getCounter(),0);
    }

}