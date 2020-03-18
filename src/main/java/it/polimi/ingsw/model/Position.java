package it.polimi.ingsw.model;

// MARK -- OK

public class Position {

    private int x;
    private int y;

    // Valid values are 0 <= x < 6 and 0 <= y < 6
    public Position(int x, int y) {
            this.x = x;
            this.y = y;
    }

    public boolean equals(Position that) {
            return (getDistanceFrom(that) == 0);
    }

    public Position copy() {
        return new Position(x,y);
    }

    // Returns distance as the number of Tiles
    public int getDistanceFrom(Position that) {
        int deltax = Math.abs(this.x - that.getX());
        int deltay = Math.abs(this.y - that.getY());
        int diagonal = Math.min(deltax, deltay);
        int straight = Math.max(deltax, deltay) - diagonal;

        return (int)(diagonal * Math.sqrt(2) + straight);
    }

    // Null if there is no Worker on caller
    public Worker getWorker() {
        for(Player player : Session.getPlayers()) {
            for(Worker worker : player.getWorkers()) {
                if(this.equals(worker.getPosition())) {
                    return worker;
                }
            }
        }
        return null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
