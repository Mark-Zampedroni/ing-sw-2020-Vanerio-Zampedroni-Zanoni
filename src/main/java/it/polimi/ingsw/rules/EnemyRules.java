package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;

public abstract class EnemyRules extends CommonRules {

    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { /* Nothing on default */ }
    public boolean consentEnemyWin(Position position) { return true; /* Default */ }

    protected void applyEffect() {
        enemyModifiers.add(this);
    }

    public void removeEffect() {
        enemyModifiers.remove(this);
    }

}
