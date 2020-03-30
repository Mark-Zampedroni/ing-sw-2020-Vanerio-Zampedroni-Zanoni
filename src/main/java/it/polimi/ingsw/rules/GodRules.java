package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.constants.Height.MID;
import static it.polimi.ingsw.constants.Height.TOP;

public abstract class GodRules {

    public void executeMove(Worker worker, Position position) {
        worker.setPosition(position);
    }

    public void executeBuild(Position position) {
        Tile tile = Board.getTile(position);
        tile.increaseHeight();
    }

    public List<Action> afterSelect() {
        return new ArrayList<>(Arrays.asList(Action.SELECT_WORKER, Action.MOVE));
    }

    public List<Action> afterMove() {
        List<Action> actions = new ArrayList<>();
        actions.add(Action.BUILD);
        return actions;
    }

    public List<Action> afterBuild() {
        List<Action> actions = new ArrayList<>();
        actions.add(Action.END_TURN);
        return actions;
    }

    public void consentSelect(Worker worker, List<Action> actions) throws CantActException {
        if (!isSelectable(worker, actions)) {
            throw new CantActException("This worker can't do any action");
        }
    }

    public void consentBuild(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
        Check.distance(worker, position);
        Check.dome(position);
    }

    public void consentMovement(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
        Check.distance(worker, position);
        Check.dome(position);
        Check.height(worker, position);
        // CHECK ATHENA BLOCK
    }

    public boolean isWinner(Worker worker, Position position) {
        return (Board.getTile(worker.getPosition()).getHeight() == MID && Board.getTile(position).getHeight() == TOP);
    }

    public boolean blockedByEnemy(Worker worker, Position position) {
        return false;
    }

    public boolean isSelectable(Worker worker, List<Action> actions) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (Action action : actions) {
                    try {
                        switch (action) {
                            case BUILD:
                                consentBuild(worker, new Position(worker.getPosition().getX() + i, worker.getPosition().getY() + j));
                                return true;
                            case MOVE:
                                consentMovement(worker, new Position(worker.getPosition().getX() + i, worker.getPosition().getY() + j));
                                return true;
                            default:
                                continue;
                        }
                    } catch (CantActException e) {
                        continue;
                    }
                }
            }
        }
        return false;
    }
}

/* QUESTI DUE METODI NEL CONTROLLER */

        /*
        public ArrayList<ActionType> buildTowerOn (Position position, Worker worker) {
            GodSharedRules rules = worker.getMaster().getRules();
            ArrayList <ActionType> nextAction = new ArrayList<>();
            try {
                rules.consentBuild(worker, position);
                nextAction = executeBuild (position, worker);
            }
            catch (CantActException e) {
                System.out.println("Build error, try again");
                nextAction.add(ActionType.BUILD);
            }
            return nextAction;
        }*/

        /*
        public ArrayList<ActionType> moveWorkerTo (Position position, Worker worker) {
            GodSharedRules rules = worker.getMaster().getRules();
            Position oldPosition = worker.getPosition();
            ArrayList <ActionType> nextAction = new ArrayList<>();
            try {
                rules.consentMovement(worker, position);
                nextAction = fixOthers(position, oldPosition, worker);
                worker.setPosition(position);
            } catch (CantActException e) {
                System.out.println("Move error, try again");
                nextAction.add(ActionType.MOVE);
            }
            return nextAction;
        }*/