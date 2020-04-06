package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonRulesTest {
    GodRules test;
    Player player;
    Worker worker;
    List<Action> list;

    @BeforeEach
    void setUp() {
        Session.getBoard().clear();
        player = new Player("TestName");
        Session.addPlayer(player);
        worker = player.getWorkers().get(0);
        test = Gods.APOLLO.createRules();
        player.setRules(test);
        list = new ArrayList<>();
    }

    @AfterEach
    void clearUp() {
        player.getRules().clear();
        Session.getBoard().clear();
        Session.removePlayer(player);

    }

    @Test
    void consentSelect() {
    }

    @Test
    void canSelect() {
        list.add(Action.BUILD);
        list.add(Action.MOVE);

        worker.setPosition(2, 2);

        assertTrue(test.canSelect(worker,list));
        assertDoesNotThrow(()->test.consentSelect(worker));
        for (int x = -1; x < 2 ; x++) {
            for (int y = -1; y < 2; y++) {
                if(!(x==0 && y==0)) {
                    for(int k=0; k<4; k++) {
                        Session.getBoard().getTile(new Position(2 + x, 2 + y)).increaseHeight();
                    }
                }
            }
        }
        assertFalse(test.canSelect(worker,list));
        assertThrows(CantActException.class, ()->test.consentSelect(worker));
    }

}