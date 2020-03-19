package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.EnemyConsentException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class PrometheusRules extends GodRules {
    private boolean PreBuilding; //True if worker has build before moving

    @Override

    public void consentMovement(Worker worker, Position position) throws CantMoveException, EnemyConsentException {
        super.consentMovement(worker, position);
        if (PreBuilding && Board.getTile(worker.getPosition()).getHeight() < Board.getTile(position).getHeight()) {
            throw new PrometheusException("Not Allowed due to Prometheus' power");
        }
    }
        public void setPreBuilding( boolean build)
        {
            this.PreBuilding = build;
        }

}