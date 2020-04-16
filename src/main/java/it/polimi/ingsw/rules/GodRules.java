package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.observer.observable.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.constants.Height.MID;
import static it.polimi.ingsw.constants.Height.TOP;

/**
 * Rules shared by all the gods
 */
public abstract class GodRules extends Observable implements Serializable {

    static final List<EnemyRules> enemyModifiers = new ArrayList<>();

    /**
     * Executes a movement {@link Action action}
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    public void executeMove(Worker worker, Position position) {
        worker.setPosition(position);
        //notify(worker, position);  serializeModel e mandarlo con notify
    }

    public void executeAdd(Player player, Position position) {
        player.addWorker(position);
    }

    public void consentAdd(Position position) throws CantActException {
        Check.dome(position);
        Check.positionValidity(position);
        Check.occupant(position);
    }

    /**
     * Executes a build {@link Action action}
     *
     * @param position {@link Position position} where to build
     */
    public void executeBuild(Position position) {
        Tile tile = Session.getInstance().getBoard().getTile(position);
        tile.increaseHeight();
        //notify(position);   serializeModel e mandarlo con notify
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action selected} a {@link Worker worker}
     *
     * @return list of {@link Action actions} that can be done after a worker {@link Action selection}
     */
    public List<Action> afterSelect() {
        return new ArrayList<>(Arrays.asList(Action.SELECT_WORKER, Action.MOVE));
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action moved} a {@link Worker worker}
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    public List<Action> afterMove() {
        List<Action> out = new ArrayList<>();
        out.add(Action.BUILD);
        return out;
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action built} with a {@link Worker worker}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    public List<Action> afterBuild() {
        List<Action> out = new ArrayList<>();
        out.add(Action.END_TURN);
        return out;
    }

    /**
     * Checks if by the rules it's physically possible to perform a select {@link Action action}
     *
     * @param worker worker the {@link it.polimi.ingsw.model.player.Player player} wants to select
     * @throws CantActException when the worker can't be selected
     */
    public void consentSelect(Worker worker) throws CantActException {
        if (!canSelect(worker, afterSelect())) {
            throw new CantActException("This worker can't do any action");
        }
    }

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action}
     *
     * @param worker worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    public void consentBuild(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
        Check.dome(position);
    }

    /**
     * Checks if by the rules it's physically possible to perform a move {@link Action action}
     *
     * @param worker worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    public void consentMovement(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
        Check.distance(worker, position);
        Check.dome(position);
        Check.height(worker, position);
        for(EnemyRules enemy : enemyModifiers) { // Only Athena atm
            if(this != enemy) {
            enemy.consentEnemyMovement(worker, position); }
        }
    }

    /**
     * Method to evaluate if the {@link Worker worker}'s
     * {@link it.polimi.ingsw.model.player.Player master} wins the game
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.model.player.Player master} could win the game
     */
    public boolean isWinner(Worker worker, Position position) {
        Board board = Session.getInstance().getBoard();
        return ((board.getTile(worker.getPosition()).getHeight() == MID && board.getTile(position).getHeight() == TOP));
    }

    /**
     * Checks if by the rules it's possible to win the game
     *
     * @param worker worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.model.player.Player master} wins the game
     */
    public boolean consentWin(Worker worker, Position position) {
        for(EnemyRules enemy : enemyModifiers) { // Only Hera atm
            if(this != enemy && !enemy.consentEnemyWin(position)) {
                return false;
            }
        }
        return isWinner(worker, position);
    }

    /**
     * States if the {@link Worker worker} can physically perform at least one
     * of the {@link Action actions} in list
     *
     * @param worker worker the {@link it.polimi.ingsw.model.player.Player player} may want to select
     * @param actions list of {@link Action actions} the worker may be required to perform
     * @return {@code true} if the worker could do at least one {@link Action action}
     */
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
                            default: // Do nothing
                        }
                    } catch (CantActException e) { /* Do nothing */ }
                }
            }
        }
        return false;
    }

    /**
     * Placeholder to make clear() callable from any {@link GodRules rules} set
     */
    public void clear() { /* Do nothing */ }
}