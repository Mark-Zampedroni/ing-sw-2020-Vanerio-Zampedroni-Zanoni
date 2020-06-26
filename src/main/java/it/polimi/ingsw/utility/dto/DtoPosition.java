package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.map.Position;

import java.io.Serializable;

/**
 * Dto copy of {@link Position Position}.
 * The Dto classes are created and stored in some messages exchanged on the network,
 * their methods consist of only getters and all their variables are final
 */
public class DtoPosition implements Serializable {
    private static final long serialVersionUID = -8141609635185613115L;
    private final int x;
    private final int y;

    /**
     * Initializes a new DtoPosition
     *
     * @param position the position to copy as a dto
     */
    public DtoPosition(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * Initializes a new DtoPosition
     *
     * @param x position on the x-axis
     * @param y position on the y-axis
     */
    public DtoPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for coordinate x
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for coordinate y
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if two DtoPositions have the same coordinates
     *
     * @param position target DtoPosition
     * @return {@code true} if the coordinates match
     */
    public boolean isSameAs(DtoPosition position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Checks if a DtoPosition has the same coordinates of a Position
     *
     * @param position target Position
     * @return {@code true} if the coordinates match
     */
    public boolean isSameAs(Position position) {
        return (x == position.getX() && y == position.getY());
    }

    /**
     * Generates a String with the DtoPosition coordinates
     *
     * @return coordinates as String
     */
    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

    /**
     * Evaluates if the DtoPosition is valid
     *
     * @return {@code true} if position is within the boundaries
     */
    public boolean isValid() {
        return (x >= 0 && x < 5 && y >= 0 && y < 5);
    }

    /**
     * Checks if the DtoPosition is on the perimeter
     *
     * @return {@code true} if DtoPosition coordinates are on the perimeter
     */
    public boolean isBoundary() {
        return (x == 0 || x == 4 || y == 0 || y == 4);
    }
}
