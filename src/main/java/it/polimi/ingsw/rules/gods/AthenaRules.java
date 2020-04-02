package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;

public class AthenaRules extends EventRules {
    //event is true if worker has moved up

    @Override
    public void consentEnemy(Worker worker, Position position) throws CantActException {
        Check.height(worker,position,0,"Athena block you");
    }
}