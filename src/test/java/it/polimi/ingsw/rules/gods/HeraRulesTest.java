package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EnemyRules;
import it.polimi.ingsw.rules.GodRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeraRulesTest {

    Player player, player2;
    Worker worker, worker2;

    @BeforeEach
    void setUp() {
        player = Setupper.addPlayer("TestName1", Colors.BLUE,1);
        player2 = Setupper.addPlayer("TestName2", Colors.RED,2);
        worker = player.getWorkers().get(0);
        worker2 = player2.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(player2);
    }

    @Test
    void applyEffect() {
        player2.setRules(Gods.APOLLO.createRules()); // Any god
        player2.getRules().executeBuild(new Position(0,1));
        player2.getRules().executeBuild(new Position(0,2));
        player2.getRules().executeBuild(new Position(0,2));
        player2.getRules().executeBuild(new Position(0,3));
        player2.getRules().executeBuild(new Position(0,3));
        player2.getRules().executeBuild(new Position(0,3));
        player2.getRules().executeMove(worker2, new Position(0,2));
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,1)));
        assertTrue(player2.getRules().consentWin(worker2, new Position(0,3)));
        // Test Hera.consentWin block
        player.setRules(Gods.HERA.createRules());
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,3)));
        // Test consentEnemyPosition
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(0,3)));
        player2.getRules().executeMove(worker2, new Position(1,0));
        player.getRules().executeMove(worker2, new Position(0,2));
        assertTrue(player.getRules().consentWin(worker2, new Position(0,3)));

        ((EnemyRules)player.getRules()).removeEffect();
    }

}