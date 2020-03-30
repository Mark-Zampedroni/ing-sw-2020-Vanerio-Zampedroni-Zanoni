package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.CommonRules;

public class PanRules extends CommonRules {
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
               Board.getTile(worker.getPosition()).getHeight() >= Board.getTile(position).getHeight() +2  );
    }
}
