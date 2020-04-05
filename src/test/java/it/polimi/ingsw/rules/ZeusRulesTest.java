package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ZeusRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeusRulesTest {
    Player player;
    Worker worker;
    ZeusRules test = new ZeusRules();


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
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(1,1)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentBuild(worker, new Position(2,2)));
    }

    @Test
    void consentMovement() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(1,2)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentMovement(worker, new Position(2,2)));
    }
}