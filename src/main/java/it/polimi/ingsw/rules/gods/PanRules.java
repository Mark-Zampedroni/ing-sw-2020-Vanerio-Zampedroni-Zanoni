package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import static it.polimi.ingsw.constants.Height.MID;
import static it.polimi.ingsw.constants.Height.TOP;

public class PanRules extends GodRules {
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(super.isWinner(worker,position) ||
               Board.getTile(worker.getPosition()).getHeight() >= Board.getTile(position).getHeight() +2  );
    }
}
