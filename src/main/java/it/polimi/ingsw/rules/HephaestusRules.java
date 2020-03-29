package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.BuildGodPowerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

//event is true if it's the 2nd building action
public class HephaestusRules extends EventRule {

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
    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (getEvent() && !position.equals(getPos())) {
            throw new BuildGodPowerException("Not Allowed");
        }
    }
}