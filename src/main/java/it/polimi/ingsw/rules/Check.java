package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.utility.NotInstantiableClass;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;

import static it.polimi.ingsw.constants.Height.*;

public class Check {

    public Check() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    public static void piece(Worker worker, Position position, boolean value, int height, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if(eventBase.getEvent() && (Session.getBoard().getTile(eventBase.getPos()).getHeight() == height) == value) { throw new CantActException(msg); }
    }

    public static void piece(Worker worker, Position position) throws CantActException {
        piece(worker, position, true, TOP, "You can't build a dome");
    }

    public static void positionValidity(Position position, boolean value, String msg) throws CantActException {
        if(!position.isValid() == value) { throw new CantActException(msg); }
    }

    public static void positionValidity(Position position) throws CantActException {
        positionValidity(position,true,"Position is not valid");
    }

    public static void occupant(Worker worker, Position position, Target type, boolean value, String msg) throws CantActException {
        if (position.getWorker() != null) {
            switch (type) {
                case ALLY: if (allyOccupant(worker,position) == value) { throw new CantActException(msg); }
                case ENEMY: if (allyOccupant(worker,position) != value) { throw new CantActException(msg); }
                case ANY: throw new CantActException("Tile is occupied by your worker");
            }
        }
    }

    public static void occupant(Worker worker, Position position, Target type) throws CantActException {
        occupant(worker,position,type,true,"Tile occupied");
    }

    private static boolean allyOccupant(Worker worker, Position position) {
        return (worker.getMaster() == position.getWorker().getMaster());
    }

    public static void boundary(Position position, boolean value, String msg) throws CantActException {
        if(position.isBoundary() == value) { throw new CantActException(msg); }
    }

    public static void boundary(Position position) throws CantActException {
        boundary(position,true,"Tile on boundary");
    }

    public static void height(Worker worker, Position position, int offset, String msg) throws CantActException {
        if (Session.getBoard().getTile(position).getHeight() > Session.getBoard().getTile(worker.getPosition()).getHeight() + offset) { throw new CantActException(msg); }
    }

    public static void height(Worker worker, Position position) throws CantActException {
        height(worker,position,1,"Tile is too high and unreachable from your position");
    }

    public static void distance(Worker worker, Position position, int min, int max, String msg) throws CantActException {
        if(worker.getPosition().getDistanceFrom(position) <= max &&
           worker.getPosition().getDistanceFrom(position) >= min) { throw new CantActException(msg); }
    }

    public static void distance(Worker worker, Position position) throws CantActException {
        distance(worker,position,1,1,"Tile out of reach");
    }

    public static void dome(Position position, boolean value, String msg) throws CantActException {
        if(Session.getBoard().getTile(position).hasDome() == value) { throw new CantActException(msg); }
    }

    public static void dome(Position position) throws CantActException {
        dome(position,true,"Tile has a dome");
    }

    public static void oldPosition(Worker worker, Position position, boolean value, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if (eventBase.getEvent() && position.equals(eventBase.getPos()) == value) { throw new CantActException(msg); }
    }

    public static void oldPosition(Worker worker, Position position) throws CantActException {
        oldPosition(worker,position,true,"Tile can't be old position");
    }

}

