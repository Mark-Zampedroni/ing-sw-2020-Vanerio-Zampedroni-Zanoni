package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.rules.EnemyRules;

public class HeraRules extends EnemyRules {

    public HeraRules() {
        applyEffect();
    }

    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
