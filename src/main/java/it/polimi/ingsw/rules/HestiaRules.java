package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

//Event is true during the additional turn
public class HestiaRules extends EventRule {

    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {
        ArrayList<ActionType> nextAction = super.executeBuild(position, worker);
        if (!this.getEvent()) {
            nextAction.add(ActionType.BUILD);
            this.setEvent(true);
            this.setPos(position);
        }
        return nextAction;
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.boundary(position);
    }
}
