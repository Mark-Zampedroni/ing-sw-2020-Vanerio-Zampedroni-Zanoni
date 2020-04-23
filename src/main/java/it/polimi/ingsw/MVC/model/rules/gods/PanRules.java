package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.CommonRules;

public class PanRules extends CommonRules {
    /**
     * Method to evaluate if the {@link Worker worker}'s
     * {@link it.polimi.ingsw.MVC.model.player.Player master} wins the game
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.MVC.model.player.Player master} could win the game
     */
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
                Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() >= Session.getInstance().getBoard().getTile(position).getHeight() + 2);
    }
}
