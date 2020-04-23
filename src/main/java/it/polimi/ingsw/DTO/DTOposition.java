package it.polimi.ingsw.DTO;

import it.polimi.ingsw.model.map.Position;

/**
 * DTO copy of the class {@link Position Position}
 */
public class DTOposition {
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
     * Confirms if there is a {@link DTOworker worker} on the same coordinates
     *
     * @return the {@link DTOworker worker} on the same coordinates, null if none is found
     */
    public DTOworker getWorker() {
        for(DTOplayer player : DTOsession.getInstance().getPlayers()) {
            for(DTOworker worker : player.getWorkers()) {
                if(this.equals(worker.getPosition())) {
                    return worker;
                }
            }
        }
        return null;
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


   /* /**
     * Evaluates if the DTOposition is valid
     *
     * @return {@code true} if position is within the {@link Board board} boundaries

    public boolean isValid() {
        return (x >= 0 && x < 5 && y >= 0 && y < 5);
    }

    /**
     * Calculates the distance from another DTOposition
     *
     * @param position target {@link Position position}
     * @return calculated distance

    public int getDistanceFrom(DTOposition position) {
        int deltax = Math.abs(this.x - position.getX());
        int deltay = Math.abs(this.y - position.getY());
        int diagonal = Math.min(deltax, deltay);
        int straight = Math.max(deltax, deltay) - diagonal;

        return (int)(diagonal * Math.sqrt(2) + straight);
    }
*/

   /* /**
     * Checks if the position is perimetrical
     *
     * @return {@code true} if position coordinates are on the perimeter of {@link Board board}

    public boolean isBoundary() {
        return (x == 0 || x == 4 || y == 0 || y == 4);
    }
*/
}
