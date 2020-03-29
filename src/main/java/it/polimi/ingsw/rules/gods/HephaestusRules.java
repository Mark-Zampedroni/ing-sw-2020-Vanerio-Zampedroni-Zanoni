package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.EventRule;
import java.util.List;

//event is true if it's the 2nd building action
public class HephaestusRules extends EventRule {

    @Override
    public void executeBuild(Position position) {
        if(!getEvent()) {
            setEvent(true);
            setPos(position);
        }
        super.executeBuild(position);
    }

    @Override
    public List<ActionType> afterBuild() {
        List<ActionType> actions = super.afterBuild();
        if(!getEvent()) { actions.add(ActionType.BUILD); }
        return actions;
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        super.consentBuild(worker, position);
        Check.oldPosition(worker, position, false, "You must build on the previous position");
    }
}