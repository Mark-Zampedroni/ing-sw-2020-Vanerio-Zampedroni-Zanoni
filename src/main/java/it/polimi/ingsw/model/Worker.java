package it.polimi.ingsw.model;

// MARK -- OK

public class Worker {

    private Position position;
    private Player master;

    public Worker(Player master) {
        this.master = master;
    }

    // Returns a copy of Position, so it can't be changed
    public Position getPosition() { return position.copy(); }

    public void setPosition(Position position) { this.position = position; }

    public Player getMaster() { return master; }

}
