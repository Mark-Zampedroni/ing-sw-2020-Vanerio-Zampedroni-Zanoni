package it.polimi.ingsw.model.actions;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class TritonMoveActionTest {

    /*
    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(0,1);
        Player player = new Player ("Giuseppe");
        Session.addPlayer(player);
        GodRules rules = new GodRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        Position oldPosition = new Position (0,0);
        worker.setPosition(oldPosition);
        MoveAction moving = new TritonMoveAction();
        ArrayList<ActionType> nextAction= moving.moveWorkerTo(position, worker);
        assertTrue(worker.getPosition().equals(position));
        assertEquals(ActionType.BUILD, nextAction.get(0));
        assertEquals(ActionType.MOVE, nextAction.get(1));
        nextAction= moving.moveWorkerTo(new Position(1,1), worker);
        assertEquals(ActionType.BUILD, nextAction.get(0));
        assertEquals(nextAction.size(),1);

        //wrong move
        position = new Position(4,4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);

        //other wrong move
        position = new Position(1,2);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }*/
}
