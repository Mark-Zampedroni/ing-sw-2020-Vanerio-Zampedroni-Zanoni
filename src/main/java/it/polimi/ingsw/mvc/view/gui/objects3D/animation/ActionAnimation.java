package it.polimi.ingsw.mvc.view.gui.objects3D.animation;

import it.polimi.ingsw.mvc.view.gui.objects3D.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.NodeOperation;
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

    private static Image MOVE_EFFECT;
    private static Image SELECT_WORKER_EFFECT;
    private static Image BUILD_EFFECT;
    private static Image ADD_WORKER_EFFECT;
    final double SQUARES_OFFSET = 0.5; //0.5
    final int time = 500;
    final int numberOfSquares = 3;
    final double sX = 0.6;
    final double sY = -5;
    //final double CIRCLES_OFFSET = 0.65; //0.65
    final double sZ = 3.75;
    private final String MOVE_EFFECT_TEXTURE = "/texture/effects/playermoveindicator_blue.png";
    private final String SELECT_WORKER_TEXTURE = "/texture/effects/playermoveindicator_yellow.png";
    private final String BUILD_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_blue.png";
    private final String ADD_WORKER_EFFECT_TEXTURE = "/texture/effects/playerplaceindicator_gold.png";
    private final List<ImageView> e = new ArrayList<>();

    public ActionAnimation(Action type, BoardCoords3D coords) {

        super(-268.3, -243.4, 0.3,
                -3.6, -7.4, -9.7);

        if (MOVE_EFFECT == null) {
            MOVE_EFFECT = new Image(MOVE_EFFECT_TEXTURE, true);
            SELECT_WORKER_EFFECT = new Image(SELECT_WORKER_TEXTURE, true);
            BUILD_EFFECT = new Image(BUILD_EFFECT_TEXTURE, true);
            ADD_WORKER_EFFECT = new Image(ADD_WORKER_EFFECT_TEXTURE, true);
        }

        createEffect(getTexture(type));

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);

        setCoords(coords);
    }

    public void changeTexture(Action action) {
        e.forEach(a -> a.setImage(getTexture(action)));
    }

    private Image getTexture(Action action) {
        switch (action) {
            case SELECT_WORKER:
                return SELECT_WORKER_EFFECT;
            case ADD_WORKER:
                return ADD_WORKER_EFFECT;
            case BUILD:
                return BUILD_EFFECT;
            case MOVE:
            default:
                return MOVE_EFFECT;
        }
    }

    private void createEffect(Image texture) {
        Rotate xr;

        getChildren().addAll(
                createActionAnimation(0, texture),
                createActionAnimation(1, texture),
                createActionAnimation(2, texture)
        );

        getTransforms().add(xr = new Rotate(0, Rotate.X_AXIS));
        xr.setAngle(90);
        NodeOperation.setTranslate(this, sX, sY, sZ);
    }

    private ImageView createActionAnimation(int number, Image texture) {
        ImageView element = new ImageView(texture);
        e.add(element);
        NodeOperation.setScale(element, 0.01);

        element.setTranslateZ(-SQUARES_OFFSET * number);
        startAnimation(element, number);
        return element;
    }

    private void startAnimation(Node node, int number) {
        double startOpacity = (double) (numberOfSquares - number) / (double) numberOfSquares;
        double startPosition = SQUARES_OFFSET * number;
        double endOpacity = (double) (numberOfSquares - number - 1) / (double) numberOfSquares;
        double endPosition = startPosition + SQUARES_OFFSET;

        Timeline animation = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(node.translateZProperty(), startPosition),
                        new KeyValue(node.opacityProperty(), startOpacity)),
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
