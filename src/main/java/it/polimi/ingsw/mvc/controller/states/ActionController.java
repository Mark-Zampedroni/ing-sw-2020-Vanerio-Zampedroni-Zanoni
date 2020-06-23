package it.polimi.ingsw.mvc.controller.states;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the action requests and replies (during the "GAME" state of the controller).
 * Applies the changes to the model
 */
public class ActionController implements Serializable {

    private static final long serialVersionUID = 4736242274347489291L;
    private final Player player;
    private final GodRules rules;
    private final TurnController controller;

    /**
     * Constructor, initializes the variables
     *
     * @param controller state of the controller
     * @param player     current turn owner
     */
    public ActionController(TurnController controller, Player player) {
        this.player = player;
        this.rules = player.getRules();
        this.controller = controller;
    }

    /**
     * Executes an action on the specified position with the specified worker (if possible)
     *
     * @param worker   worker that performs the action
     * @param position position on where is performed the action
     * @param type     type of the action passed
     * @return the list of possible {@link Action actions} (by the rules) after the action specified
     * @throws WrongActionException if the action is not one of the actions which apply changes to the board
     */
    public List<Action> act(Worker worker, Position position, Action type) throws WrongActionException {
        switch (type) {
            case MOVE:
                return actMove(worker, position);
            case BUILD:
                return actBuild(position);
            case SELECT_WORKER:
                return actSelectWorker(position);
            case ADD_WORKER:
                return actAddWorker(position);
            default:
                throw new WrongActionException("Used correct method signature but wrong parameters.");
        }
    }

    /**
     * Returns the list of possible positions where the specified {@link Worker worker}
     * can perform the {@link Action action}
     *
     * @param worker worker requested
     * @param action action requested
     * @return the list of possible position for that action
     */
    public List<Position> getCandidates(Worker worker, Action action) {
        switch (action) {
            case ADD_WORKER:
                return getAddWorkerCandidates();
            case SELECT_WORKER:
                return getSelectWorkerCandidates();
            case MOVE:
            case BUILD:
                if (worker == null) {
                    break;
                } // ERROR
                return getMoveBuildCandidates(worker, action);
            default:
                return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    /**
     * Executes the action {@link Action ADD_WORKER} on the specified position
     *
     * @param position the position where the player puts the worker
     * @return the list of possible actions after the addition
     */
    private List<Action> actAddWorker(Position position) {
        rules.executeAdd(player, position);
        return Collections.singletonList((player.getWorkers().size() < 2) ? Action.ADD_WORKER : Action.END_TURN);
    }

    /**
     * Executes the action {@link Action SELECT_WORKER} on the specified position
     *
     * @param position position where is the worker
     * @return the list of possible actions after the selection
     */
    private List<Action> actSelectWorker(Position position) {
        for (Player p : Session.getInstance().getPlayers()) {
            for (Worker w : p.getWorkers()) {
                if (w.getPosition().isSameAs(position) && controller != null) {
                    controller.setCurrentWorker(w);
                    break;
                }
            }
        }
        return rules.afterSelect();
    }

    /**
     * Executes the action {@link Action MOVE} on the specified position with the specified worker
     *
     * @param position the position where the worker will move
     * @param worker   the worker that performs the movement
     * @return the list of possible actions after the movement
     */
    private List<Action> actMove(Worker worker, Position position) {
        rules.executeMove(worker, position);
        List<Action> afterMove = rules.afterMove();
        boolean victory = rules.consentWin(worker, position);
        return victory ? Collections.singletonList(Action.WIN) : afterMove;
    }

    /**
     * Executes the action {@link Action BUILD} on the specified position
     *
     * @param position the position where the building is done
     * @return the list of possible actions after the build
     */
    private List<Action> actBuild(Position position) {
        rules.executeBuild(position);
        return rules.afterBuild();
    }


    /**
     * Returns the list of possible positions where the specified {@link Worker worker}
     * can perform the move or build {@link Action action}
     *
     * @param worker that perform the action
     * @param action that the worker performs between move and build
     * @return the list of possible position for the actions
     */
    private List<Position> getMoveBuildCandidates(Worker worker, Action action) {
        Position target;
        List<Position> temp = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                target = new Position(x, y);
                try {
                    if (action == Action.MOVE) {
                        rules.consentMovement(worker, target);
                        temp.add(target);
                    } else if (action == Action.BUILD) {
                        rules.consentBuild(worker, target);
                        temp.add(target);
                    }
                } catch (CantActException e) { /* Ignore */ }

            }
        }
        return temp;
    }

    /**
     * Returns the list of positions occupied by selectable workers
     *
     * @return the list of positions occupied by selectable workers
     */
    private List<Position> getSelectWorkerCandidates() {
        List<Position> temp = new ArrayList<>();
        for (Worker worker : player.getWorkers()) {
            try {
                rules.consentSelect(player.getUsername(), worker);
                temp.add(worker.getPosition());
            } catch (CantActException e) { /* Do nothing */ }
        }
        return temp;
    }

    /**
     * Returns the list of possible positions where {@link Worker workers} can be added
     *
     * @return the list of possible positions for the action ADD_WORKER
     */
    private List<Position> getAddWorkerCandidates() {
        Position target;
        List<Position> temp = new ArrayList<>();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                target = new Position(x, y);
                try {
                    rules.consentAdd(target);
                    temp.add(target);
                } catch (CantActException e) { /* Do nothing */ }
            }
        }
        return temp;
    }

}
