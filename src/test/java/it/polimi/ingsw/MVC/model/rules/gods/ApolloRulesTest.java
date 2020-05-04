package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.MVC.model.Setupper;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.GodRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApolloRulesTest {
    Player player;
    Player opponent;
    Worker worker;
    GodRules test = GodRules.getInstance(Gods.APOLLO);

    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE, 1);
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


    @Test
    void consentMovement() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentMovement(worker, new Position(1,2)));
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentMovement(worker, new Position(2,2)));

    }

    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(1,2)));
        worker.setPosition(1,1);
        player.getWorkers().get(1).setPosition(2,2);
        assertThrows(CantActException.class, () ->test.consentBuild(worker, new Position(2,2)));

    }

    @Test
    void executeMove() {
        worker.setPosition(2,2);
        Position oldPosition = worker.getPosition();
        opponent.getWorkers().get(0).setPosition(2,3);
        Position position = opponent.getWorkers().get(0).getPosition();
        test.executeMove(worker, new Position(2,3));
        assertTrue(opponent.getWorkers().get(0).getPosition().equals(oldPosition));
        assertTrue(worker.getPosition().equals(position));
    }
}
