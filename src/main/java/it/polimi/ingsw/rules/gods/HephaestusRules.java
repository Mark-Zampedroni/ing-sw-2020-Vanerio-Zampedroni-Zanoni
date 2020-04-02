package it.polimi.ingsw.rules.gods;

import static it.polimi.ingsw.constants.Height.*;
import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRules;
import java.util.List;

//event is true if it's the 2nd building action

//LA SECONDA COSTRUZIONE NON DEVE ESSERE UNA DOME
public class HephaestusRules extends EventRules {

    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
            setPos(position);
        }
        super.executeBuild(position);
    }

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