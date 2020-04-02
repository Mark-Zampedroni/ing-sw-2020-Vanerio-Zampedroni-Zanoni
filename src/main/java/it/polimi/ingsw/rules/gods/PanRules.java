package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.CommonRules;

public class PanRules extends CommonRules {
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
                Session.getBoard().getTile(worker.getPosition()).getHeight() >= Session.getBoard().getTile(position).getHeight() + 2);
    }
}
