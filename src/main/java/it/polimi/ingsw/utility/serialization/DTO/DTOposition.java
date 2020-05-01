package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.MVC.model.map.Position;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Position Position}
 */
public class DTOposition implements Serializable {
    private final int x;
    private final int y;

    /**
     * Initializes a new DTOposition
     *
     * @param position indicates his equivalent in server storage
     */
    public DTOposition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
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

    /**
     * Method to check if two DTOpositions have the same coordinates
     *
     * @param position {@link DTOposition position} to compare
     * @return {@code true} if the coordinates match
     */
    public boolean equals(DTOposition position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Method to check if a DTOpositions have the same coordinates of a Position
     *
     * @param position {@link Position position} to compare
     * @return {@code true} if the coordinates match
     */
    public boolean equals(Position position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Generates a String stating the DTOposition coordinates
     *
     * @return coordinates as String
     */
    @Override
    public String toString() {
        return "("+x+","+y+")";
    }


/**
     * Evaluates if the DTOposition is valid
     *
     * @return {@code true} if position is within the {@link DTOboard DTOboard} boundaries
    */
    public boolean isValid() {
        return (x >= 0 && x < 5 && y >= 0 && y < 5);
    }

    /**
     * Calculates the distance from another DTOposition
     *
     * @param position target {@link DTOposition DTOposition}
     * @return calculated distance
    */
    public int getDistanceFrom(DTOposition position) {
        int deltax = Math.abs(this.x - position.getX());
        int deltay = Math.abs(this.y - position.getY());
        int diagonal = Math.min(deltax, deltay);
        int straight = Math.max(deltax, deltay) - diagonal;

        return (int)(diagonal * Math.sqrt(2) + straight);
    }

    /**
     * Checks if the DTOposition is perimetrical
     *
     * @return {@code true} if DTOposition coordinates are on the perimeter of {@link DTOboard DTOboard}
    */
    public boolean isBoundary() {
        return (x == 0 || x == 4 || y == 0 || y == 4);
    }
}
