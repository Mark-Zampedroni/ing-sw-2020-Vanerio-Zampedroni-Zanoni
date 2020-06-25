package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

public class BoardCoords3D {

    private int xBoard;
    private int yBoard;
    private int zBoard;

    public BoardCoords3D(int xBoard, int yBoard, int zBoard) {
        this.xBoard = xBoard;
        this.yBoard = yBoard;
        this.zBoard = zBoard;
    }

    public int getValueX() {
        return xBoard;
    }

    public void setValueX(int x) {
        xBoard = x;
    }

    public int getValueY() {
        return yBoard;
    }

    public void setValueY(int y) {
        yBoard = y;
    }

    public int getValueZ() {
        return zBoard;
    }

    public void setValueZ(int z) {
        zBoard = z;
    }

    public void setValues(int x, int y, int z) {
        setValueX(x);
        setValueY(y);
        setValueZ(z);
    }

}
