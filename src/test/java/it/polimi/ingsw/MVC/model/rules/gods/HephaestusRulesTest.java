package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.MVC.model.Setupper;
import it.polimi.ingsw.MVC.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HephaestusRulesTest {
    Player player;
    Worker worker;
    HephaestusRules test = (HephaestusRules) GodRules.getInstance(Gods.HEPHAESTUS);

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
        assertDoesNotThrow(()->test.consentBuild(worker, new Position(2,2)));
        test.setEvent(true);
        test.setPos(new Position(2,2));
        assertThrows(CantActException.class, ()->test.consentBuild(worker, new Position(2,1)));
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