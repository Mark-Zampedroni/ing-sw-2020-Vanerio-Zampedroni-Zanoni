package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.BuildGodPowerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
// event is true when additional building turn has been occurred

public class DemeterRules extends EventRule {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (getEvent() && position.equals(getPos()))
        {
            throw new BuildGodPowerException("Not Allowed");
        }

    }

}