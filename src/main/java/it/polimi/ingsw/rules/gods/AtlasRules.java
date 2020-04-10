package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.EventRules;

/**
 * Rules for a player with Atlas as God
 */
public class AtlasRules extends EventRules {

    /**
     * Executes a build {@link Action action}, if the {@link #getEvent() getEvent} flag
     * is {@code true} it {@link Action BUIL} a Dome
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        Tile tile = Session.getBoard().getTile(position);
        if(!getEvent()) { tile.increaseHeight(); }
        else { tile.putDome(); }
    }

}
