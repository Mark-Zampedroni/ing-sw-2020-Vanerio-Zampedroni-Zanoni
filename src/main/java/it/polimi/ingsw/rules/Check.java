package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.NotInstanciableClass;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

public class Check {

    public Check() throws NotInstanciableClass {
        throw new NotInstanciableClass();
    }

    public static void positionValidity(Position position, boolean value) throws CantActException {
        if(!position.isValid() == value)
        { throw new CantActException("Position is not valid"); }
    }

    public static void occupant(Worker worker, Position position, Target type, boolean value) throws CantActException {
        if(position.getWorker()!=null) {
            if(type == Target.ALLY) {
                 if((worker.getMaster()==position.getWorker().getMaster()) == value) {
                }
            }
        }
        { throw new CantActException("msg"); }
    }


}
