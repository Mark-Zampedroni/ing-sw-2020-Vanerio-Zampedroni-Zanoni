package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
        import it.polimi.ingsw.enumerations.Gods;
        import it.polimi.ingsw.exceptions.actions.CantBuildException;
        import it.polimi.ingsw.model.map.Board;
        import it.polimi.ingsw.model.map.Tile;
        import it.polimi.ingsw.model.player.Position;
        import it.polimi.ingsw.model.player.Worker;
        import it.polimi.ingsw.rules.EventRule;
        import it.polimi.ingsw.rules.GodRules;

        import javax.swing.*;
        import java.lang.reflect.Array;
        import java.util.ArrayList;

public class AtlasBuildAction extends BuildAction {

    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {
        EventRule rules = (EventRule) worker.getMaster().getRules();
        ArrayList<ActionType> nextAction;
        if (!rules.getEvent()) {
            nextAction= super.executeBuild(position, worker);
        } else {
            Tile tile = Board.getTile(position);
            tile.placeDome();
            nextAction = new ArrayList<>();
            nextAction.add(ActionType.END_TURN);
        }
        return nextAction;
    }
}
