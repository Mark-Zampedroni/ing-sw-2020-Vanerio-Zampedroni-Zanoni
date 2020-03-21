package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.constants.Height;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

import static it.polimi.ingsw.model.Session.getBoard;

public class BuildAction {

    public void buildTowerOn (Position position, Worker worker) {
        GodRules rules = worker.getMaster().getRules();
        try {
            rules.consentBuild(worker, position);
            rules.setPos(position);
            executeBuild(position, worker);
            }
        catch (CantBuildException e) {
            System.out.println("Build error, try again");
        }
    }

    public void executeBuild(Position position, Worker worker) {
        Tile tile = Board.getTile(position);
        if (tile.getHeight() == Height.TOP)
        {   tile.placeDome();
             } else {
            tile.increaseHeight();
        }
    }
}
