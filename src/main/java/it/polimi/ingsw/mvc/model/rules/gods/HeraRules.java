package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;

import java.io.Serializable;

public class HeraRules extends EnemyRules implements Serializable {

    private static final long serialVersionUID = 7066204499633555911L;

    public HeraRules() {
        applyEffect();
    }

    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
