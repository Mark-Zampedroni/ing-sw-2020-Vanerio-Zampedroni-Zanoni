package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.Target;
import it.polimi.ingsw.exceptions.utility.NotInstantiableClass;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;

import static it.polimi.ingsw.constants.Height.*;

public class Check {

    public Check() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * If value is {@code true} this method checks if the worker is trying to build at the same height as the limit set by a parameter, otherwise it checks the opposite statement
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param value defines the condition that this method is checking
     * @param height defines a height's limit
     * @param msg displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void piece(Worker worker, Position position, boolean value, int height, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if(eventBase.getEvent() && (Session.getBoard().getTile(eventBase.getPos()).getHeight() == height) == value) { throw new CantActException(msg); }
    }

    /**
     * Checks if a worker is trying to build dome
     *
     * @param worker chosen worker
     * @param position targeted position
     * @throws CantActException when the worker has tried to build a dome
     */
    public static void piece(Worker worker, Position position) throws CantActException {
        piece(worker, position, true, TOP, "You can't build a dome");
    }

    /**
     * Checks a position's validity according to value, if value is {@code true} this method checks if the position is valid, otherwise it checks the opposite statement
     *
     * @param position targeted position
     * @param value defines the condition that this method is checking
     * @param msg displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void positionValidity(Position position, boolean value, String msg) throws CantActException {
        if(!position.isValid() == value) { throw new CantActException(msg); }
    }

    /**
     * Checks if a position is valid
     *
     * @param position targeted position
     * @throws CantActException when the position isn't within the {@link Board board} boundaries
     */
    public static void positionValidity(Position position) throws CantActException {
        positionValidity(position,true,"Position is not valid");
    }

    /**
     * Checks if a worker had been placed on a specific position, if value is {@code true} this method checks if the position is occupied by a worker,  otherwise it checks the opposite statement
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param type defines a specific type of a worker placed in a position
     * @param value defines the condition that this method is checking
     * @param msg displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void occupant(Worker worker, Position position, Target type, boolean value, String msg) throws CantActException {
        if (position.getWorker() != null) {
            if(type.compareWorkers(worker,position.getWorker()) == value) { throw new CantActException(msg); }
        }
    }

    /**
     * Checks if a position is already occupied by a worker
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param type defines a specific type of a worker placed in a position
     * @throws CantActException when the position was already occupied by a worker
     */
    public static void occupant(Worker worker, Position position, Target type) throws CantActException {
        occupant(worker,position,type,true,"Tile occupied");
    }

    /**
     * If value is {@code true} this method checks if the position is on the perimeter, otherwise it checks the opposite statement
     *
     * @param position targeted position
     * @param value defines the condition that this method is checking
     * @param msg displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void boundary(Position position, boolean value, String msg) throws CantActException {
        if(position.isBoundary() == value) { throw new CantActException(msg); }
    }

    /**
     * Checks if a position is on the perimeter
     *
     * @param position targeted position
     * @throws CantActException when the position is on the perimeter of {@link Board board}
     */
    public static void boundary(Position position) throws CantActException {
        boundary(position,true,"Tile on boundary");
    }

    /**
     * Checks if the worker's upwards movement exceeds the height's limit defines through offset
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param offset defines the height's limit which a worker can move upwards in one single movement
     * @param msg displays an error message
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void height(Worker worker, Position position, int offset, String msg) throws CantActException {
        if (Session.getBoard().getTile(position).getHeight() > Session.getBoard().getTile(worker.getPosition()).getHeight() + offset) { throw new CantActException(msg); }
    }

    /**
     * Checks if the height's difference between the worker and the targeted position was more than one
     *
     * @param worker chosen worker
     * @param position targeted position
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void height(Worker worker, Position position) throws CantActException {
        height(worker,position,1,"Tile is too high and unreachable from your position");
    }

    /**
     *Checks if the distance's difference between a worker and a position is within a range, min set the lower limit of that range instead max defines the upper one
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param min defines the minimum movement's distance
     * @param max defines the maximum movement's distance
     * @param msg displays an error message
     * @throws CantActException when the distance's difference between a worker and a position wasn't within the range
     */
    public static void distance(Worker worker, Position position, int min, int max, String msg) throws CantActException {
        if(worker.getPosition().getDistanceFrom(position) < min ||
           worker.getPosition().getDistanceFrom(position) > max) { throw new CantActException(msg); }
    }

    /**
     * Checks if the distance's difference between a worker and a position is one
     *
     * @param worker chosen worker
     * @param position targeted position
     * @throws CantActException when the distance's difference between a worker and a position wasn't one
     */
    public static void distance(Worker worker, Position position) throws CantActException {
        distance(worker,position,1,1,"Tile out of reach");
    }

    /**
     * If value is {@code true} this method checks if a position has a dome placed on, otherwise it checks the opposite statement
     *
     * @param position targeted position
     * @param value defines the condition that this method is checking
     * @param msg displays an error message
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void dome(Position position, boolean value, String msg) throws CantActException {
        if(Session.getBoard().getTile(position).hasDome() == value) { throw new CantActException(msg); }
    }

    /**
     * Checks if a dome had been placed on a position
     *
     * @param position targeted position
     * @throws CantActException when a dome had been placed on a position
     */
    public static void dome(Position position) throws CantActException {
        dome(position,true,"Tile has a dome");
    }

    /**
     *Compares the position where the worker is trying to move with it's previous one, if value is {@code true} this method checks if the two positions are the same, otherwise it checks the opposite statement
     *
     * @param worker chosen worker
     * @param position targeted position
     * @param value defines the condition that this method is checking
     * @param msg displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void oldPosition(Worker worker, Position position, boolean value, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if (eventBase.getEvent() && position.equals(eventBase.getPos()) == value) { throw new CantActException(msg); }
    }

    /**
     * Checks if the worker is trying to move on the same position it was placed before
     *
     * @param worker chosen worker
     * @param position targeted position
     * @throws CantActException when the worker has tried to move on the same position as before
     */
    public static void oldPosition(Worker worker, Position position) throws CantActException {
        oldPosition(worker,position,true,"Tile can't be old position");
    }

}

