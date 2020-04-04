package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.EnemyRules;

public class Hera extends EnemyRules {

    public Hera() {
        applyEffect();
    }

    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
