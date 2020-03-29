package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.GodSharedRules;

import java.util.ArrayList;

public class ApolloRules extends GodSharedRules {

    @Override
    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker) {
        position.getWorker().setPosition(oldPosition);
        return super.fixOthers(position, oldPosition, worker);
    }

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
