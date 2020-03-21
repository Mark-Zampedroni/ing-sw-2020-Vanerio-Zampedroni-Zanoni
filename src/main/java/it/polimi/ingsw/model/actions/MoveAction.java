package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.GodRules;

public class MoveAction {

    public void moveWorkerTo (Position position, Worker worker) {
        GodRules rules = worker.getMaster().getRules();
        try {
            rules.consentMovement(worker, position);
            worker.setPosition(position);
        } catch (CantMoveException e) {
            System.out.println("Move error, try again");
        }
    }
}
