package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZeusRulesTest {
    private Player player;
    private Worker worker;
    private Player opponent;
    private final ZeusRules test = (ZeusRules) GodRules.getInstance(Gods.ZEUS);


    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        opponent = Setupper.addPlayer("Opponent", Colors.WHITE,2);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(opponent);
    }

    //Checks if the worker can build under himself
    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> {
            assert test != null;
            test.consentBuild(worker, new Position(1,1));
        });
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () -> {
            assert test != null;
            test.consentBuild(worker, new Position(2,2));
        });
        opponent.getWorkers().get(0).setPosition(1,2);
        assertThrows(CantActException.class, () -> {
            assert test != null;
            test.consentBuild(worker, new Position(1,2));
        });
    }

    //Checks if the movement
    @Test
    void consentMovement() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> {
            assert test != null;
            test.consentMovement(worker, new Position(1,2));
        });
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () -> {
            assert test != null;
            test.consentMovement(worker, new Position(2,2));
        });
    }
}