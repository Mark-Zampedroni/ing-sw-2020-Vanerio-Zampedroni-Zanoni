package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;

import java.io.Serializable;

public abstract class EnemyRules extends CommonRules implements Serializable {

    private static final long serialVersionUID = 2360689823147364189L;

    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { /* Nothing on default */ }
    public boolean consentEnemyWin(Position position) { return true; /* Default */ }

    protected void applyEffect() {
        enemyModifiers.add(this);
    }

    @Override
    public void removeEffect() {
        enemyModifiers.remove(this);
    }

}
