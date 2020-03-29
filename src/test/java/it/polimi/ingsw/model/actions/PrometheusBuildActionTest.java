package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.rules.*;
import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

class PrometheusBuildActionTest {

    @Test
    void BuildTowerOn() {
        Position position = new Position(1, 2);
        Player player = new Player("Giuseppe");
        EventRule rules = new EventRule();
        player.setRules(rules);
        Worker worker = player.getWorkers().get(0);
        worker.setPosition(new Position(1, 1));
        BuildAction building = new PrometheusBuildAction();
        ArrayList<ActionType> nextAction = building.buildTowerOn(position, worker);
        assertEquals(Board.getTile(position).getHeight(), 1);
        assertEquals(ActionType.MOVE, nextAction.get(0));
        rules.setEvent(true);
        nextAction= building.buildTowerOn(position, worker);
        assertEquals(Board.getTile(position).getHeight(), 2);
        assertEquals(ActionType.END_TURN, nextAction.get(0));
        assertTrue(rules.getEvent());

        //wrong move
        position = new Position (4,4);
        nextAction = building.buildTowerOn(position, worker);
        assertEquals(nextAction.get(0), ActionType.BUILD);

        //building a dome
        position = new Position (1,0);
        rules.setEvent(false);
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        Board.getTile(position).increaseHeight();
        nextAction = building.buildTowerOn(position, worker);
        assertTrue(Board.getTile(position).hasDome());
        assertEquals(ActionType.MOVE, nextAction.get(0));
    }
}