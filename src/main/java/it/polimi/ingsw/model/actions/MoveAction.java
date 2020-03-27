package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.movement.ClimbMoveException;
import it.polimi.ingsw.exceptions.actions.movement.DomeMoveException;
import it.polimi.ingsw.exceptions.actions.movement.MoveGodPowerException;
import it.polimi.ingsw.exceptions.actions.movement.MoveOutsideRangeException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.GodSharedRules;

import java.util.ArrayList;

public class MoveAction {

    public boolean flag=false;

    public ArrayList<ActionType> moveWorkerTo (Position position, Worker worker) {
        GodSharedRules rules = worker.getMaster().getRules();
        Position oldPosition = worker.getPosition();
        ArrayList <ActionType> nextAction = new ArrayList<>();
        try {
            rules.consentMovement(worker, position);
            nextAction = fixOthers(position, oldPosition, worker);
            worker.setPosition(position);
        } catch (CantMoveException e) {
            System.out.println("Move error, try again");
            nextAction.add(ActionType.MOVE);
        }
        return nextAction;
    }

    public ArrayList<ActionType> fixOthers(Position position, Position oldPosition, Worker worker){
        ArrayList<ActionType> actions = new ArrayList<>();
        actions.add(ActionType.BUILD);
        return actions;
    }
}
