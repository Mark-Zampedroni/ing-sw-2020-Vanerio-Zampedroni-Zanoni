package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.player.*;

public class AthenaRules extends EventRule {
    //event is true if worker has moved up
    @Override
    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false; // DA FARE
    }
}