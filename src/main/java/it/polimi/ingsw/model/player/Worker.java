package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.map.Position;

public class Worker {

    private Position position;
    private Player master;

    //da cambiare mi ha dato problemi nel testing e non capivo cosa fosse
    public Worker(Player master) {
        this.master = master;
        position = new Position(0,0); // May be changed on a later commit
    }

    public Worker(Player master, Position position) {
        this.master = master;
        this.position = position;
    }

    // Returns a copy of Position, so it can't be changed
    public Position getPosition() { return position.copy(); }

    public void setPosition(Position position) {
        if(position.getWorker() == null) {
            this.position = position.copy();
        }
        else { System.out.println("[!] Worker cant be moved on Position, it's occupied by: "+position.getWorker()); }
    }
    public void setPosition(int x, int y) { setPosition(new Position(x,y)); }

    public void switchPosition(Worker worker) {
        Position temp = position.copy();
        position = worker.position;
        worker.setPosition(position);
    }

    public Player getMaster() { return master; }

    @Override
    public String toString() {
        return "{Master: "+master+
                " X: "+String.valueOf(getPosition().getX())+
                " Y: "+String.valueOf(getPosition().getY())+"}";
    }

}
