package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

/**
 * Coordinates on the 3D board.
 * (X,Y) identify the tile on the board, Z the floor of the tower
 * (may be 0, 1, 2 or 3)
 */
public class BoardCoordinates3D {

    private int xBoard;
    private int yBoard;
    private int zBoard;

    /**
     * Constructor
     *
     * @param xBoard coordinate of the board X axis
     * @param yBoard coordinate of the board Y axis
     * @param zBoard height coordinate (floor 0, 1, 2 or 3 of the tower at X,Y)
     */
    public BoardCoordinates3D(int xBoard, int yBoard, int zBoard) {
        this.xBoard = xBoard;
        this.yBoard = yBoard;
        this.zBoard = zBoard;
    }

    /**
     * Getter for the X coordinate value
     *
     * @return X coordinate value
     */
    public int getValueX() {
        return xBoard;
    }

    /**
     * Setter for the X coordinate value
     */
    public void setValueX(int x) {
        xBoard = x;
    }

    /**
     * Getter for the Y coordinate value
     *
     * @return Y coordinate value
     */
    public int getValueY() {
        return yBoard;
    }

    /**
     * Setter for the Y coordinate value
     */
    public void setValueY(int y) {
        yBoard = y;
    }

    /**
     * Getter for the Z coordinate value (height)
     *
     * @return Z coordinate value
     */
    public int getValueZ() {
        return zBoard;
    }

    /**
     * Setter for the Z coordinate value (height)
     */
    public void setValueZ(int z) {
        zBoard = z;
    }

    /**
     * Setter for all the coordinates
     */
    public void setValues(int x, int y, int z) {
        setValueX(x);
        setValueY(y);
        setValueZ(z);
    }

}
