package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EnemyRules;
import it.polimi.ingsw.rules.EventRules;

public class AthenaRules extends EnemyRules {

    @Override
    public void executeMove(Worker worker, Position position) {
        if(Session.getBoard().getTile(worker.getPosition()).getHeight() < Session.getBoard().getTile(position).getHeight()) {
            applyEffect();
        }
        worker.setPosition(position);
    }

    @Override
    public void clear() {
        removeEffect();
    }

    @Override
    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { Check.height(worker,position,0,"Athena blocks you"); }
}