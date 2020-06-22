package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.player.Worker;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Worker Worker}
 */

public class DtoWorker implements Serializable {

    private static final long serialVersionUID = 5476611340980895309L;
    private final String masterUsername;
    private final DtoPosition position;

    /**
     * Initializes the DTOworker
     *
     * @param worker indicates his equivalent in server storage
     */
    public DtoWorker(Worker worker) {
        this.masterUsername = worker.getMaster().getUsername();
        this.position = new DtoPosition(worker.getPosition());
    }

    /**
     * Getter for master username
     *
     * @return the {@link String username} of the player who owns the worker
     */
    public String getMasterUsername() {
        return masterUsername;
    }

    /**
     * Returns the {@link DtoPosition DTOposition} where the Worker is settled
     *
     * @return the {@link DtoPosition DTOposition}
     */
    public DtoPosition getPosition() {
        return position;
    }

    /**
     * Returns if the DTO-Position corresponds to a position on the coordinates
     *
     * @param x coordinate on the x-axis
     * @param y coordinate on the y-axis
     * @return the {@code true} if is the same
     */
    public boolean isOn(int x, int y) {
        return position.isSameAs(new DtoPosition(x, y));
    }


    /**
     * Generates a String which contains the DTOworker's variables values
     *
     * @return a String with the DTOworker's attributes
     */
    @Override
    public String toString() {
        return "Master: " + getMasterUsername() + " , (" + getPosition().getX() + "," + getPosition().getY() + ")";
    }
}
