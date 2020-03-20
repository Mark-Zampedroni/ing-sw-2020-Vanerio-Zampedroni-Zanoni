package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.DemeterException;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;
// event is true when additional building turn has been occurred

public class DemeterRules extends EventRule {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (!getEvent())
        {
            setPos(position);
        }
        else
        {
            setEvent(false);
            if (position.equals(getPos()))
            {
                throw new DemeterException("Not Allowed");
            }
        }

    }

}