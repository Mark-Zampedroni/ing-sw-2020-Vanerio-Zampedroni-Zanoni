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

/* Interfaccia con le azioni sul Model, controlla se sia eseguibile l'azione e l'effettua */
public class ActionController implements Serializable {

    private static final long serialVersionUID = 4736242274347489291L;
    private final Player player;
    private final GodRules rules;
    private final TurnController controller;

    public ActionController(TurnController controller, Player player) {
        this.player = player;
        this.rules = player.getRules();
        this.controller = controller;
    }


    // CONTROLLO SU POSIZIONE ! IN TURNCONTROLLER

    /*
        Applies action [MOVE/BUILD] changes to model
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

    public List<Position> getCandidates(Worker worker, Action action) {
        switch(action) {
            case ADD_WORKER: return getAddWorkerCandidates();
            case SELECT_WORKER: return getSelectWorkerCandidates();
            case MOVE:
            case BUILD:
                if(worker == null) { break; } // ERROR
                return getMoveBuildCandidates(worker,action);
            case WIN:
            case END_TURN: return new ArrayList<>();
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
        List<Action> afterMove = rules.afterMove();
        boolean victory = rules.consentWin(worker, position);
        rules.executeMove(worker, position);
        return victory ? Collections.singletonList(Action.WIN) : afterMove;
    }


    private List<Action> actBuild(Position position) {
        List<Action> afterBuild = rules.afterBuild();
        rules.executeBuild(position);
        return afterBuild;
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
