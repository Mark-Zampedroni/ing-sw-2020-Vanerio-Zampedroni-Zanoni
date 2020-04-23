package it.polimi.ingsw.MVC.controller;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.actions.WrongActionException;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;
import it.polimi.ingsw.MVC.model.rules.GodRules;

import java.util.ArrayList;
import java.util.List;

/* Interfaccia con le azioni sul Model, controlla se sia eseguibile l'azione e l'effettua */
public class ActionController {

    private final Player player;
    private final GodRules rules;
    private List<Action> newActions;

    private List<Position> moveCandidates;
    private List<Position> buildCandidates;
    private List<Worker> selectableWorkers; /* DA IMPLEMENTARE - Se isEmpty e azione SOLO SELECT_WORKER -> PERSO
                                       uso il consentSelect gia' in GodRules */

    public ActionController(Player player) {
        this.player = player;
        this.rules = player.getRules();
        selectableWorkers = new ArrayList<>();
        clear();
    }

    /*
        Initializes new turn environment
     */
    public void clear() {
        moveCandidates = new ArrayList<>();
        buildCandidates = new ArrayList<>();
    }

    /*
        Asserts that List contains Position
     */
    public void containsPosition(List<Position> list, Position position) throws CantActException {
        if(!list.isEmpty()) { throw new CantActException("You can't do that here"); }
    }

    /*
        Creates action list with only [WIN]
     */
    public List<Action> winning() {
        List<Action> out = new ArrayList<>();
        out.add(Action.WIN);
        return out;
    }

    /*
        Applies action [MOVE/BUILD] changes to model
     */
    public List<Action> act(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        boolean victory;
        switch(type) {
            case MOVE:
                containsPosition(moveCandidates, position);
                newActions = rules.afterMove();
                victory = rules.consentWin(worker, position);
                rules.executeMove(worker, position);
                return victory ? winning() : newActions;
            case BUILD:
                containsPosition(buildCandidates, position);
                newActions = rules.afterBuild();
                rules.executeBuild(position);
                return newActions;
            default:
                throw new WrongActionException("Used correct method signature but wrong parameters.");
        }
    }

    /*
        Applies action [SELECT_WORKER] changes to model
     */
    public List<Action> act(Worker worker, Action type) throws CantActException, WrongActionException {
        if(type == Action.SELECT_WORKER) {
            if(!selectableWorkers.contains(worker)) { throw new CantActException("You can't select this worker"); }
            // SELECT WORKER ACTION
            return rules.afterSelect();
        }
        else {
            throw new WrongActionException("Used correct method signature but wrong parameters.");
        }
    }

    public List<Action> act(Position position, Action type) throws CantActException, WrongActionException {
        if(type == Action.ADD_WORKER) {
            rules.consentAdd(position);
            newActions = new ArrayList<>();
            newActions.add((player.getWorkers().size() < 2) ? Action.ADD_WORKER : Action.END_TURN);
            return newActions;
        }
        else {
            throw new WrongActionException("Used correct method signature but wrong parameters.");
        }
    }

    /*
        Sets candidate Positions for [MOVE/BUILD] actions
     */
    public void setCandidates(Worker worker, List<Action> actions) {
        clear(); // Clear candidate lists
        Position target;
        Position center = worker.getPosition();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                target = new Position(center.getX()+x,center.getY()+y);
                for (Action action : actions) {
                    try {
                        switch (action) {
                            case MOVE:
                                rules.consentMovement(worker, target);
                                moveCandidates.add(target);
                                break;
                            case BUILD:
                                rules.consentBuild(worker, target);
                                buildCandidates.add(target);
                                break;
                            default:
                                /* Do nothing */
                        }
                    } catch(CantActException e) { /* Do nothing */ }
                }
            }
        }
    }

    public void setWorkerCandidates() {
        for(Worker worker : player.getWorkers()) {
            try {
                rules.consentSelect(worker);
                selectableWorkers.add(worker);
            } catch(CantActException e) { /* Do nothing */ }
        }

    }

    public List<Position> getMoveCandidates() {
        return moveCandidates;
    }

    public List<Position> getBuildCandidates() {
        return buildCandidates;
    }

    public List<Position> getCandidates(Action type) {
        switch(type) {
            case MOVE: return getMoveCandidates();
            case BUILD: return getBuildCandidates();
            default: return dummyPosition();
        }
    }

    /*
        Creates non-empty Position list
     */
    public List<Position> dummyPosition() {
        List<Position> out = new ArrayList<>();
        out.add(new Position(-1,-1));
        return out;
    }
}
