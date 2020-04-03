package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.AtlasRules;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AtlasRulesTest {

    Player player;
    Worker worker;
    AtlasRules test= new AtlasRules();

    @BeforeEach
    void setUp(){
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        player.setRules(test);
    }

    @AfterEach
    void clearUp() {
        Session.getBoard().clear();
        Session.removePlayer(player);
    }

    @Test
    void executeBuild() {
        Position position = new Position(1,2);
        test.setEvent(false);
        test.executeBuild(position);
        assertEquals(Session.getBoard().getTile(position).getHeight(), 1);
        assertFalse(Session.getBoard().getTile(position).hasDome());
        test.setEvent(true);
        position = new Position (3,4);
        test.executeBuild(position);
        assertTrue(Session.getBoard().getTile(position).hasDome());
    }
}
