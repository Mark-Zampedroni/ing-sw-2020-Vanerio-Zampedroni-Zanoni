package it.polimi.ingsw.MVC.model.rules.gods;


import it.polimi.ingsw.MVC.model.Setupper;
import it.polimi.ingsw.MVC.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.Session;

import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AtlasRulesTest {

    Player player;
    Worker worker;
    AtlasRules test= (AtlasRules) GodRules.getInstance(Gods.ATLAS);

    @BeforeEach
    void setUp(){
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);
    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        test.setEvent(false);
        test.executeBuild(position);
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(), 1);
        assertFalse(Session.getInstance().getBoard().getTile(position).hasDome());
        test.setEvent(true);
        position = new Position (3,4);
        test.executeBuild(position);
        assertTrue(Session.getInstance().getBoard().getTile(position).hasDome());
    }
}
