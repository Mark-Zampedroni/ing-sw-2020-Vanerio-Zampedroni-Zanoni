package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.BuildGodPowerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
//event is true if it's the 2nd building action
public class HephaestusRules extends EventRule {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (getEvent() && !position.equals(getPos())) {
            throw new BuildGodPowerException("Not Allowed");
        }
    }
}