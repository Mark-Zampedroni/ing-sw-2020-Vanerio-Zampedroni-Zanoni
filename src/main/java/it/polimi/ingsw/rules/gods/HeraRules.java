package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.EnemyRules;

public class HeraRules extends EnemyRules {

    public HeraRules() {
        applyEffect();
    }

    @Override
    public void clear() {
        removeEffect();
    }

    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
