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

class AthenaRulesTest {

    Player player, player2;
    Worker worker, worker2;

    @BeforeEach
    void setUp() {
        player = Setupper.addPlayer("TestName1", Colors.BLUE,1);
        player2 = Setupper.addPlayer("TestName2", Colors.BROWN,2);
        worker = player.getWorkers().get(0);
        worker.setPosition(0,0);
        worker2 = player2.getWorkers().get(0);
        worker2.setPosition(4,4);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(player2);
    }

    //Checks if Athena power works on another player
    @Test
    void applyEffect() {
        player.setRules(GodRules.getInstance(Gods.ATHENA));
        player2.setRules(GodRules.getInstance(Gods.APOLLO));

        worker2.setPosition(2,2);
        player2.getRules().executeBuild(new Position(3,2)); // Height 1 (Athena second position)
        player2.getRules().executeBuild(new Position(2,3)); // Height 1
        player2.getRules().executeBuild(new Position(3,3));
        player2.getRules().executeBuild(new Position(3,3)); // Height 2
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(2,3))); // No Exception

        player.getRules().executeMove(worker, new Position(3,2)); // If Athena moves on a position that's higher, then:
        assertDoesNotThrow(()->player.getRules().consentMovement(worker, new Position(3,3))); // Athena does not block herself
        assertThrows(CantActException.class, ()->player2.getRules().consentMovement(worker2, new Position(2,3))); // Athena blocks enemy
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(2,1))); // Athena blocks enemy

        player.getRules().clear();
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(2,3))); // Athena does not block enemy
    }

    //Checks if Athena effect works also when a player wins
    @Test
    void winCondition() {
        player.setRules(GodRules.getInstance(Gods.ATHENA));
        player2.setRules(GodRules.getInstance(Gods.APOLLO));

        player2.getRules().executeBuild(new Position(0,1)); // Height 1
        player2.getRules().executeBuild(new Position(1,0)); // Height 1
        player2.getRules().executeBuild(new Position(2,0)); // Height 1
        player2.getRules().executeBuild(new Position(0,2)); // Height 1
        player2.getRules().executeBuild(new Position(0,2)); // Height 2
        player2.getRules().executeBuild(new Position(0,3)); // Height 1
        player2.getRules().executeBuild(new Position(0,3)); // Height 2
        player2.getRules().executeBuild(new Position(0,3)); // Height 3

        player.getRules().executeMove(worker, new Position(1,0)); // Athena activates effect
        player2.getRules().executeMove(worker2, new Position(0,2));
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,2))); // Height 2
        player2.getRules().executeMove(worker2, new Position(0,2)); // Height 2
        assertTrue(player2.getRules().consentWin(worker2, new Position(0,3))); // Height 3
        player.getRules().clear();
    }

}