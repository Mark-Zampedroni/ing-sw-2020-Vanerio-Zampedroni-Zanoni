package it.polimi.ingsw.model;

// Finita per v6 del UML

public class Position {

    private int x;
    private int y;

    // Valid values are 0 <= x < 6 and 0 <= y < 6
    public Position(int x, int y) {
            this.x = x;
            this.y = y;
    }

    public boolean isSameAs(Position p2) {
            return (getDistanceFrom(p2) == 0);
    }

    @Override
    public Position clone() {
            return new Position(x, y);
    }

    // Returns distance as the number of Tiles
    public int getDistanceFrom(Position p2) {
        int deltax = Math.abs(this.x - p2.getX());
        int deltay = Math.abs(this.y - p2.getY());
        int diagonal = Math.min(deltax, deltay);
        int straight = Math.max(deltax, deltay) - diagonal;

        return (int)(diagonal * Math.sqrt(2) + straight);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
