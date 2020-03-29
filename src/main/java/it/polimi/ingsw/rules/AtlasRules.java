package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class AtlasRules extends EventRule {

    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {

        ArrayList<ActionType> nextAction;
        if (!this.getEvent()) {
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
