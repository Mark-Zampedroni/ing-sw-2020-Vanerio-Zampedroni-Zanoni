package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

import static it.polimi.ingsw.constants.Height.MID;
import static it.polimi.ingsw.constants.Height.TOP;

public class PanRules extends GodRules {
    @Override
    public boolean isWinner(Worker worker, Position position){
        return(Board.getTile(worker.getPosition()).getHeight()==MID && Board.getTile(position).getHeight()==TOP || Board.getTile(worker.getPosition()).getHeight() >= Board.getTile(position).getHeight() +2  );
    }
}
