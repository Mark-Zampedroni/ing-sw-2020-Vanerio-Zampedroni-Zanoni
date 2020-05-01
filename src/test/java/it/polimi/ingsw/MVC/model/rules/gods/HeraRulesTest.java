package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.MVC.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.EnemyRules;
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
        player2 = Setupper.addPlayer("TestName2", Colors.BROWN,2);
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
        player2.setRules(GodRules.getInstance(Gods.APOLLO)); // Any god
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
        player.setRules(GodRules.getInstance(Gods.HERA));
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,3)));
        // Test consentEnemyPosition
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(0,3)));
        player2.getRules().executeMove(worker2, new Position(1,0));
        player.getRules().executeMove(worker2, new Position(0,2));
        assertTrue(player.getRules().consentWin(worker2, new Position(0,3)));

        ((EnemyRules)player.getRules()).removeEffect();
    }

}