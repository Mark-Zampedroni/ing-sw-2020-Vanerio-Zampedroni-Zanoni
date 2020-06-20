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
     * Executes a movement {@link Action action}, method with the
     * {@link Worker worker} {@link Position oldPosition} argument,
     * if increase her height set the malus to other players with {@link #applyEffect() applyEffect} method
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        if(Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() < Session.getInstance().getBoard().getTile(position).getHeight()) {
            applyEffect();
        }
        super.executeMove(worker, position);
    }

    /**
     * Remove the malus to the other players
     */
    @Override
    public void clear() {
        removeEffect();
    }

    /**
     * Checks if other players move in correct way after the application of the malus
     *
     * @param position position where the worker have to move to
     * @param worker worker that perform the movement
     */
    @Override
    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { Check.height(worker,position,0,"Athena blocks you"); }
}