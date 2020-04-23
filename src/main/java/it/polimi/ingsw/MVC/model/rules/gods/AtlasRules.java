package it.polimi.ingsw.MVC.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.map.Tile;
import it.polimi.ingsw.MVC.model.map.Position;
import it.polimi.ingsw.MVC.model.rules.EventRules;

import java.io.Serializable;

/**
 * Rules for a player with Atlas as God
 */
public class AtlasRules extends EventRules implements Serializable {

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
