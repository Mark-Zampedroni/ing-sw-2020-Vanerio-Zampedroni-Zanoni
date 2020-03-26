package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.constants.Height;
import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;

import static it.polimi.ingsw.model.Session.getBoard;

public class BuildAction {

    public ArrayList<ActionType> buildTowerOn (Position position, Worker worker) {
        GodRules rules = worker.getMaster().getRules();
        ArrayList <ActionType> nextAction = new ArrayList<>();
        try {
            rules.consentBuild(worker, position);
            nextAction = executeBuild (position, worker);
            }
        catch (CantBuildException e) {
            System.out.println("Build error, try again");
        }
        return nextAction;
    }

    public ArrayList<ActionType> executeBuild(Position position, Worker worker) {
        ArrayList <ActionType> nextAction = new ArrayList<>();
        Tile tile = Board.getTile(position);
        if (tile.getHeight() == Height.TOP)
        {   tile.placeDome();
             } else {
            tile.increaseHeight();
        }
        nextAction.add(ActionType.END_TURN);
        return nextAction;
    }
}
