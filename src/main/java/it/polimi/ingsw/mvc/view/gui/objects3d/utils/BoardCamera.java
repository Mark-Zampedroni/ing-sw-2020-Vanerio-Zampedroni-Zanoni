package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.ScrollEvent;

/**
 * Camera of the 3D board
 */
public class BoardCamera extends PerspectiveCamera {

    /**
     * Constructor of the camera.
     * Starts the zoom event on mouse scroll
     *
     * @param scene SubScene containing the 3D board
     */
    public BoardCamera(SubScene scene) {
        super(true);
        setNearClip(1);
        setFarClip(1000);
        translateZProperty().set(-70);
        translateYProperty().set(-3);

        scene.setCamera(this);

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double zoom = (event.getDeltaY() > 0) ? 0.95 : 1.05;
            if (!(zoom < 1 && getTranslateZ() > -20) && !(zoom > 1 && getTranslateZ() < -95)) {
                translateZProperty().set((getTranslateZ()) * zoom);
            }
        });
    }


}
