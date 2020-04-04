package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.GodRules;

public class ApolloRules extends GodRules {

    @Override
    public void executeMove(Worker worker, Position position) {
        if(position.getWorker() != null) {
            worker.switchPosition(position.getWorker());
        }
        else {
            super.executeMove(worker,position);
        }
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
        Check.distance(worker, position);
    }
}
