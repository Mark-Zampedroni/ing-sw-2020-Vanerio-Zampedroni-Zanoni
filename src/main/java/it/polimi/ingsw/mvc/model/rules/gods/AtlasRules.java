package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.map.Tile;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.mvc.model.rules.SpecialPower;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;

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
     * Executes a {@link Action build}, placing a dome if the god power is on
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        Tile tile = Session.getInstance().getBoard().getTile(position);
        if(!getEvent()) {
            tile.increaseHeight();
        }
        else {
            tile.putDome();
        }
        notify(new DtoSession(Session.getInstance()));}

    /**
     * Returns a list of possible actions after a {@link Action move}, adding the {@link Action god power}
     *
     * @return list of {@link Action actions} that can be done after {@link Action moving}
     */
    @Override
    public List<Action> afterMove() {
        return new ArrayList<>(Arrays.asList(Action.SPECIAL_POWER, Action.BUILD));
    }

    /**
     * Toggles the {@link Action god power}
     */
    @Override
    public void toggleSpecialPower() {
        setEvent(!getEvent());
    }
}
