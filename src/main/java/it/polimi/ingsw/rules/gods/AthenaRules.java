package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.EventRules;

public class AthenaRules extends EventRules {
    //event is true if worker has moved up
    @Override
    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false; // DA FARE
    }
}