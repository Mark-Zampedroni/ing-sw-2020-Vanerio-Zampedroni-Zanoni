package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.message.Reply;
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

    public ActionController(Player player) {
        this.player = player;
        this.rules = player.getRules();
    }

    public List<Action> act(Worker worker, Position position, Action type) throws CantActException, WrongActionException {
        try {
            switch(type) {
                case MOVE:
                    rules.consentMovement(worker,position);
                    rules.executeMove(worker,position);
                    return rules.afterMove();
                case BUILD:
                    rules.consentBuild(worker,position);
                    rules.executeBuild(position);
                    return rules.afterMove();
                default:
                    throw new WrongActionException("Used correct method signature but wrong parameters.");
            }
        } catch(CantActException | WrongActionException e) { throw e; }
    }

    public List<Action> act(Worker worker, Action type) throws CantActException, WrongActionException {
        try {
            if(type == Action.SELECT_WORKER) {
                    if(!player.getWorkers().contains(worker)) { throw new CantActException("You can't select an enemy worker"); }
                    rules.consentSelect(worker);
                    return rules.afterSelect();
                    }
            else {
                    throw new WrongActionException("Used correct method signature but wrong parameters.");
            }
        } catch(CantActException | WrongActionException e) { throw e; }
    }

}
