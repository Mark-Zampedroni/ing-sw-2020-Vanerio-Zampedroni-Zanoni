package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.CommonRules;

public class PanRules extends CommonRules {
    /**
     * Method to evaluate if the {@link Worker worker}'s
     * {@link it.polimi.ingsw.model.player.Player master} wins the game
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.model.player.Player master} could win the game
     */
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
                Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() >= Session.getInstance().getBoard().getTile(position).getHeight() + 2);
    }
}
