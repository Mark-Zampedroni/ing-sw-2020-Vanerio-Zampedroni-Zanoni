package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Position;
import it.polimi.ingsw.model.Worker;

public class PrometheusRules extends GodRules {
    private boolean PreBuilding; //True if worker has build before moving
@Override
    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1 || !askEnemyConsent()) {
            throw new aException("Maximum moviment limit excedeed");
        } else if (!askEnemyConsent() || position.getWorker() != null) {
            throw new bException("This tile is alrealy occupied");
        } else if (!askEnemyConsent() || Board.getTile(position).hasDome()) {
            throw new cException("This tile is no longer available for movement");
        } else if (!askEnemyConsent() || Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1) {
            throw new dException("Unreachable tile from this position");
        } else if (PreBuilding && Board.getTile(worker.getPosition()).getHeight() < Board.getTile(position).getHeight()) {
            throw new PrometheusException("Not Allowed due to Prometheus' power");
        }
    }

    public void setPreBuilding(boolean build) { this.PreBuilding = build;}
}