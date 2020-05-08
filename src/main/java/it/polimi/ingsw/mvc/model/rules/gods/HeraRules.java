package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;

public class HeraRules extends EnemyRules {

    public HeraRules() {
        applyEffect();
    }

    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
