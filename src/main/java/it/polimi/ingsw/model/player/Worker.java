package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;

import java.io.Serializable;

/**
 * Used as a piece placed on the {@link it.polimi.ingsw.model.map.Board board} and moved
 * by a {@link Player player}, it can perform certain actions
 */
public class Worker implements Serializable {

    private Position position;

    /**
     * Initializes worker on (-1,-1), it's not yet placed on the board
     */
    public Worker() {
        position = new Position(-1,-1); // Worker on (-1,-1) only if not placed on Board
    }

    /**
     * Initializes the worker on an already valid {@link Position position}
     *
     * @param position starting {@link Position position} on the {@link it.polimi.ingsw.model.map.Board board}
     */
    public Worker(Position position) {
        this.position = position;
    }

    /**
     * Returns a copy of the {@link Position position} where the Worker is settled
     *
     * @return a copy of {@link Position position}
     */
    public Position getPosition() { return position.copy(); }

    /**
     * Puts the worker on {@link Position position}, successful only if it's not already occupied
     *
     * @param position target position
     */
    public void setPosition(Position position) {
        if(position.getWorker() == null) {
            this.position = position.copy();
        }
        else { System.out.println("[!] Worker cant be moved on Position, it's occupied by: "+position.getWorker()); }
    }

    /**
     * Puts the worker on the {@link Position position} with coordinates (x,y)
     *
     * @param x coordinate
     * @param y coordinate
     */
    public void setPosition(int x, int y) { setPosition(new Position(x,y)); }

    /**
     * Switches position with another worker
     *
     * @param worker worker to switch the position with
     */
    public void switchPosition(Worker worker) {
        Position temp = position.copy();
        position = worker.position.copy();
        worker.setPosition(temp);
    }

    /**
     * Getter for master
     *
     * @return the {@link Player player} who owns the worker
     */
    public Player getMaster() {
        for(Player p : Session.getInstance().getPlayers()) {
            for(Worker w : p.getWorkers()) {
                if(this == w) { return p; }
            }
        }
        return null; // Can't happen
    }

    /**
     * Generates a String which contains the worker's variables values
     *
     * @return a String with the worker's attributes
     */
    @Override
    public String toString() {
        return "{Master: "+getMaster()+
                " X: "+getPosition().getX()+
                " Y: "+getPosition().getY()+"}";
    }

}
