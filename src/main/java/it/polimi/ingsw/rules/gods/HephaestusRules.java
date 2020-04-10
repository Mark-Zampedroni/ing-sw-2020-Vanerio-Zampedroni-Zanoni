package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

/**
 * Rules for a player with Hephaestus as God
 */
public class HephaestusRules extends EventRules {

    /**
     * Executes a build {@link Action action}, if is the first build {@link Action action}
     * calls the {@link #setEvent setEvent} with {@code true} argument and calls the
     * {@link #setPos(Position) setPos} method with the {@link Position position} of the build
     * as argument
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
            setPos(position);
        }
        super.executeBuild(position);
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is {@code true}
     * adds another {@link Action BUILD} action to the next actions list
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) { actions.add(Action.BUILD); }
        return actions;
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.oldPosition(worker, position, false, "You must build on the previous position");
        Check.piece(worker, position);
    }
}