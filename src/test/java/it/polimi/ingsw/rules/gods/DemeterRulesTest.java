package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.DemeterRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class DemeterRulesTest {
    Player player;
    Worker worker;
    DemeterRules test= (DemeterRules) Gods.DEMETER.createRules();


    @BeforeEach
    void setUp() {
        Session.getInstance().getBoard().clear();
        player = new Player("TestName");
        Session.getInstance().addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getInstance().getBoard().clear();
        Session.getInstance().removePlayer(player);
    }


    @Test
    void consentBuild() {
        worker.setPosition(1,1);
        assertDoesNotThrow(() -> test.consentBuild(worker, new Position(2, 2)));
        test.setEvent(true);
        test.setPos(new Position(2, 2));
        assertThrows(CantActException.class, () -> test.consentBuild(worker, new Position(2, 2)));

    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        test.setEvent(false);
        test.executeBuild(position);
        assertTrue(test.getEvent());
        assertTrue(position.equals(test.getPos()));
        assertEquals(Session.getInstance().getBoard().getTile(position).getHeight(),1);
    }

    @Test
    void afterBuild(){
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