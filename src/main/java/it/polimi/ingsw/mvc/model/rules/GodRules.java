package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.rules.gods.*;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.utility.observer.Observable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static it.polimi.ingsw.utility.constants.Height.MID;
import static it.polimi.ingsw.utility.constants.Height.TOP;

/**
 * Rules shared by all the gods
 */
public abstract class GodRules extends Observable<DtoSession> implements Serializable {


    private static final long serialVersionUID = 5054362736166633442L;
    protected Position oldPosition;



    /**
     * Getter for the list of {@link GodRules rules} that modify the conditions for the player
     * for different actions during the game
     *
     * @return the list of the {@link GodRules rules}
     */
    protected List<EnemyRules> getEnemyModifiers() {
        return Session.getInstance().getEnemyModifiers();
    }

    /**
     * Executes a movement {@link Action action}
     *
     * @param worker   selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    public void executeMove(Worker worker, Position position) {
        oldPosition = worker.getPosition();
        worker.setPosition(position);
        notify(new DtoSession(Session.getInstance()));
    }

    /**
     * Adds a worker of a player in a specific position of the board
     *
     * @param player {@link Player player} the player
     * @param position {@link Position position} the {@link Worker worker} will placed
     */
    public void executeAdd(Player player, Position position) {
        player.addWorker(position);
        notify(new DtoSession(Session.getInstance()));
    }

    /**
     * Checks if by the rules it's physically possible to add a {@link Worker worker} in that position
     *
     * @param position position the {@link it.polimi.ingsw.mvc.model.player.Player player} wants to select
     * @throws CantActException when the position can't be selected
     */
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
        notify(new DtoSession(Session.getInstance()));
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action selected} a {@link Worker worker}
     *
     * @return list of {@link Action actions} that can be done after a worker {@link Action selection}
     */
    public List<Action> afterSelect() {
        return new ArrayList<>(Arrays.asList(Action.SELECT_WORKER, Action.MOVE));
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
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
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
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
     * @param worker worker the {@link it.polimi.ingsw.mvc.model.player.Player player} wants to select
     * @param username player that is currently active
     * @throws CantActException when the worker can't be selected
     */
    public void consentSelect(String username, Worker worker) throws CantActException {
        if (!canSelect(worker, afterSelect()) || worker.getMaster() != Session.getInstance().getPlayerByName(username)) {
            throw new CantActException("This worker can't do any action");
        }
    }

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action}
     *
     * @param worker   worker that wants to build
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
     * @param worker   worker that wants to move
     * @param position position to where the worker is moved
     * @throws CantActException when the worker can't move
     */
    public void consentMovement(Worker worker, Position position) throws CantActException {
        Check.positionValidity(position);
        Check.distance(worker, position);
        Check.dome(position);
        Check.height(worker, position);
        for (EnemyRules enemy : getEnemyModifiers()) { // Only Athena atm
            if (this != enemy) {
                enemy.consentEnemyMovement(worker, position);
            }
        }
    }

    /**
     * Method to evaluate if the {@link Worker worker}'s
     * {@link it.polimi.ingsw.mvc.model.player.Player master} wins the game
     *
     * @param worker   worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.mvc.model.player.Player master} could win the game
     */
    public boolean isWinner(Worker worker, Position position) {
        Board board = Session.getInstance().getBoard();
        return ((board.getTile(oldPosition).getHeight() == MID && board.getTile(position).getHeight() == TOP));
    }

    /**
     * Checks if by the rules it's possible to win the game
     *
     * @param worker   worker that moves
     * @param position position to where the worker is moved
     * @return {@code true} if the worker's {@link it.polimi.ingsw.mvc.model.player.Player master} wins the game
     */
    public boolean consentWin(Worker worker, Position position) {
        for (EnemyRules enemy : getEnemyModifiers()) { // Only Hera atm
            if (this != enemy && !enemy.consentEnemyWin(position)) {
                return false;
            }
        }
        return isWinner(worker, position);
    }

    /**
     * States if the {@link Worker worker} can physically perform at least one
     * of the {@link Action actions} in list
     *
     * @param worker  worker the {@link it.polimi.ingsw.mvc.model.player.Player player} may want to select
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
     * Method for creating the rules of a specific {@link Gods god}
     *
     * @param god the god selected
     * @return an instance of the associated {@link GodRules rules}
     */
    public static GodRules getInstance(Gods god) {
        switch (god) {
            case APOLLO:
                return new ApolloRules();
            case ARTEMIS:
                return new ArtemisRules();
            case ATHENA:
                return new AthenaRules();
            case ATLAS:
                return new AtlasRules();
            case DEMETER:
                return new DemeterRules();
            case HEPHAESTUS:
                return new HephaestusRules();
            case MINOTAUR:
                return new MinotaurRules();
            case PAN:
                return new PanRules();
            case PROMETHEUS:
                return new PrometheusRules();
            case ZEUS:
                return new ZeusRules();
            case TRITON:
                return new TritonRules();
            case POSEIDON:
                return new PoseidonRules();
            case HESTIA:
                return new HestiaRules();
            case HERA:
                return new HeraRules();
            default:
                return null;
        }
    }

    /**
     * Placeholder to make clear() callable from any {@link GodRules rules} set
     */
    public void clear() { /* Do nothing */ }

    /**
     * Placeholder to make removeEffect() callable from any {@link GodRules rules} set
     */
    public void removeEffect() { /* Do nothing */ }

    /**
     * Generic getter for the special power condition, used for make callable the method
     * from any {@link GodRules rules} set
     *
     * @return boolan value
     */
    public boolean hasSpecialPower() {
        return false;
    }
}