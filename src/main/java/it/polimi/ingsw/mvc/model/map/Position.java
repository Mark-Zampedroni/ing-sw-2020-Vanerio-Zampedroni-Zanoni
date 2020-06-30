package it.polimi.ingsw.mvc.model.map;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;

import java.io.Serializable;

/**
 * Used as coordinates holder by {@link Worker Worker} and indirectly links it to a {@link Tile Tile}
 */
public class Position implements Serializable {

    private static final long serialVersionUID = 4895645710384390980L;
    private final int x;
    private final int y;

    /**
     * Initializes a new position
     *
     * @param x int that indicates the x coordinate
     * @param y int that indicates the y coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Method to check if two positions have the same coordinates
     *
     * @param position {@link Position position} to compare
     * @return {@code true} if the coordinates match
     */
    public boolean isSameAs(Position position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Evaluates if the position is valid
     *
     * @return {@code true} if position is within the {@link Board board} boundaries
     */
    public boolean isValid() {
        return (x >= 0 && x < 5 && y >= 0 && y < 5);
    }

    /**
     * Calculates the distance from another position
     *
     * @param position target {@link Position position}
     * @return calculated distance
     */
    public int getDistanceFrom(Position position) {
        int deltaX = Math.abs(this.x - position.getX());
        int deltaY = Math.abs(this.y - position.getY());
        int diagonal = Math.min(deltaX, deltaY);
        int straight = Math.max(deltaX, deltaY) - diagonal;

        return (int) (diagonal * Math.sqrt(2) + straight);
    }

    /**
     * Confirms if there is a {@link Worker worker} on the same coordinates
     *
     * @return the {@link Worker worker} on the same coordinates, null if none is found
     */
    public Worker getWorker() {
        for (Player player : Session.getInstance().getPlayers()) {
            for (Worker worker : player.getWorkers()) {
                if (this.isSameAs(worker.getPosition())) {
                    return worker;
                }
            }
        }
        return null;
    }

    /**
     * Generates a String stating the position coordinates
     *
     * @return coordinates as String
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Checks if the position is perimetrical
     *
     * @return {@code true} if position coordinates are on the perimeter of {@link Board board}
     */
    public boolean isBoundary() {
        return (x == 0 || x == 4 || y == 0 || y == 4);
    }

    /**
     * Getter for x coordinate
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for y coordinate
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }
}
