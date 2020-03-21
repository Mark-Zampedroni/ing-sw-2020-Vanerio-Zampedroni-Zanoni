package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.map.Board ;
import it.polimi.ingsw.model.player.*;

public class AthenaRules extends GodRules {
    //event is true if worker has moved up
    @Override
    public boolean blockedByEnemy(Worker worker, Position position)
    {
        if (getEvent()) {return (Board.getTile(worker.getPosition()).getHeight() > Board.getTile(position).getHeight());}
        else {return false;}
    }
}