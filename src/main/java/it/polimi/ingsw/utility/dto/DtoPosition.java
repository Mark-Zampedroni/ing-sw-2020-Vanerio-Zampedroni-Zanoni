package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.map.Position;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Position Position}
 */
public class DtoPosition implements Serializable {
    private static final long serialVersionUID = -8141609635185613115L;
    private final int x;
    private final int y;

    /**
     * Initializes a new DTOposition
     *
     * @param position indicates his equivalent in server storage
     */
    public DtoPosition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Initializes a new DTOposition
     *
     * @param x coordinate on the x-axis
     * @param y coordinate on the y-axis
     */
    public DtoPosition(int x, int y) {
        this.x = x;
        this.y = y;
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
     * @param position {@link DtoPosition position} to compare
     * @return {@code true} if the coordinates match
     */
    public boolean isSameAs(DtoPosition position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Method to check if a DTOpositions have the same coordinates of a Position
     *
     * @param position {@link Position position} to compare
     * @return {@code true} if the coordinates match
     */
    public boolean isSameAs(Position position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Generates a String stating the DTOposition coordinates
     *
     * @return coordinates as String
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }


    /**
     * Evaluates if the DTOposition is valid
     *
     * @return {@code true} if position is within the {@link DtoBoard DTOboard} boundaries
     */
    public boolean isValid() {
        return (x >= 0 && x < 5 && y >= 0 && y < 5);
    }

    /**
     * Checks if the DTOposition is perimetrical
     *
     * @return {@code true} if DTOposition coordinates are on the perimeter of {@link DtoBoard DTOboard}
     */
    public boolean isBoundary() {
        return (x == 0 || x == 4 || y == 0 || y == 4);
    }
}
