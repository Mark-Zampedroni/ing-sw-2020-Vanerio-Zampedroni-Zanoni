package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.Setupper;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
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
        Session.getInstance().getBoard().clear();
        player = Setupper.addPlayer("TestName", Colors.BLUE,1);
        worker = player.getWorkers().get(0);
        test = GodRules.getInstance(Gods.APOLLO);
        player.setRules(test);
        list = new ArrayList<>();
    }

    @AfterEach
    void clearUp() {
        player.getRules().clear();
        Session.getInstance().getBoard().clear();
        Setupper.removePlayer(player);

    }

    //Checks if the selection of workers works properly
    @Test
    void canSelect() {
        list.add(Action.BUILD);
        list.add(Action.MOVE);

        worker.setPosition(2, 2);

        assertTrue(test.consentSelect(worker, list));
        assertDoesNotThrow(() -> test.consentSelect(player.getUsername(), worker));
        for (int x = -1; x < 2 ; x++) {
            for (int y = -1; y < 2; y++) {
                if(!(x==0 && y==0)) {
                    for(int k=0; k<4; k++) {
                        Session.getInstance().getBoard().getTile(new Position(2 + x, 2 + y)).increaseHeight();
                    }
                }
            }
        }
        assertFalse(test.consentSelect(worker, list));
        assertThrows(CantActException.class, () -> test.consentSelect(player.getUsername(), worker));
    }

    //Checks the flag special power and the removing of effects
    @Test
    void checkConditions(){
        assertFalse(test.hasSpecialPower());
        test.clear();
        test.removeEffect();
    }

    //Checks if adding a worker works properly
    @Test
    void checkAddingWorker(){
        Position position = new Position(2,3);
        assertDoesNotThrow(()->test.consentAdd(position));
        test.executeAdd(player, position);
        position.getWorker().equals(player.getWorkers().get(0));
    }

}