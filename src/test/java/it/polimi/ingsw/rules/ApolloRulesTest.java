package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ApolloRules;
import it.polimi.ingsw.rules.gods.PrometheusRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApolloRulesTest {
    Player player;
    Worker worker;
    Player player2;
    Worker worker2;
    Position position;
    ApolloRules test = new ApolloRules();
    PrometheusRules test2 = new PrometheusRules();

    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
        player2 = new Player("Gianni");
        Session.addPlayer(player2);
        worker2 = player2.getWorkers().get(0);
        player2.setRules(test2);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
        Session.removePlayer(player2);

    }


    @Test
    void consentMovement() {

        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(1,2)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentMovement(worker, new Position(2,2)));
        position= new Position(2,1);
        worker2.setPosition(position);
        assertDoesNotThrow(()->test.consentMovement(worker, position));


    }

    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(1,2)));
        worker.setPosition(1,1);
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentBuild(worker, new Position(2,2)));

    }
}