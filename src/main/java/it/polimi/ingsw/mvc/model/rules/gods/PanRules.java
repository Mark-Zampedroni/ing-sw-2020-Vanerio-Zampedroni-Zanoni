package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.CommonRules;

import java.io.Serializable;

public class PanRules extends CommonRules implements Serializable {
    private static final long serialVersionUID = -4382896954451533930L;

    /**
     * Method to evaluate if the {@link Worker worker}'s
     * {@link it.polimi.ingsw.mvc.model.player.Player master} wins the game
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.mvc.model.player.Player master} could win the game
     */
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
                Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() >= Session.getInstance().getBoard().getTile(position).getHeight() + 2);
    }
}
