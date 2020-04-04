package it.polimi.ingsw.rules;

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

class EnemyRulesTest {

    Player player, player2;
    Worker worker, worker2;

    @BeforeEach
    void setUp() {
        player = new Player("TestName1");
        player2 = new Player("TestName2");
        Session.addPlayer(player);
        Session.addPlayer(player2);
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
        worker = player.getWorkers().get(0);

        player2.setRules(new ApolloRules());
        worker2 = player2.getWorkers().get(0);



        worker2.setPosition(2,2);
        player2.getRules().executeBuild(new Position(3,2));
        player2.getRules().executeBuild(new Position(2,3));
        player2.getRules().executeBuild(new Position(3,3));
        player2.getRules().executeBuild(new Position(3,3));
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(2,3))); // No Exception

        player.getRules().executeMove(worker, new Position(3,2)); // If Athena moves on a position that's higher, then:
        assertDoesNotThrow(()->player.getRules().consentMovement(worker, new Position(3,3))); // Athena does not block herself
        assertThrows(CantActException.class, ()->player2.getRules().consentMovement(worker2, new Position(2,3))); // Athena blocks enemy

        player.getRules().executeMove(worker, new Position(4,3)); // If Athena moves on a position that isn't higher, then:
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(2,3))); // Athena does not block enemy





    }

}