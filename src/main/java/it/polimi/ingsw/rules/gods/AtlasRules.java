package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.EventRules;

public class AtlasRules extends EventRules {

    @Override
    public void executeBuild(Position position) {
        Tile tile = Session.getBoard().getTile(position);
        if(!getEvent()) { tile.increaseHeight(); }
        else { tile.putDome(); }
    }

}
