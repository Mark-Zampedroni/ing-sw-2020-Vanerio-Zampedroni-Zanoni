package it.polimi.ingsw.mvc.view.gui.objects3D.obj;

import it.polimi.ingsw.mvc.view.gui.objects3D.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.NodeOperation;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

import java.util.Arrays;


public class WorkerObj extends TrackedGroup {

    private final String WORKER_OBJ = "/obj/MaleBuilder.fxml";

    private final SimpleDoubleProperty angleY = new SimpleDoubleProperty(0);
    private Rotate yRotate;
    private Node worker;

    public WorkerObj(BoardCoords3D coords, Colors color) throws Exception {
        super(-12.3,22.5,-2.1,
              -5.9,-9.4,-11.9); // Floors height offset

        getChildren().add(worker = NodeOperation.getModel(WORKER_OBJ, getTextureColor(color)));
        NodeOperation.setScale(this,2.8);

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY); //0.6,9.9
        initRotation();
        setCoords(coords);
        rotate(90);
    }

    private String getTextureColor(Colors color) {
        return "/texture/MaleBuilder_"+color.toString().toLowerCase()+"_v001.png";
    }

    @Override
    public void setCoords(BoardCoords3D newCoords) {
        BoardCoords3D difference = new BoardCoords3D(
                newCoords.getValueX() - getCoords().getValueX(),
                newCoords.getValueY() - getCoords().getValueY(),
                0
        );
        super.setCoords(newCoords);
        int contAxe = (difference.getValueX() != 0 && difference.getValueY() != 0) ? 2 : 1;
        double xValue = (difference.getValueX() < 0) ? 180 : 0;
        double yValue = (difference.getValueY() < 0) ? 270 : 90;
        yValue = (difference.getValueY() == 0) ? 0 : yValue;
        if(difference.getValueX() != 0 || difference.getValueY() != 0) {
            rotate((xValue + yValue) / contAxe);
        }
    }

    private void initRotation() {
        worker.getTransforms().addAll(
                yRotate = new Rotate(0,Rotate.Y_AXIS)
        );
        yRotate.angleProperty().bind(angleY);
    }

    private void rotate(double newAngle) {
        yRotate.setPivotX(worker.getTranslateX());
        yRotate.setPivotZ(worker.getTranslateZ());
        angleY.setValue(newAngle);
    }

}
