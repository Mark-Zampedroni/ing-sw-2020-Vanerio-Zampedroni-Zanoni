package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.AthenaRules;
import it.polimi.ingsw.rules.EventRule;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.GodSharedRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class AthenaMoveActionTest {

    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(1,2);
        Player player = new Player ("Giuseppe");
        EventRule rules = new AthenaRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        worker.setPosition(new Position(1,1));
        MoveAction moving = new AthenaMoveAction();
        assertEquals(rules.getEvent(), false);
        ArrayList<ActionType> nextAction= moving.moveWorkerTo(position, worker);
        assertTrue(worker.getPosition().equals(position));
        assertEquals(ActionType.BUILD, nextAction.get(0));
        assertEquals(rules.getEvent(), false);

        //correct move with flag
        position = new Position (2,2);
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.BUILD);

        assertEquals(rules.getEvent(), true);

        //wrong move
        position = new Position (4,4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);


        //other wrong move
        position = new Position (2,3);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }
}