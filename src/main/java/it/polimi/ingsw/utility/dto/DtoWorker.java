package it.polimi.ingsw.utility.dto;

import it.polimi.ingsw.mvc.model.player.Worker;

import java.io.Serializable;

/**
 * Dto copy of {@link Worker Worker}.
 * The Dto classes are created and stored in some messages exchanged on the network,
 * their methods consist of only getters and all their variables are final
 */

public class DtoWorker implements Serializable {

    private static final long serialVersionUID = 5476611340980895309L;
    private final String masterUsername;
    private final DtoPosition position;

    /**
     * Initializes the DtoWorker
     *
     * @param worker the worker to copy as a dto
     */
    public DtoWorker(Worker worker) {
        this.masterUsername = worker.getMaster().getUsername();
        this.position = new DtoPosition(worker.getPosition());
    }

    /**
     * Getter for the username of the player who owns the worker copied
     *
     * @return the {@link String username} of the player who owns the worker
     */
    public String getMasterUsername() {
        return masterUsername;
    }

    /**
     * Returns the {@link DtoPosition DtoPosition} where the Worker copied is settled
     *
     * @return the {@link DtoPosition DTOposition}
     */
    public DtoPosition getPosition() {
        return position;
    }

    /**
     * Returns if the position of the worker copied is at the coordinates given
     *
     * @param x coordinate on the x-axis
     * @param y coordinate on the y-axis
     * @return the {@code true} if the position given is the same as the position of the worker copied
     */
    public boolean isOn(int x, int y) {
        return position.isSameAs(new DtoPosition(x, y));
    }

}
