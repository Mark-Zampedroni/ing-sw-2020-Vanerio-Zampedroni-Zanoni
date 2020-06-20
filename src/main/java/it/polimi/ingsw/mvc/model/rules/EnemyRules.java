package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;

/**
 * Rules shared by the gods which apply extra checks to enemies
 */
public abstract class EnemyRules extends CommonRules implements Serializable {

    private static final long serialVersionUID = 2360689823147364189L;

    /**
     * Check enforced by {@link #executeMove(Worker worker, Position position) executeMove}
     *
     * @param position position where the worker has to move to
     * @param worker worker that perform the movement
     */
    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { /* Nothing on default */ }

    /**
     * Check enforced on the other players as an extra win condition
     *
     * @param position position where the worker has to move to
     */
    public boolean consentEnemyWin(Position position) {
        return true; /* Default */
    }

    /**
     * Applies the check to the enemy
     */
    protected void applyEffect() {
        getEnemyModifiers().add(this);
    }

    /**
     * Removes the check from the enemy
     */
    @Override
    public void removeEffect() {
        getEnemyModifiers().remove(this);
    }

}
