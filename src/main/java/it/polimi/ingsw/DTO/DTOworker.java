package it.polimi.ingsw.DTO;

import it.polimi.ingsw.model.player.Worker;

/**
 * DTO copy of the class {@link Worker Worker}
 */

public class DTOworker {

    private DTOposition position;

    /**
     * Initializes the DTOworker
     *
     * @param worker indicates his equivalent in server storage
     */
    public DTOworker(Worker worker) {
        this.position = new DTOposition(worker.getPosition());
    }

    /**
     * Returns the {@link DTOposition DTOposition} where the Worker is settled
     *
     * @return the {@link DTOposition DTOposition}
     */
    public DTOposition getPosition() { return position; }

    /**
     * Getter for master
     *
     * @return the {@link DTOplayer DTOplayer} who owns the worker
     */
    public DTOplayer getMaster() {
        for(DTOplayer p : DTOsession.getInstance().getPlayers()) {
            for(DTOworker w : p.getWorkers()) {
                if(this.equals(w)) { return p; }
            }
        }
        return null; // Can't happen
    }

    /**
     * Generates a String which contains the DTOworker's variables values
     *
     * @return a String with the DTOworker's attributes
     */
    @Override
    public String toString() {
        return "{Master: "+getMaster()+
                " X: "+getPosition().getX()+
                " Y: "+getPosition().getY()+"}";
    }
/*
    public boolean equals(Worker worker) {
        return (position.equals(worker.getPosition()));
    }*/
}
