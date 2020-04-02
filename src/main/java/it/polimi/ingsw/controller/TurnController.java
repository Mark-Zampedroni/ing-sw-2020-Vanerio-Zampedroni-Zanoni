package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.Reply;
import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;
import java.util.List;

public class TurnController {

    List<Player> players;
    List<Action> possibleActions;
    Player currentPlayer;
    Worker currentWorker;
    GodRules currentRules;
    int currentIndex;

    public TurnController(List<Player> players) {
        this.players = players;
        currentPlayer = players.get(currentIndex);
        currentRules = currentPlayer.getRules();
    }

    public Reply response(String msg) { return new Reply(); } // Da mettere in server

    private void clearActions() {
        possibleActions = new ArrayList();
        possibleActions.add(Action.SELECT_WORKER);
    }

    public void passTurn() {
        currentIndex++;
        currentIndex = currentIndex % players.size(); //0-2 or 0-3
        currentPlayer = players.get(currentIndex);
        currentRules = currentPlayer.getRules();
        clearActions();
    }

    public Reply selectWorker(Worker worker) {
        try {
            if(possibleActions.contains(Action.SELECT_WORKER)) {
                currentRules.consentSelect();
            }
            else {
                return response("You can't select a worker now");
            }
        } catch(CantActException e) { return response(e.getMessage()); }

        }
    }

}
