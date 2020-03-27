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
import it.polimi.ingsw.rules.MinotaurRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class MinotaurMoveActionTest {

    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(1, 2);
        Player player = new Player("Giuseppe");
        Player playerTwo = new Player("Giovanni");
        Session.addPlayer(player);
        Session.addPlayer(playerTwo);
        Worker opponentWorker = playerTwo.getWorkers().get(0);
        opponentWorker.setPosition(position);
        GodSharedRules rules = new MinotaurRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        Position oldPosition = new Position(1, 1);
        worker.setPosition(oldPosition);
        MoveAction moving = new MinotaurMoveAction();
        ArrayList<ActionType> nextAction = moving.moveWorkerTo(position, worker);
        System.out.println(worker.getPosition());
        System.out.println(opponentWorker.getPosition());
        //c'è un problema con il consent
        assertTrue(worker.getPosition().equals(position));
        Position opponentPosition = opponentWorker.getPosition();
        position.setValue(position.getX(), position.getY()+1);
        assertTrue(position.equals(opponentPosition));
        assertEquals(ActionType.BUILD, nextAction.get(0));

        //wrong move
        position = new Position(4, 4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);

        //other wrong move
        position = new Position(1, 3);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }
}