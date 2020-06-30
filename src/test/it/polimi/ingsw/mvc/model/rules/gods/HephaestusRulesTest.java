package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HephaestusRulesTest {
    private Player player;
    private Worker worker;
    private final HephaestusRules test = (HephaestusRules) GodRules.getInstance(Gods.HEPHAESTUS);

    @BeforeEach
    void setUp() {
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

    //Checks if the two different builds are correctly managed
    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> {
            assert test != null;
            test.consentBuild(worker, new Position(2,2));
        });
        assert test != null;
        test.setEvent(true);
        test.setPos(new Position(2,2));
        assertThrows(CantActException.class, ()->test.consentBuild(worker, new Position(2,1)));
    }

    //Checks if the build action works properly
    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        assert test != null;
        test.executeBuild(position);
        assertTrue(position.isSameAs(test.getPos()));
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(), 1);
    }

    //Checks if after the first build is possible a second build
    @Test
    void afterBuild(){
        assert test != null;
        List<Action> list = test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.get(1), Action.BUILD);
        assertEquals(list.size(),2);
        test.setEvent(true);
        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(), 1);
    }
}