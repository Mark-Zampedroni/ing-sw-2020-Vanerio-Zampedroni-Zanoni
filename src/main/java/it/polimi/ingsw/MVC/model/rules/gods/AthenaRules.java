package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.*;
import it.polimi.ingsw.MVC.model.rules.Check;
import it.polimi.ingsw.MVC.model.rules.EnemyRules;

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