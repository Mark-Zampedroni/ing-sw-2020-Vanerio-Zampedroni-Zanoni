package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;

import javax.swing.*;
import java.util.ArrayList;

//efesto, hestia e demetrio qui
public class DifferentBuildAction extends BuildAction {

    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {
        ArrayList<ActionType> nextAction = super.executeBuild(position, worker);
        nextAction.add(ActionType.BUILD);
        EventRule rules = (EventRule) worker.getMaster().getRules();
        rules.setEvent(true);
        rules.setPos(position);
        return nextAction;
    }
}
