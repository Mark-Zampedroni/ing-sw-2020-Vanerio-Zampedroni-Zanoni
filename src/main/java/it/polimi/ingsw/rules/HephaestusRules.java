package it.polimi.ingsw.rules;

import it.polimi.ingsw.exceptions.actions.CantBuildException;
import it.polimi.ingsw.exceptions.actions.building.HephaestusException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class HephaestusRules extends EventRule {

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        super.consentBuild(worker, position);
        if (!getEvent()) {
            setPos(position);
        } else {
            setEvent(false);
            if (!position.equals(getPos())) { //Metodo per valutare se il blocco piazzato è una dome?
                throw new HephaestusException("Not Allowed");
            }
        }
    }


}