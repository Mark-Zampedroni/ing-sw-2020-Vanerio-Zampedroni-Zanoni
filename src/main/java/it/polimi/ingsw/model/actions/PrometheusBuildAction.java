package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;

import java.util.ArrayList;

public class PrometheusBuildAction extends BuildAction {

    @Override
    public ArrayList<ActionType> executeBuild(Position position, Worker worker) {

        EventRule rules = (EventRule) worker.getMaster().getRules();
        ArrayList<ActionType> nextAction;
        nextAction = super.executeBuild(position, worker);
        if (!rules.getEvent()) {
            rules.setEvent(true);
            nextAction = new ArrayList<>();
            nextAction.add(ActionType.MOVE);}
            return nextAction;
    }
}

