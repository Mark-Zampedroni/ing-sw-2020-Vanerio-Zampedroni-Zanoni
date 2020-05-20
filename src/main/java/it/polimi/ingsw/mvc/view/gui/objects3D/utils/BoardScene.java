package it.polimi.ingsw.mvc.view.gui.objects3D.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class BoardScene extends SubScene {

    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    private final DoubleProperty prevAngleX = new SimpleDoubleProperty(0);
    private final DoubleProperty prevAngleY = new SimpleDoubleProperty(0);

    private final DoubleProperty prevPosX = new SimpleDoubleProperty(0);
    private final DoubleProperty prevPosY = new SimpleDoubleProperty(0);

    private final Group objects;

    private final String BACKGROUND_COLOR = "#47cbf4";
    private final double X_ROTATION_SPEED = 0.35;
    private final double Y_ROTATION_SPEED = 0.3;

    public BoardScene(Group objects, Node board, float width, float height) {
        super(objects, width, height, true, SceneAntialiasing.DISABLED);
        this.objects = objects;
        setFill(Color.web(BACKGROUND_COLOR));
        addLight();
        initRotation(board);
    }

    private void addLight() {
        AmbientLight light = new AmbientLight();
        light.setColor(Color.WHITE);
        objects.getChildren().addAll(light);
    }

    private void initRotation(Node board) {
        Rotate xRotate;
        Rotate yRotate;
        objects.getTransforms().addAll(
                xRotate = new Rotate(0,Rotate.X_AXIS),
                yRotate = new Rotate(0,Rotate.Y_AXIS)
        );
        yRotate.setPivotX(board.getTranslateX());
        yRotate.setPivotZ(board.getTranslateZ());

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        angleX.set(37); // Starting angle

        setOnMousePressed(event -> {
                prevPosX.setValue(event.getSceneX());
                prevPosY.setValue(event.getSceneY());
                prevAngleY.setValue(angleY.getValue());
                prevAngleX.setValue(angleX.getValue());
        });

        setOnMouseDragged(event -> {
            double newAngleX = prevAngleX.getValue() - (prevPosY.getValue() - event.getSceneY())*Y_ROTATION_SPEED;
            if(newAngleX >= 36.5 && newAngleX <= 90) {angleX.set(newAngleX); }

            if(Math.abs(prevAngleY.getValue()) > 10000000) { angleY.set(0); }
            angleY.set(prevAngleY.getValue() - (prevPosX.getValue() - event.getSceneX())*X_ROTATION_SPEED);
        });
    }

}
