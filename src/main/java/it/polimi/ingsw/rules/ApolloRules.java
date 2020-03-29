package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class ApolloRules extends GodSharedRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantActException {
        super.consentMovement(worker, position);
        Check.occupant(worker, position, Target.ALLY);
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.occupant(worker, position, Target.ANY);
    }
}
