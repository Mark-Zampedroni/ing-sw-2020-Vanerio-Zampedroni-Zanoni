package it.polimi.ingsw.mvc.controller.states.actionControl;

import it.polimi.ingsw.mvc.controller.states.TurnController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.GodRules;

import java.io.Serializable;
import java.util.*;

/**
 * Controller part dedicated to call the methods that perform the actions of the player in the game.
 */
public class ActionController implements Serializable {

    private static final long serialVersionUID = 4736242274347489291L;
    private final Player player;
    private final GodRules rules;
    private final TurnController controller;

    /**
     * Constructor, is linked to the current {@link Player player} and the controller of the turn
     * @param controller controller of the turn
     * @param player currently active
     */
    public ActionController(TurnController controller, Player player) {
        this.player = player;
        this.rules = player.getRules();
        this.controller = controller;
    }

    /**
     * Apply the changes to the model about the action passed, performed by the passed worker,
     * in the passed position
     *
     * @param worker that performs the action
     * @param position where is performed the action
     * @param type of the action passed
     * @return the list of possibile {@link Action actions} after the passed action
     * @throws WrongActionException if the action is not one of the possible actions in the game
     */
    public List<Action> act(Worker worker, Position position, Action type) throws WrongActionException {
        //boolean victory;
        switch(type) {
            case MOVE: return actMove(worker,position);
            case BUILD: return actBuild(position);
            case SELECT_WORKER: return actSelectWorker(position);
            case ADD_WORKER: return actAddWorker(position);
            default:
                throw new WrongActionException("Used correct method signature but wrong parameters.");
        }
    }

    /**
     * Return the list of possible positions where the {@link Worker worker}
     * can perform the {@link Action action}
     *
     * @param worker that perform the action
     * @param action that the worker performs
     *
     * @return the list of possible position for that action
     */
    public List<Position> getCandidates(Worker worker, Action action) {
        switch(action) {
            case ADD_WORKER: return getAddWorkerCandidates();
            case SELECT_WORKER: return getSelectWorkerCandidates();
            case MOVE:
            case BUILD:
                if(worker == null) { break; } // ERROR
                return getMoveBuildCandidates(worker,action);
            default: return new ArrayList<>();
        }
        return null;
    }


    private List<Action> actAddWorker(Position position) {
        rules.executeAdd(player, position);
        return Collections.singletonList((player.getWorkers().size() < 2) ? Action.ADD_WORKER : Action.END_TURN);
    }

    private List<Action> actSelectWorker(Position position) {
        for (Player player : Session.getInstance().getPlayers()) {
            for (Worker w : player.getWorkers()) {
                if (w.getPosition().equals(position) && controller != null) {
                    controller.setCurrentWorker(w);
                    break;
                }
            }
        }
        return rules.afterSelect();
    }


    private List<Action> actMove(Worker worker, Position position) {
        rules.executeMove(worker, position);
        List<Action> afterMove = rules.afterMove();
        boolean victory = rules.consentWin(worker, position);
        return victory ? Collections.singletonList(Action.WIN) : afterMove;
    }


    private List<Action> actBuild(Position position) {
        rules.executeBuild(position);
        return rules.afterBuild();
    }


    // CANDIDATES MOVE / BUILD
    private List<Position> getMoveBuildCandidates(Worker worker, Action action) {
        Position target;
        List<Position> temp = new ArrayList<>();
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 5; y++) {
                target = new Position(x, y);
                try {
                    switch(action) {
                        case MOVE:
                            rules.consentMovement(worker, target);
                            temp.add(target);
                            break;
                        case BUILD:
                            rules.consentBuild(worker, target);
                            temp.add(target);
                            break;
                    }

                } catch (CantActException e) { /* Ignore */ }

            }
        }
        return temp;
    }

    // CANDIDATES SELECT_WORKER
    private List<Position> getSelectWorkerCandidates() {
        List<Position> temp = new ArrayList<>();
        for(Worker worker : player.getWorkers()) {
            try {
                rules.consentSelect(player.getUsername(), worker);
                temp.add(worker.getPosition());
            } catch(CantActException e) { /* Do nothing */ }
        }
        return temp;
    }

    // CANDIDATES ADD_WORKER
    private List<Position> getAddWorkerCandidates() {
        Position target;
        List<Position> temp = new ArrayList<>();
        for(int x = 0; x < 5; x++) {
            for(int y = 0; y < 5; y ++) {
                target = new Position(x,y);
                try {
                    rules.consentAdd(target);
                    temp.add(target);
                } catch (CantActException e) { /* Do nothing */ }
            }
        }
        return temp;
    }

}
