package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.EventRules;

import java.io.Serializable;

/**
 * Rules for a player with Atlas as God
 */
public class AtlasRules extends EventRules implements Serializable {

    private static final long serialVersionUID = 1782644679931349574L;

    /**
     * Executes a build {@link Action action}, if the {@link #getEvent() getEvent} flag
     * is {@code true} it {@link Action builds} a Dome
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        Tile tile = Session.getInstance().getBoard().getTile(position);
        if(!getEvent()) { tile.increaseHeight(); }
        else { tile.putDome(); }
    }

}
