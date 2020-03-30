package it.polimi.ingsw.model.actions;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class MoveActionTest {

    /*
    @Test
    void moveWorkerTo() {
        //correct move
        Position position = new Position(1,2);
        Player player = new Player ("Giuseppe");
        GodRules rules = new GodRules();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        worker.setPosition(new Position(1,1));
        MoveAction moving = new MoveAction();
        ArrayList<ActionType> nextAction= moving.moveWorkerTo(position, worker);
        assertTrue(worker.getPosition().equals(position));
        assertEquals(ActionType.BUILD, nextAction.get(0));

        //wrong move
        position = new Position (4,4);
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);

        //other wrong move
        position = new Position (1,3);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = moving.moveWorkerTo(position, worker);
        assertEquals(nextAction.get(0), ActionType.MOVE);
    }*/
}
