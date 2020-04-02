package it.polimi.ingsw.rules;

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
import static org.junit.jupiter.api.Assertions.*;

class PrometheusRulesTest {
    Player player;
    Worker worker;
    PrometheusRules test =new PrometheusRules();

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
}