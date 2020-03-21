package it.polimi.ingsw.model.actions;

import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

//efesto, prometeo e demetrio qui
public class DifferentBuildAction extends BuildAction {

    @Override
    public void executeBuild (Position position, Worker worker) {
        super.executeBuild(position, worker);
        rules.setEvent(true);
    }
}
