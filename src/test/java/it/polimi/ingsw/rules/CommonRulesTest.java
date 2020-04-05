package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
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
    CommonRules test= new CommonRules();
    Player player;
    Worker worker;
    Position position= new Position(1,1);
    List<Action> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
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
    void consentSelect() {
    }

    @Test
    void canSelect() {
        list.add(Action.BUILD);
        worker.setPosition(1, 1);
    /*    assertTrue(test.canSelect(worker, list));
        list.remove(Action.BUILD);
        list.add(Action.MOVE);
        assertTrue(test.canSelect(worker, list));
        list.add(Action.BUILD);*/
        list.add(Action.MOVE);

        for (int x = -1; x < 2 ; x++) {
            for (int y = -1; y < 2; y++) {
                if(x!=0 && y!=0) {
                    position.setValue(worker.getPosition().getX() + x, worker.getPosition().getY() + y);
                    for(int k=0; k<4; k++){Session.getBoard().getTile(position).increaseHeight();}

                }
            }
        }
        assertFalse(test.canSelect(worker,list));
    }

}