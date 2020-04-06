package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;

public abstract class CommonRules extends GodRules {

    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.occupant(worker, position, Target.ANY);
        Check.distance(worker, position);
    }

    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.occupant(worker, position, Target.ANY);

    }

}