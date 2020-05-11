package it.polimi.ingsw.objects3D.utils;

import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.ScrollEvent;

public class BoardCamera extends PerspectiveCamera {

    public BoardCamera(SubScene scene) {
        super(true);
        setNearClip(1);
        setFarClip(1000);
        translateZProperty().set(-70);
        translateYProperty().set(-3);

        scene.setCamera(this);

        scene.addEventHandler(ScrollEvent.SCROLL, event -> {
            double zoom = (event.getDeltaY() > 0) ? 0.95 : 1.05;
            //System.out.println(getTranslateZ() + " ZOOM: "+zoom);
            if(!(zoom < 1 && getTranslateZ() > -20) && !(zoom > 1 && getTranslateZ() < -95)) { // Limite zoom! se superato non fa nulla
                translateZProperty().set((getTranslateZ()) * zoom);
            }
        });
    }





}
