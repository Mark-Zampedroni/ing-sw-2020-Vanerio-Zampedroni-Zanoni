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

    static List<EnemyRules> enemyModifiers = new ArrayList<>();

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
        List<Action> out = new ArrayList<>();
        out.add(Action.BUILD);
        return out;
    }

    public List<Action> afterBuild() {
        List<Action> out = new ArrayList<>();
        out.add(Action.END_TURN);
        return out;
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
        for(EnemyRules enemy : enemyModifiers) {
            if(this != enemy) {
            enemy.consentEnemyMovement(worker, position); }
        } // Only Athena atm
    }

    public boolean isWinner(Worker worker, Position position) {
        Board board = Session.getBoard();
        return ((board.getTile(worker.getPosition()).getHeight() == MID && board.getTile(position).getHeight() == TOP));
    }

    public boolean consentWin(Worker worker, Position position) {
        for(EnemyRules enemy : enemyModifiers) { // Only Hera atm
            if(this != enemy && !enemy.consentEnemyWin(position)) {
                return false;
            }
        }
        return isWinner(worker, position);
    }

    public boolean canSelect(Worker worker, List<Action> actions) {
        Position position = worker.getPosition();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (Action action : actions) {
                    try {
                        switch (action) {
                            case BUILD:
                                consentBuild(worker, new Position(position.getX() + x, position.getY() + y));
                                return true;
                            case MOVE:
                                consentMovement(worker, new Position(position.getX() + x, position.getY() + y));
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

    /*
        Holder to enable clear call from any GodRules child
     */
    public void clear() { /* Do nothing */ }

}

        /*CONTROLLO UN'AZIONE NEL FUTURO DOPO SELEZIONE WORKER, SE LISTE VUOTE BLOCCO SELEZIONE WORKER

        PER ALTRE AZIONI CONTROLLO POSSIBILITA' INTORNO CON I CONSENT, SE LISTE VUOTE PER BUILD E MOVE E NO END_TURN O SELECT_WORKER
        ALLORA PERDE GAME. ALTRIMENTI NELL'EXECUTE CONTROLLA CHE SIA NELLA LISTA CORRISPETTIVA.

        */