package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.map.Position;
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
        Tile tile = Session.getBoard().getTile(position);
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

    public void consentSelect(Worker worker) throws CantActException {
        if (!canSelect(worker, afterSelect())) {
            throw new CantActException("This worker can't do any action");
        }
    }

    public void consentBuild(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
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
        Board board = Session.getBoard();
        return (board.getTile(worker.getPosition()).getHeight() == MID && board.getTile(position).getHeight() == TOP);
    }

    public void consentEnemy(Worker worker, Position position) throws CantActException {
        // Do nothing
    }

    public boolean canSelect(Worker worker, List<Action> actions) {
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
                                // Do nothing
                        }
                    } catch (CantActException e) { /* Do nothing */ }
                }
            }
        }
        return false;
    }
}

        /*CONTROLLO UN'AZIONE NEL FUTURO DOPO SELEZIONE WORKER, SE LISTE VUOTE BLOCCO SELEZIONE WORKER

        PER ALTRE AZIONI CONTROLLO POSSIBILITA' INTORNO CON I CONSENT, SE LISTE VUOTE PER BUILD E MOVE E NO END_TURN O SELECT_WORKER
        ALLORA PERDE GAME. ALTRIMENTI NELL'EXECUTE CONTROLLA CHE SIA NELLA LISTA CORRISPETTIVA.

        */