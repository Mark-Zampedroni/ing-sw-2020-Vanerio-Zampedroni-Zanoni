package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.CommonRules;

import java.io.Serializable;

/**
 * Rules for a player with Pan as God
 */
public class PanRules extends CommonRules implements Serializable {
    private static final long serialVersionUID = -4382896954451533930L;

    /**
     * Adds a new win condition
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker {@link it.polimi.ingsw.mvc.model.player.Player master} can win the game
     */
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
                Session.getInstance().getBoard().getTile(oldPosition).getHeight() >= Session.getInstance().getBoard().getTile(position).getHeight() + 2);
    }
}
