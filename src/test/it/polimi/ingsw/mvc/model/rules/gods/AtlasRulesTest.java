package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.mvc.model.Session;

import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.map.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AtlasRulesTest {

    private Player player;
    private final AtlasRules test = (AtlasRules) GodRules.getInstance(Gods.ATLAS);

    @BeforeEach
    void setUp(){
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    //Checks if works properly the building of domes in different levels
    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        assert test != null;
        test.setEvent(false);
        test.executeBuild(position);
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(), 1);
        assertFalse(Session.getInstance().getBoard().getTile(position).hasDome());
        test.toggleSpecialPower();
        position = new Position (3,4);
        test.executeBuild(position);
        assertTrue(Session.getInstance().getBoard().getTile(position).hasDome());
    }

    //Checks if is correct the list of possible actions with his special power
    @Test
    void correctActionListAfterMove(){
        List <Action> actions = test.afterMove();
        assertTrue(actions.get(0).equals(Action.SPECIAL_POWER));
        assertTrue(actions.get(1).equals(Action.BUILD));

    }
}
