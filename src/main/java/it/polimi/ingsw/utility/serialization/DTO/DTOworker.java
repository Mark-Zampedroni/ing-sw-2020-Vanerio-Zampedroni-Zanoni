package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.MVC.model.player.Worker;

import java.io.Serializable;

/**
 * DTO copy of the class {@link Worker Worker}
 */

public class DTOworker implements Serializable {

    private String masterUsername;
    private DTOposition position;

    /**
     * Initializes the DTOworker
     *
     * @param worker indicates his equivalent in server storage
     */
    public DTOworker(Worker worker) {
        this.masterUsername = worker.getMaster().getUsername();
        this.position = new DTOposition(worker.getPosition());
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
     * Returns the {@link DTOposition DTOposition} where the Worker is settled
     *
     * @return the {@link DTOposition DTOposition}
     */
    public DTOposition getPosition() { return position; }


    /**
     * Generates a String which contains the DTOworker's variables values
     *
     * @return a String with the DTOworker's attributes
     */
    @Override
    public String toString() {
        return "Master: "+getMasterUsername()+" , ("+getPosition().getX()+","+getPosition().getY()+")";
    }
}
