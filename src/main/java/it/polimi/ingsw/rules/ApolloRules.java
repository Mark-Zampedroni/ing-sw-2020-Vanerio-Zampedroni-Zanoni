package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.movement.MoveOnAllyWorkerException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class ApolloRules extends GodSharedRules {
    @Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        super.consentMovement(worker, position);
        if (position.getWorker()!= null && worker.getMaster() == position.getWorker().getMaster()) {
            throw new MoveOnAllyWorkerException("Same player");
        }
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (position.getWorker() != null) {
            throw new AlreadyOccupiedException("This tile is already occupied");
        }
    }
}
