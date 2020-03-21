package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

public class AtlasBuildAction extends BuildAction {

    public void buildTowerOn (Position position, Worker worker, boolean dome) {
        GodRules rules = worker.getMaster().getRules();
        try {rules.consentBuild(worker, position);
            Tile tile = Board.getTile(position);
            if (dome)
                tile.placeDome();
            else if (tile.getHeight()!=3) {
                tile.increaseHeight();
            }
        }
        catch (CantBuildException e) {
            System.out.println("Build error, try again");
        }
    }
}
