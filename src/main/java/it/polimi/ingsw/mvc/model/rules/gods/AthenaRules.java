package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.*;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;

import java.io.Serializable;

public class AthenaRules extends EnemyRules implements Serializable {

    private static final long serialVersionUID = -152466532092964491L;

    /**
     * Executes a {@link Action movement}, if the position height increases it enforces a new check on movement
     * to the other players
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} wants to move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        if(Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() < Session.getInstance().getBoard().getTile(position).getHeight()) {
            applyEffect();
        }
        super.executeMove(worker, position);
    }

    /**
     * Removes the enforced check
     */
    @Override
    public void clear() {
        removeEffect();
    }

    /**
     * Check enforced by {@link #executeMove(Worker worker, Position position) executeMove}
     *
     * @param position position where the worker has to move to
     * @param worker worker that performs the movement
     */
    @Override
    public void consentEnemyMovement(Worker worker, Position position) throws CantActException {
        Check.height(worker,position,0,"Athena blocks you");
    }
}