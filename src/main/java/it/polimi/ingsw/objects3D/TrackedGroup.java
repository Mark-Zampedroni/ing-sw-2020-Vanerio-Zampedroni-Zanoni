package it.polimi.ingsw.objects3D;

import it.polimi.ingsw.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.objects3D.utils.NodeOperation;
import javafx.scene.Group;

public abstract class TrackedGroup extends Group {

    protected final double zeroX, zeroZ, zeroY;
    private final double firstFloorOffset, secondFloorOffset, thirdFloorOffset;

    public final BoardCoords3D coords = new BoardCoords3D(0,0,0);

    public TrackedGroup(double zeroX, double zeroZ, double zeroY,
                        double firstFloorOffset, double secondFloorOffset, double thirdFloorOffset) {
        this.zeroX = zeroX;
        this.zeroZ = zeroZ;
        this.zeroY = zeroY;
        this.firstFloorOffset = firstFloorOffset;
        this.secondFloorOffset = secondFloorOffset;
        this.thirdFloorOffset = thirdFloorOffset;
    }


    public BoardCoords3D getCoords() {
        return coords;
    }

    public void setCoords(BoardCoords3D newCoords) {
        coords.setValues(newCoords.getValueX(),newCoords.getValueY(),newCoords.getValueZ());
        putOnCorrectTile();
    }

    private void putOnCorrectTile() {
        NodeOperation.putOnCorrectTile(this,coords);
    }

    public double getZeroX() {
        return zeroX;
    }

    public double getZeroZ() {
        return zeroZ;
    }

    public double getZeroY() {
        return zeroY;
    }

    public void setFloor(int height) {
        double offset;
        switch(height) {
            case 1: offset = firstFloorOffset; break;
            case 2: offset = secondFloorOffset; break;
            case 3: offset = thirdFloorOffset; break;
            default: offset = getZeroY();
        }
        setTranslateY(offset);
    }

}