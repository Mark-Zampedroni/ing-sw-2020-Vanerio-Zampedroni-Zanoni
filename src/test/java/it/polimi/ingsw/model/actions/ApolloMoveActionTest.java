package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.ApolloRules;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.GodSharedRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class ApolloMoveActionTest {

    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(1,2);
        Player player = new Player ("Giuseppe");
        Player playerTwo = new Player("Giovanni");
        Session.addPlayer(player);
        Session.addPlayer(playerTwo);
        Worker opponentWorker = playerTwo.getWorkers().get(0);
        opponentWorker.setPosition(position);
        GodSharedRules rules = new ApolloRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        Position oldPosition = new Position (1,1);
        worker.setPosition(oldPosition);
        MoveAction moving = new ApolloMoveAction();
        ArrayList<ActionType> nextAction= moving.moveWorkerTo(position, worker);
        assertTrue(worker.getPosition().equals(position));
        position= opponentWorker.getPosition();
        assertTrue(position.equals(oldPosition));
        assertEquals(ActionType.BUILD, nextAction.get(0));

        //wrong move
        position.setValue(4,4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }
}
