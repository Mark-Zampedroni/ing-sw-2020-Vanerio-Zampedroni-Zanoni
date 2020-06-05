package mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.rules.gods.DemeterRules;
import mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DemeterRulesTest {
    private Player player;
    private Worker worker;
    private final DemeterRules test = (DemeterRules) GodRules.getInstance(Gods.DEMETER);


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
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> {
            assert test != null;
            test.consentBuild(worker, new Position(2, 2));
        });
        assert test != null;
        test.setEvent(true);
        test.setPos(new Position(2, 2));
        assertThrows(CantActException.class, () -> test.consentBuild(worker, new Position(2, 2)));

    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        test.executeBuild(position);
        assertTrue(position.equals(test.getPos()));
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(),1);
    }

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