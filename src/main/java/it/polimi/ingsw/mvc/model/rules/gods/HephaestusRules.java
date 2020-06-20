package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.mvc.model.rules.EventRules;

import java.io.Serializable;
import java.util.List;

/**
 * Rules for a player with Hephaestus as God
 */
public class HephaestusRules extends EventRules implements Serializable {

    private static final long serialVersionUID = -1708343556340324002L;

    /**
     * Executes a build {@link Action action}, if it is the first build {@link Action action}
     * calls the {@link #setPos(Position) setPos} method with the {@link Position position}
     * of the build as argument
     *
     * @param position {@link Position position} where to build
     */
    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setPos(position);
        }
        super.executeBuild(position);
    }

     /** *Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.mvc.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the event flag described by {@link #getEvent() getEvent} is {@code true}
     * adds another {@link Action BUILD} action to the next actions list else if false set the flag to
     *  {@code true}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> actions = super.afterBuild();
        if(!getEvent()) { actions.add(Action.BUILD);
        setEvent(true);}
        return actions; }

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action},
     * for the second build you have to build in the previous position
     *
     * @param worker worker that wants to build
     * @param position position where the worker wants to build
     * @throws CantActException when the worker can't build
     */
    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.oldPosition(worker, position, false, "You must build on the previous position");
        Check.piece(worker);
    }
}