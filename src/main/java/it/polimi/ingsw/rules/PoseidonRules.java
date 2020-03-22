package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.PoseidonException;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import static it.polimi.ingsw.constants.Height.*;
//Event is true if is the unmoved worker
public class PoseidonRules extends GodRules {
    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if(getEvent() && Board.getTile(worker.getPosition()).getHeight()!=GROUND) {
        }
            throw new PoseidonException("Not allowed");
        }
}
