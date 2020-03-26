package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRule;
import it.polimi.ingsw.rules.GodRules;
import it.polimi.ingsw.rules.PoseidonRules;

import java.util.ArrayList;

public class PoseidonBuildAction extends BuildAction {


    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {
        ArrayList<ActionType> nextAction= super.executeBuild(position, worker);
        Worker notMovedWorker;
        PoseidonRules rules = (PoseidonRules) worker.getMaster().getRules();
        if (worker.getMaster().getWorkers().get(0) != worker){
            notMovedWorker= worker.getMaster().getWorkers().get(0);
        } else {
            notMovedWorker= worker.getMaster().getWorkers().get(1);
        }
        Position positionNotMoved = notMovedWorker.getPosition();

        if (Board.getTile(positionNotMoved).getHeight() == 0) {
            rules.clearCounter();
            nextAction.add(ActionType.BUILD_OTHER);
        }
        return nextAction;
    }
}
