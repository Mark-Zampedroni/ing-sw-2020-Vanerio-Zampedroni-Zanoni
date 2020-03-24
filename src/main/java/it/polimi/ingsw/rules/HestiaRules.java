package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.BuildGodPowerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
//Event is true during the additional turn
public class HestiaRules extends EventRule {
    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if(getEvent() && position.isBoundary())
        {
            throw new BuildGodPowerException("Can't build on perimeter space");
        }
    }
}
