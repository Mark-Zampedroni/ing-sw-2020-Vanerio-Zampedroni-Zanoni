package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.gods.ArtemisRules;
import it.polimi.ingsw.rules.GodSharedRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class ArthemisMoveActionTest {

    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(1,2);
        Player player = new Player ("Giuseppe");
        Session.addPlayer(player);
        GodSharedRules rules = new ArtemisRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        Position oldPosition = new Position (1,1);
        worker.setPosition(oldPosition);
        MoveAction moving = new ArthemisMoveAction();
        ArrayList<ActionType> nextAction= moving.moveWorkerTo(position, worker);
        assertTrue(worker.getPosition().equals(position));
        assertEquals(ActionType.MOVE, nextAction.get(0));
        assertEquals(ActionType.BUILD, nextAction.get(1));
        nextAction= moving.moveWorkerTo(new Position(1,3), worker);
        assertEquals(ActionType.BUILD, nextAction.get(0));
        assertEquals(nextAction.size(),1);

        //wrong move
        position = new Position(4,4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);

        //other wrong move
        position = new Position(1,3);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }
}