package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ApolloRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AthenaRulesTest {

    Player player, player2;
    Worker worker, worker2;

    @BeforeEach
    void setUp() {
        player = new Player("TestName1");
        player2 = new Player("TestName2");
        Session.addPlayer(player);
        Session.addPlayer(player2);
        worker = player.getWorkers().get(0);
        worker2 = player2.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
        Session.removePlayer(player2);
    }

    @Test
    void applyEffect() {
        player.setRules(Gods.create(Gods.ATHENA));
        player2.setRules(Gods.create(Gods.APOLLO));

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

    @Test
    void winCondition() {
        player.setRules(Gods.create(Gods.ATHENA));
        player2.setRules(Gods.create(Gods.APOLLO));

        player2.getRules().executeBuild(new Position(0,1)); // Height 1
        player2.getRules().executeBuild(new Position(1,0)); // Height 1
        player2.getRules().executeBuild(new Position(2,0)); // Height 1
        player2.getRules().executeBuild(new Position(0,2)); // Height 1
        player2.getRules().executeBuild(new Position(0,2)); // Height 2
        player2.getRules().executeBuild(new Position(0,3)); // Height 1
        player2.getRules().executeBuild(new Position(0,3)); // Height 2
        player2.getRules().executeBuild(new Position(0,3)); // Height 3

        player.getRules().executeMove(worker, new Position(1,0)); // Athena activates effect
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,2))); // Height 2
        player2.getRules().executeMove(worker2, new Position(0,2)); // Height 2
        assertTrue(player2.getRules().consentWin(worker2, new Position(0,3))); // Height 3
        player.getRules().clear();
    }

}