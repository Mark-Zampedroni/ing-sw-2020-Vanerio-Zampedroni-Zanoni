package it.polimi.ingsw.MVC.model.rules;

import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;

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
