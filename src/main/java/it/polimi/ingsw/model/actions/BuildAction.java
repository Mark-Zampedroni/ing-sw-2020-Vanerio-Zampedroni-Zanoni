package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.constants.Height;
import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodSharedRules;

import java.util.ArrayList;

public class BuildAction {

    public ArrayList<ActionType> buildTowerOn (Position position, Worker worker) {
        GodSharedRules rules = worker.getMaster().getRules();
        ArrayList <ActionType> nextAction = new ArrayList<>();
        try {
            rules.consentBuild(worker, position);
            nextAction = executeBuild (position, worker);
            }
        catch (CantActException e) {
            System.out.println("Build error, try again");
            nextAction.add(ActionType.BUILD);
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
