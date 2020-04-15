package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Gods;
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
    Player opponent;
    ZeusRules test = (ZeusRules) Gods.ZEUS.createRules();


    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = new Player("TestName");
        opponent = new Player("Opponent");
        Session.getInstance().addPlayer(player);
        Session.getInstance().addPlayer(opponent);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Session.getInstance().removePlayer(player);
        Session.getInstance().removePlayer(opponent);
    }

    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(1,1)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentBuild(worker, new Position(2,2)));
        opponent.getWorkers().get(0).setPosition(1,2);
        assertThrows(CantActException.class, () ->test.consentBuild(worker, new Position(1,2)));
    }

    @Test
    void consentMovement() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(1,2)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentMovement(worker, new Position(2,2)));
    }
}