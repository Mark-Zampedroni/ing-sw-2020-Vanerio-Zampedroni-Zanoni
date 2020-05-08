package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//non ho idea del perchè mi dia il 92% di coverage

class PrometheusRulesTest {
    private Player player;
    private Worker worker;
    private final PrometheusRules test = (PrometheusRules) GodRules.getInstance(Gods.PROMETHEUS);

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


    @Test
    void consentMovement() {
        worker.setPosition(new Position(2,2));
        assertDoesNotThrow(() -> {
            assert test != null;
            test.consentBuild(worker, new Position(3,2));
        });
        worker.setPosition(3,2);
        assert test != null;
        test.setEvent(true);
        Session.getInstance().getBoard().getTile(new Position(3,3)).increaseHeight();
        assertThrows(CantActException.class, ()->test.consentMovement(worker, new Position(3,3)));
    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        assert test != null;
        test.setEvent(false);
        test.executeBuild(position);
        assertTrue(test.getEvent());
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(),1);
    }

    @Test
    void afterBuild(){
        assert test != null;
        test.setEvent(false);
        List<Action> list = test.afterBuild();
        assertEquals(list.get(0), Action.MOVE);
        assertEquals(list.size(),1);
        test.setEvent(true);
        list= test.afterBuild();
        assertEquals(list.get(0), Action.END_TURN);
        assertEquals(list.size(), 1);
    }
}