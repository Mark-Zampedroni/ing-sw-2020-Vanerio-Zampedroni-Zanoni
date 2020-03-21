package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class Action {

    public boolean movFlag;

    public void startAction (ActionType action, Worker worker, Position position) {
        if (action.equals(ActionType.BUILD)) {
            if (worker.getMaster().getGod().equals(Gods.ATLAS)){
                new AtlasBuildAction().buildTowerOn(position, worker);  //qui andrebbe messo il collegamento con la view
            }
        }
        if (action.equals(ActionType.MOVE)) {
            if (worker.getMaster().getGod().equals(Gods.APOLLO)){
                new ApolloMoveAction().moveWorkerTo(position, worker); }
            else { if (worker.getMaster().getGod().equals(Gods.ARTHEMIS)) {
                new MoveAction().moveWorkerTo(position, worker);
                movFlag = true;
            }
            }
        }

    }

}
