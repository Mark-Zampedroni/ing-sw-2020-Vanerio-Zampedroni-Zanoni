package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.exceptions.actions.WrongActionException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.List;

public class ActionController {

    Player player;
    GodRules rules;
    List<Action> newActions;

    public ActionController(Player player) {
        this.player = player;
        this.rules = player.getRules();
    }

    public List<Action> act(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        switch(type) {
            case MOVE:
                rules.consentMovement(worker, position);
                newActions = rules.afterMove();
                rules.executeMove(worker, position);
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

}
