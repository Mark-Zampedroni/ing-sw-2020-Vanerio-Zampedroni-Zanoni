package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.*;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;

public class AthenaRules extends EnemyRules {

    @Override
    public void executeMove(Worker worker, Position position) {
        if(Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() < Session.getInstance().getBoard().getTile(position).getHeight()) {
            applyEffect();
        }
        super.executeMove(worker, position);
    }

    @Override
    public void clear() {
        removeEffect();
    }

    @Override
    public void consentEnemyMovement(Worker worker, Position position) throws CantActException { Check.height(worker,position,0,"Athena blocks you"); }
}