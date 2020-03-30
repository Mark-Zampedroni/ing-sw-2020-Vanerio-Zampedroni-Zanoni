package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.rules.EventRules;

public class AtlasRules extends EventRules {

    @Override
    public void executeBuild(Position position) {
        Tile tile = Board.getTile(position);
        if(!getEvent()) { tile.increaseHeight(); }
        else { tile.placeDome(); }
    }

}
