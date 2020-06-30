package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeraRulesTest {

    Player player, player2;
    Worker worker2;

    @BeforeEach
    void setUp() {
        player = Setupper.addPlayer("TestName1", Colors.BLUE,1);
        player2 = Setupper.addPlayer("TestName2", Colors.BROWN,2);
        worker2 = player2.getWorkers().get(0);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
        Setupper.removePlayer(player2);
    }

    //Checks if the effect of Hera is correctly applied on other players
    @Test
    void applyEffect() {
        player2.setRules(GodRules.getInstance(Gods.APOLLO)); // Any god
        player2.getRules().executeBuild(new Position(0,1)); //height 1
        player2.getRules().executeBuild(new Position(0,2));
        player2.getRules().executeBuild(new Position(0,2)); //height 2
        player2.getRules().executeBuild(new Position(0,3));
        player2.getRules().executeBuild(new Position(0,3));
        player2.getRules().executeBuild(new Position(0,3)); //height 3
        worker2.setPosition(0,1);
        player2.getRules().executeMove(worker2, new Position(0,2));
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,2)));
        player2.getRules().executeMove(worker2, new Position(0,3));
        assertTrue(player2.getRules().consentWin(worker2, new Position(0,3)));
        // Test Hera.consentWin block
        player.setRules(GodRules.getInstance(Gods.HERA));
        assertFalse(player2.getRules().consentWin(worker2, new Position(0,3)));
        worker2.setPosition(0,2);
        // Test consentEnemyPosition
        assertDoesNotThrow(()->player2.getRules().consentMovement(worker2, new Position(0,3)));
        player2.getRules().executeBuild(new Position(1,3));
        player2.getRules().executeBuild(new Position(1,3));
        player2.getRules().executeBuild(new Position(1,3));
        assertTrue(player2.getRules().consentWin(worker2, new Position(1,3)));

        (player.getRules()).removeEffect();
    }

}