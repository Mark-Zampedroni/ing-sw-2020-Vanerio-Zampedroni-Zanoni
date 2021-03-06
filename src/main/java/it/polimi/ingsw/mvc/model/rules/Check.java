package it.polimi.ingsw.mvc.model.rules;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.utility.enumerations.Target;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;
import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

import java.io.Serializable;

import static it.polimi.ingsw.utility.constants.Height.TOP;

/**
 * Checks used on all the gods actions consents
 */
public class Check implements Serializable {

    private static final long serialVersionUID = 6347275335033595844L;

    /**
     * This class shouldn't be instantiated
     * @throws NotInstantiableClass if instantiated
     */
    public Check() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * If value is {@code true} this method checks if the worker is trying to build at the same height specified by the parameter, otherwise checks the opposite statement
     *
     * @param worker chosen worker
     * @param value  defines the condition that this method is checking
     * @param height defines a height's limit
     * @param msg    displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void piece(Worker worker, boolean value, int height, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if (eventBase.getEvent() && (Session.getInstance().getBoard().getTile(eventBase.getPos()).getHeight() == height) == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if a worker is trying to build a dome
     *
     * @param worker chosen worker
     * @throws CantActException when the worker has tried to build a dome
     */
    public static void piece(Worker worker) throws CantActException {
        piece(worker, true, TOP, "You can't build a dome");
    }

    /**
     * Checks a position validity according to value, if value is {@code true} this method checks if the position is valid, otherwise checks the opposite statement
     *
     * @param position targeted position
     * @param value    defines the condition that this method is checking
     * @param msg      displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void positionValidity(Position position, boolean value, String msg) throws CantActException {
        if (!position.isValid() == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if a position is valid
     *
     * @param position targeted position
     * @throws CantActException when the position isn't within the {@link Board board} boundaries
     */
    public static void positionValidity(Position position) throws CantActException {
        positionValidity(position, true, "Position is not valid");
    }

    /**
     * Checks if a position is occupied
     *
     * @param position targeted position
     * @throws CantActException when the position isn't within the {@link Board board} boundaries
     */
    public static void occupant(Position position) throws CantActException {
        if (position.getWorker() != null) {
            throw new CantActException("This position already has a worker");
        }
    }

    /**
     * Checks if a worker had been placed on a specific position, if value is {@code true} this method checks if the position is occupied by a worker,  otherwise checks the opposite statement
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @param type     defines a specific type of a worker placed in a position
     * @param value    defines the condition that this method is checking
     * @param msg      displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void relation(Worker worker, Position position, Target type, boolean value, String msg) throws CantActException {
        if (position.getWorker() != null && type.compareWorkers(worker, position.getWorker()) == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if a position is already occupied by a worker
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @param type     defines a specific type of a worker placed in a position
     * @throws CantActException when the position was already occupied by a worker
     */
    public static void relation(Worker worker, Position position, Target type) throws CantActException {
        relation(worker, position, type, true, "Tile occupied");
    }

    /**
     * If value is {@code true} this method checks if the position is on the perimeter, otherwise checks the opposite statement
     *
     * @param position targeted position
     * @param value    defines the condition that this method is checking
     * @param msg      displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void boundary(Position position, boolean value, String msg) throws CantActException {
        if (position.isBoundary() == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if a position is on the perimeter
     *
     * @param position targeted position
     * @throws CantActException when the position is on the perimeter of {@link Board board}
     */
    public static void boundary(Position position) throws CantActException {
        boundary(position, true, "Tile on boundary");
    }

    /**
     * Checks if the worker upwards movement exceeds the height's limit defines through offset
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @param offset   defines the height's limit which a worker can move upwards in one single movement
     * @param msg      displays an error message
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void height(Worker worker, Position position, int offset, String msg) throws CantActException {
        if (Session.getInstance().getBoard().getTile(position).getHeight() > Session.getInstance().getBoard().getTile(worker.getPosition()).getHeight() + offset) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if the height difference between the worker and the targeted position is more than one
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void height(Worker worker, Position position) throws CantActException {
        height(worker, position, 1, "Tile is too high and unreachable from your position");
    }

    /**
     * Checks if the distance difference between a worker and a position is within a range, min set the lower limit of that range instead max defines the upper one
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @param min      defines the minimum movement's distance
     * @param max      defines the maximum movement's distance
     * @param msg      displays an error message
     * @throws CantActException when the distance's difference between a worker and a position wasn't within the range
     */
    public static void distance(Worker worker, Position position, int min, int max, String msg) throws CantActException {
        if (worker.getPosition().getDistanceFrom(position) < min ||
                worker.getPosition().getDistanceFrom(position) > max) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if the distance difference between a worker and a position is one
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @throws CantActException when the distance's difference between a worker and a position wasn't one
     */
    public static void distance(Worker worker, Position position) throws CantActException {
        distance(worker, position, 1, 1, "Tile out of reach");
    }

    /**
     * If value is {@code true} this method checks if a position has a dome placed on, otherwise checks the opposite statement
     *
     * @param position targeted position
     * @param value    defines the condition that this method is checking
     * @param msg      displays an error message
     * @throws CantActException when the position where the worker has tried to move on was too high
     */
    public static void dome(Position position, boolean value, String msg) throws CantActException {
        if (Session.getInstance().getBoard().getTile(position).hasDome() == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if a dome had been placed on a position
     *
     * @param position targeted position
     * @throws CantActException when a dome had been placed on a position
     */
    public static void dome(Position position) throws CantActException {
        dome(position, true, "Tile has a dome");
    }

    /**
     * Compares the position where the worker is trying to move with it's previous one, if value is {@code true} this method checks if the two positions are the same, otherwise checks the opposite statement
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @param value    defines the condition that this method is checking
     * @param msg      displays an error message
     * @throws CantActException when the condition hasn't been respected accordingly to value
     */
    public static void oldPosition(Worker worker, Position position, boolean value, String msg) throws CantActException {
        EventRules eventBase = (EventRules) worker.getMaster().getRules();
        if (eventBase.getEvent() && position.isSameAs(eventBase.getPos()) == value) {
            throw new CantActException(msg);
        }
    }

    /**
     * Checks if the worker is trying to move on its previous position
     *
     * @param worker   chosen worker
     * @param position targeted position
     * @throws CantActException when the worker has tried to move on the same position as before
     */
    public static void oldPosition(Worker worker, Position position) throws CantActException {
        oldPosition(worker, position, true, "Tile can't be old position");
    }

}

