package it.polimi.ingsw.mvc.view.gui.objects3d;

import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import javafx.scene.Group;

/**
 * Group of 3D objects on the board subScene
 */
public abstract class TrackedGroup extends Group {

    /**
     * Relative position of the group
     */
    public final BoardCoordinates3D coordinates = new BoardCoordinates3D(0, 0, 0);
    protected final double zeroX;
    protected final double zeroZ;
    protected final double zeroY;
    private final double firstFloorOffset;
    private final double secondFloorOffset;
    private final double thirdFloorOffset;

    /**
     * Constructor
     *
     * @param zeroX             relative X coordinate of the group
     * @param zeroZ             relative Z coordinate of the group
     * @param zeroY             relative Y coordinate of the group
     * @param firstFloorOffset  offset to place a node on the first floor of a tower
     * @param secondFloorOffset offset to place a node on the second floor of a tower
     * @param thirdFloorOffset  offset to place a node on the third floor of a tower
     */
    public TrackedGroup(double zeroX, double zeroZ, double zeroY,
                        double firstFloorOffset, double secondFloorOffset, double thirdFloorOffset) {
        this.zeroX = zeroX;
        this.zeroZ = zeroZ;
        this.zeroY = zeroY;
        this.firstFloorOffset = firstFloorOffset;
        this.secondFloorOffset = secondFloorOffset;
        this.thirdFloorOffset = thirdFloorOffset;
    }

    /**
     * Gets the group relative coordinates
     *
     * @return the relative coordinates of the group
     */
    public BoardCoordinates3D getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the new relative coordinates of the group, and moves it on the scene
     *
     * @param newCoordinates new coordinates to set
     */
    public void setCoordinates(BoardCoordinates3D newCoordinates) {
        coordinates.setValues(newCoordinates.getValueX(), newCoordinates.getValueY(), newCoordinates.getValueZ());
        putOnCorrectTile();
    }

    /**
     * Moves the group to its relative coordinates
     */
    private void putOnCorrectTile() {
        NodeOperation.putOnCorrectTile(this, coordinates);
    }

    /**
     * Gets the X coordinate on the subScene
     *
     * @return X coordinate on the subScene
     */
    public double getZeroX() {
        return zeroX;
    }

    /**
     * Gets the Z coordinate on the subScene
     *
     * @return Z coordinate on the subScene
     */
    public double getZeroZ() {
        return zeroZ;
    }

    /**
     * Gets the Y coordinate on the subScene
     *
     * @return Y coordinate on the subScene
     */
    public double getZeroY() {
        return zeroY;
    }

    /**
     * Sets the default floor for the group.
     *
     * @param height height of the tower, may be 1, 2 or 3
     */
    public void setFloor(int height) {
        double offset;
        switch (height) {
            case 1:
                offset = firstFloorOffset;
                break;
            case 2:
                offset = secondFloorOffset;
                break;
            case 3:
                offset = thirdFloorOffset;
                break;
            default:
                offset = getZeroY();
        }
        setTranslateY(offset);
    }

}
