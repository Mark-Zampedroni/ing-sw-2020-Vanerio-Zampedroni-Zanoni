package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.GodRules;

public class AtlasBuildAction extends BuildAction {

   @Override
    public void executeBuild (Position position, Worker worker) {
       if (!worker.getMaster().getRules().getEvent()) {
           super.executeBuild(position, worker);
       } else {
           Tile tile = Board.getTile(position);
           tile.placeDome();
       }
   }
}
