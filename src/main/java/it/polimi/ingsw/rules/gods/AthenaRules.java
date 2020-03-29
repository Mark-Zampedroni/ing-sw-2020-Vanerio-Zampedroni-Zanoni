package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.EventRule;

public class AthenaRules extends EventRule {
    //event is true if worker has moved up
    @Override
    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false; // DA FARE
    }
}