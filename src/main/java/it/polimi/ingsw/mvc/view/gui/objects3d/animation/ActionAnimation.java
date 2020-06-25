package it.polimi.ingsw.mvc.view.gui.objects3d.animation;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
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

import java.util.ArrayList;
import java.util.List;


public class ActionAnimation extends TrackedGroup {

    private static Image moveEffect;
    private static Image selectWorkerEffect;
    private static Image buildEffect;
    private static Image addWorkerEffect;
    static final double SQUARES_OFFSET = 0.5; //0.5
    static final int TIME = 500;
    static final int NUMBER_OF_SQUARES = 3;
    static final double SX = 0.6;
    static final double SY = -5;
    //final double CIRCLES_OFFSET = 0.65; //0.65
    static final double SZ = 3.75;
    private static final String MOVE_EFFECT_TEXTURE = "/texture/effects/playermoveindicator_blue.png";
    private static final String SELECT_WORKER_TEXTURE = "/texture/effects/playermoveindicator_yellow.png";
    private static final String BUILD_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_blue.png";
    private static final String ADD_WORKER_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_gold.png";
    private static final List<Timeline> t = new ArrayList<>();

    public ActionAnimation(Action type, BoardCoords3D coords) {

        super(-268.3, -243.4, 0.3,
                -3.6, -7.4, -9.7);

        if (moveEffect == null) {
            effectSetup();
        }

        createEffect(getTexture(type));
        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);
        setCoords(coords);
    }
    
    private static void effectSetup(){
        moveEffect = new Image(MOVE_EFFECT_TEXTURE, true);
        selectWorkerEffect = new Image(SELECT_WORKER_TEXTURE, true);
        buildEffect = new Image(BUILD_EFFECT_TEXTURE, true);
        addWorkerEffect = new Image(ADD_WORKER_EFFECT_TEXTURE, true);
    }

    private Image getTexture(Action action) {
        switch (action) {
            case SELECT_WORKER:
                return selectWorkerEffect;
            case ADD_WORKER:
                return addWorkerEffect;
            case BUILD:
                return buildEffect;
            case MOVE:
            default:
                return moveEffect;
        }
    }

    private void createEffect(Image texture) {
        Rotate xr;

        getChildren().addAll(
                createActionAnimation(0, texture),
                createActionAnimation(1, texture),
                createActionAnimation(2, texture)
        );
        xr = new Rotate(0, Rotate.X_AXIS);
        getTransforms().add(xr);
        xr.setAngle(90);
        NodeOperation.setTranslate(this, SX, SY, SZ);
    }

    public static void clear() {
        t.forEach(Timeline::stop);
        t.clear();
    }

    private ImageView createActionAnimation(int number, Image texture) {
        ImageView element = new ImageView(texture);
        NodeOperation.setScale(element, 0.01);

        element.setTranslateZ(-SQUARES_OFFSET * number);
        t.add(startAnimation(element, number));
        return element;
    }

    private Timeline startAnimation(Node node, int number) {
        double startOpacity = (double) (NUMBER_OF_SQUARES - number) / (double) NUMBER_OF_SQUARES;
        double startPosition = SQUARES_OFFSET * number;
        double endOpacity = (double) (NUMBER_OF_SQUARES - number - 1) / (double) NUMBER_OF_SQUARES;
        double endPosition = startPosition + SQUARES_OFFSET;

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateZProperty(), startPosition),
                        new KeyValue(node.opacityProperty(), startOpacity)),
                new KeyFrame(Duration.millis(TIME),
                        new KeyValue(node.translateZProperty(),
                                endPosition, Interpolator.LINEAR),
                        new KeyValue(node.opacityProperty(),
                                endOpacity, Interpolator.LINEAR)
                )
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        return animation;
    }

}
