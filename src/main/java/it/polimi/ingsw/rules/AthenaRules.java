package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class AthenaRules extends EventRule {
    //event is true if worker has moved up
    @Override
    public boolean blockedByEnemy(Worker worker, Position position)
    {
        if (getEvent()) {return (Board.getTile(worker.getPosition()).getHeight() > Board.getTile(position).getHeight());}
        else {return false;}
    }
}