package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;

public class HeraRules extends EnemyRules implements Serializable {

    private static final long serialVersionUID = 7066204499633555911L;

    /**
     * Apply the static condition of the check on enemy wins
     */
    public HeraRules() {
        applyEffect();
    }

    /**
     * Changes the win condition for the enemy, if the {@link Position position}
     * of the winning is boundary the opponent can't win
     *
     * @param position position where the worker moves
     * @return {@code true} if the position is not boundary, else returns {@code false}
     */
    @Override
    public boolean consentEnemyWin(Position position) {
        return !(position.isBoundary());
    }

}
