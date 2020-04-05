package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.PrometheusRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//non ho idea del perchè mi dia il 92% di coverage

class PrometheusRulesTest {
    Player player;
    Worker worker;
    PrometheusRules test = (PrometheusRules)Gods.create(Gods.PROMETHEUS);

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
        worker.setPosition(new Position(2,2));
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(3,2)));
        worker.setPosition(3,2);
        test.setEvent(true);
        Session.getBoard().getTile(new Position(3,3)).increaseHeight();
        assertThrows(CantActException.class, ()->test.consentMovement(worker, new Position(3,3)));
    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        test.setEvent(false);
        test.executeBuild(position);
        assertTrue(test.getEvent());
        assertEquals(Session.getBoard().getTile(position).getHeight(),1);
    }

    @Test
    void afterBuild(){
        test.setEvent(false);
        List<Action> list = test.afterBuild();
        assertEquals(list.get(0), Action.MOVE);
        assertEquals(list.size(),1);
        test.setEvent(true);
        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(), 1);
    }
}