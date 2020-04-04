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
        test.setMaster(player);
        test.setUnmovedWorker();
        assertEquals(test.getUnmovedWorker(),worker2);
        assertEquals(test.getMovedWorker(), worker);
        test.setMovedWorker(worker2);
        test.setMaster(player);
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
        assertEquals(test.getUnmovedWorker(),worker2);
        assertEquals(test.getMovedWorker(), worker);
        assertEquals(worker.getMaster(), test.getMaster());
    }

    @Test
    void consentMovement() {
        Worker worker1 = player.getWorkers().get(1);
        worker.setPosition(new Position(2,2));
        worker1.setPosition(3,4);
        test.setMultiBuild(false);
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(3,2)));
        test.setMultiBuild(true);
        test.setMovedWorker(worker);
        test.setMaster(player);
        test.setUnmovedWorker();
        assertThrows(CantActException.class, ()->test.consentBuild(worker, new Position(3,3)));
        assertDoesNotThrow(()->test.consentBuild(worker1, new Position(3,3)));
    }

    @Test
    void executeBuild() {
        worker.setPosition(1,1);
        Position position = new Position(1,2);
        test.setMultiBuild(false);
        test.setMaster(player);
        test.setMovedWorker(worker);
        test.setUnmovedWorker();
        test.executeBuild(position);
        assertTrue(test.getMultiBuild());
        assertEquals(Session.getBoard().getTile(position).getHeight(),1);
        test.executeBuild(position);
        assertEquals(test.getCounter(),1);
        assertEquals(Session.getBoard().getTile(position).getHeight(),2);
        test.executeBuild(position);
        assertEquals(test.getCounter(),2);
        assertEquals(Session.getBoard().getTile(position).getHeight(),3);
        test.setMultiBuild(false);
        test.clearCounter();
        assertEquals(test.getCounter(),0);
        test.executeBuild(position);
        assertTrue(Session.getBoard().getTile(position).hasDome());

    }

    @Test
    void afterBuild(){
        test.setMultiBuild(false);
        List<Action> list = test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
        test.setMultiBuild(true);
        test.clearCounter();
        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(), 2);
        test.increaseCounter();
        test.increaseCounter();
        test.increaseCounter();
        list = test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(),1);
    }

}