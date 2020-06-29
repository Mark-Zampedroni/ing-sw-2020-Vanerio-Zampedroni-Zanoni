package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

import java.io.IOException;

/**
 * 3D object representing a {@link it.polimi.ingsw.mvc.model.player.Worker worker} on the 3D SubScene
 */
public class WorkerObj extends TrackedGroup {

    private static final String WORKER_OBJ = "/Images/3D/Obj/MaleBuilder.fxml";

    private final SimpleDoubleProperty angleY = new SimpleDoubleProperty(0);
    private Rotate yRotate;
    private Node worker;
    private Colors color;

    /**
     * Constructor
     *
     * @param coordinates coordinates where the worker will be placed as its first position
     * @param color       color of the worker
     * @throws IOException if any of the fxml files used can't be read
     */
    public WorkerObj(BoardCoordinates3D coordinates, Colors color) throws IOException {
        super(-12.3, 22.5, -2.1,
                -5.9, -9.4, -11.9); // Floors height offset

        worker = NodeOperation.getModel(WORKER_OBJ, getTexturePath(color));
        getChildren().add(worker);
        this.color = color;
        NodeOperation.setScale(this, 2.8);

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY); //0.6,9.9
        initRotation();
        setCoordinates(coordinates);
        rotate(90);
    }

    /**
     * Gets the path of the texture colored as the parameter given
     *
     * @param color color of the texture (the texture will have the same color)
     * @return path of the texture
     */
    private String getTexturePath(Colors color) {
        return "/Images/3D/Texture/MaleBuilder_" + color.toString().toLowerCase() + "_v001.png";
    }

    /**
     * Getter for the color of the worker
     *
     * @return the color of the worker
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Rotates the worker to its default angle (around the Z axis)
     */
    public void resetRotation() {
        rotate(90);
    }

    /**
     * Sets the new position of the worker, rotates it on the direction of the movement
     *
     * @param newCoordinates new coordinates to set
     */
    @Override
    public void setCoordinates(BoardCoordinates3D newCoordinates) {
        BoardCoordinates3D difference = new BoardCoordinates3D(
                newCoordinates.getValueX() - getCoordinates().getValueX(),
                newCoordinates.getValueY() - getCoordinates().getValueY(),
                0
        );
        super.setCoordinates(newCoordinates);
        int contAxe = (difference.getValueX() != 0 && difference.getValueY() != 0) ? 2 : 1;
        double xValue = (difference.getValueX() < 0) ? 180 : 0;
        double yValue = (difference.getValueY() < 0) ? 270 : 90;
        yValue = (difference.getValueY() == 0) ? 0 : yValue;
        if (difference.getValueX() != 0 || difference.getValueY() != 0) {
            rotate((xValue + yValue) / contAxe);
        }
    }

    /**
     * Initializes the rotation event on mouse drag
     */
    private void initRotation() {
        yRotate = new Rotate(0, Rotate.Y_AXIS);
        worker.getTransforms().add(yRotate);
        yRotate.angleProperty().bind(angleY);
    }

    /**
     * Rotates the worker around the Z axis by the given angle
     *
     * @param newAngle angle of rotation
     */
    private void rotate(double newAngle) {
        yRotate.setPivotX(worker.getTranslateX());
        yRotate.setPivotZ(worker.getTranslateZ());
        angleY.setValue(newAngle);
    }

}
