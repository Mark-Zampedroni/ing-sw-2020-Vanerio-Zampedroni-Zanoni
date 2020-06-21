package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.Check;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;

/**
 * Rules for a player with Hephaestus as God
 */
public class HephaestusRules extends DemeterRules implements Serializable {

    private static final long serialVersionUID = -1708343556340324002L;

    /**
     * Checks if by the rules it's physically possible to perform a build {@link Action action},
     * for the second build you have to build in the previous position
     *
     * @param worker   worker that wants to build
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