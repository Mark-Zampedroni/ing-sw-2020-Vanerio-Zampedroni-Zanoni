package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.SpecialPower;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Rules for a player with Atlas as God
 */
public class AtlasRules extends EventRules implements Serializable, SpecialPower {

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
        notify(new DtoSession(Session.getInstance()));}

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action moved} a {@link Worker worker},
     * one of them is the {@link Action Special_Power} of building an extra dome
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    @Override
    public List<Action> afterMove() { return new ArrayList<>(Arrays.asList(Action.SPECIAL_POWER, Action.BUILD)); }

    /**
     * Method that set the activation of the special power if choosed
     */
    @Override
    public void toggleSpecialPower() {
        setEvent(!getEvent());
    }
}
