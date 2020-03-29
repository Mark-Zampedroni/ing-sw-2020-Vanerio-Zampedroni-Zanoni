package it.polimi.ingsw.rules;

import it.polimi.ingsw.constants.Height;
import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.CantMoveException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.exceptions.actions.movement.ClimbMoveException;
import it.polimi.ingsw.exceptions.actions.movement.DomeMoveException;
import it.polimi.ingsw.exceptions.actions.movement.MoveGodPowerException;
import it.polimi.ingsw.exceptions.actions.movement.MoveOutsideRangeException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

import static it.polimi.ingsw.constants.Height.MID;
import static it.polimi.ingsw.constants.Height.TOP;

public abstract class GodSharedRules {


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


        public ArrayList<ActionType> buildTowerOn (Position position, Worker worker) {
            GodSharedRules rules = worker.getMaster().getRules();
            ArrayList <ActionType> nextAction = new ArrayList<>();
            try {
                rules.consentBuild(worker, position);
                nextAction = executeBuild (position, worker);
            }
            catch (CantBuildException e) {
                System.out.println("Build error, try again");
                nextAction.add(ActionType.BUILD);
            }
            return nextAction;
        }

        public ArrayList<ActionType> executeBuild(Position position, Worker worker) {
            ArrayList <ActionType> nextAction = new ArrayList<>();
            Tile tile = Board.getTile(position);
            if (tile.getHeight() == Height.TOP)
            {   tile.placeDome();
            } else {
                tile.increaseHeight();
            }
            nextAction.add(ActionType.END_TURN);
            return nextAction;
        }


    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        if (worker.getPosition().getDistanceFrom(position) != 1) {
            throw new OutOfReachException("Maximum building limit exceeded");
        } else if (Board.getTile(position).hasDome()) {
            throw new AlreadyCompleteException("This tile is no longer available for building");
        }
    }

    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if (worker.getPosition().getDistanceFrom(position) != 1) {
            throw new MoveOutsideRangeException("Maximum movement limit exceeded");
        } else if (Board.getTile(position).hasDome()) {
            throw new DomeMoveException("This tile is no longer available for movement");
        } else if (Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() + 1) {
            throw new ClimbMoveException("Unreachable tile from this position");
        } else if(blockedByEnemy(worker, position)){
            throw new MoveGodPowerException("Not allowed due to Athena's power");
        }
    }
    public boolean isWinner(Worker worker, Position position) {
        return(Board.getTile(worker.getPosition()).getHeight()==MID && Board.getTile(position).getHeight()==TOP);
    }

    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false;
    }
}