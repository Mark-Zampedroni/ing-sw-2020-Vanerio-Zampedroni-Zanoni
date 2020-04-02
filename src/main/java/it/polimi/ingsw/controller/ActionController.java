package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.exceptions.actions.WrongActionException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Interfaccia con le azioni sul Model, controlla se sia eseguibile l'azione e l'effettua */
public class ActionController {

    Player player;
    GodRules rules;
    List<Action> newActions;

    List<Position> moveCandidates;
    List<Position> buildCandidates;

    public ActionController(Player player) {
        this.player = player;
        this.rules = player.getRules();
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
        Applies action [MOVE/BUILD] changes to model
     */
    public List<Action> act(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        boolean victory;
        switch(type) {
            case MOVE:
                rules.consentMovement(worker, position);
                newActions = rules.afterMove();
                victory = rules.isWinner(worker, position);
                rules.executeMove(worker, position);
                if(victory) {
                    List<Action> out = new ArrayList<>();
                    out.add(Action.WIN);
                    return out;
                }
                return newActions;
            case BUILD:
                rules.consentBuild(worker, position);
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
            if(!player.getWorkers().contains(worker)) { throw new CantActException("You can't select an enemy worker"); }
            rules.consentSelect(worker);
            return rules.afterSelect();
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
                            moveCandidates.add(target.copy());
                        case BUILD:
                            rules.consentBuild(worker, target);
                            buildCandidates.add(target.copy());
                        default:
                            /* Do nothing */
                        }
                    } catch(CantActException e) { /* Do nothing */ }
                }
            }
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
            default:
                List<Position> out = new ArrayList<>();
                out.add(new Position(0,0)); // Not empty
                return out;
        }
    }
}
