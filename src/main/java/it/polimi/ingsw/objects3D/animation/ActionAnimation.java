package it.polimi.ingsw.objects3D.animation;

import it.polimi.ingsw.objects3D.TrackedGroup;
import it.polimi.ingsw.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.objects3D.utils.NodeOperation;
import it.polimi.ingsw.utility.enumerations.Action;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


public class ActionAnimation extends TrackedGroup {



    private final String MOVE_EFFECT_TEXTURE = "/texture/effects/playermoveindicator_blue.png";
    private final String SELECT_WORKER_TEXTURE = "/texture/effects/playermoveindicator_yellow.png";
    private final String BUILD_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_blue.png";
    private final String ADD_WORKER_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_gold.png";

    final double SQUARES_OFFSET = 0.5; //0.5
    final double CIRCLES_OFFSET = 0.65; //0.65

    final int time = 500;
    final int numberOfSquares = 3;

    final double sX = 0.6;
    final double sY = -5;
    final double sZ = 3.75;

    public ActionAnimation(Action type, BoardCoords3D coords) {

        super(-268.3,-243.4,0.3, // Position offset
        // super(-268.6,-233.3,0.3, // Position offset
                -3.6,-7.4,-9.7); // Floors height offset

        switch(type) {
            case MOVE:
                createEffect(SQUARES_OFFSET,MOVE_EFFECT_TEXTURE);
                break;
            case BUILD:
                createEffect(CIRCLES_OFFSET,BUILD_EFFECT_TEXTURE);
                break;
            case ADD_WORKER:
                createEffect(CIRCLES_OFFSET,ADD_WORKER_EFFECT_TEXTURE);
                break;
            case SELECT_WORKER:
                createEffect(SQUARES_OFFSET,SELECT_WORKER_TEXTURE);
                break;
        }

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);

        /*
        setOnMouseClicked(event -> {
            setTranslateX(getTranslateX()+0.1);
            System.out.println(getTranslateX());
        });
        */
        setCoords(coords);
    }

    private void createEffect(double offset, String texture) {
        Rotate xr;

        getChildren().addAll(
                createActionAnimation(0, offset, texture),
                createActionAnimation(1, offset, texture),
                createActionAnimation(2, offset, texture)
        );

        getTransforms().add(xr = new Rotate(0,Rotate.X_AXIS));
        xr.setAngle(90);
        NodeOperation.setTranslate(this,sX,sY,sZ);
    }

    private ImageView createActionAnimation(int number, double offset, String texture) {
        ImageView element = new ImageView(new Image(texture, true));
        NodeOperation.setScale(element,0.01);

        element.setTranslateZ(-offset*number);
        startAnimation(element, offset, number);
        return element;
    }

    private void startAnimation(Node node, double offset, int number) {
        double startOpacity = (double)(numberOfSquares-number)/(double)numberOfSquares;
        double startPosition = offset*number;
        double endOpacity = (double)(numberOfSquares-number-1)/(double)numberOfSquares;
        double endPosition = startPosition+offset;
        //System.out.println("START: "+startOpacity+" in "+startPosition);
        //System.out.println("END: "+endOpacity+" in "+endPosition+"\n");

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateZProperty(), startPosition),
                        new KeyValue(node.opacityProperty(),startOpacity)),
                new KeyFrame(Duration.millis(time),
                        new KeyValue(node.translateZProperty(),
                                endPosition, Interpolator.LINEAR),
                        new KeyValue(node.opacityProperty(),
                                endOpacity, Interpolator.LINEAR)
                )
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

}
