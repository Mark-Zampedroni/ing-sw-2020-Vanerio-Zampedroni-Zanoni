package it.polimi.ingsw.mvc.view.gui.objects3d.animation;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
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

/**
 * 3D object, it shows that a certain action can be done on a tile (position) on the SubScene
 */
public class ActionAnimation extends TrackedGroup {

    private static Image moveEffect;
    private static Image selectWorkerEffect;
    private static Image buildEffect;
    private static Image addWorkerEffect;
    private static final double SQUARES_OFFSET = 0.5; //0.5
    private static final int TIME = 500;
    private static final int NUMBER_OF_SQUARES = 3;
    private static final double SX = 0.6;
    private static final double SY = -5;
    private static final double SZ = 3.75;
    private static final String MOVE_EFFECT_TEXTURE = "/Images/3D/Texture/effects/playermoveindicator_blue.png";
    private static final String SELECT_WORKER_TEXTURE = "/Images/3D/Texture/effects/playermoveindicator_yellow.png";
    private static final String BUILD_EFFECT_TEXTURE = "/Images/3D/Texture/effects/playerplaceindicator_blue.png";
    private static final String ADD_WORKER_EFFECT_TEXTURE = "/Images/3D/Texture/effects/playerplaceindicator_gold.png";
    private static final List<Timeline> t = new ArrayList<>();

    /**
     * Constructor for the animation.
     * It loads each Image only the first time it's called, and stores is as a static
     * variable of the class.
     *
     * @param type        action for which the animation is created
     * @param coordinates coordinates where the animation will be placed
     */
    public ActionAnimation(Action type, BoardCoordinates3D coordinates) {
        super(-268.3, -243.4, 0.3,
                -3.6, -7.4, -9.7);
        if (moveEffect == null) effectSetup();
        createEffect(getTexture(type));
        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);
        setCoordinates(coordinates);
    }

    /**
     * Loads the images and saves them in static variables
     */
    private static void effectSetup() {
        moveEffect = new Image(MOVE_EFFECT_TEXTURE, true);
        selectWorkerEffect = new Image(SELECT_WORKER_TEXTURE, true);
        buildEffect = new Image(BUILD_EFFECT_TEXTURE, true);
        addWorkerEffect = new Image(ADD_WORKER_EFFECT_TEXTURE, true);
    }

    /**
     * Stops the animation and deletes it
     */
    public static void clear() {
        t.forEach(Timeline::stop);
        t.clear();
    }

    /**
     * Getter for the texture used as base for the animation
     *
     * @param action action for which the animation is created
     * @return the image used
     */
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

    /**
     * Creates the effect of the animation using as base the Image given.
     *
     * @param texture image used by the animation
     */
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

    /**
     * Creates an ImageView showing the Image given
     *
     * @param number  number of the ImageView in the animation group (0 up to 2)
     * @param texture image to show on the ImageView
     * @return the ImageView created
     */
    private ImageView createActionAnimation(int number, Image texture) {
        ImageView element = new ImageView(texture);
        NodeOperation.setScale(element, 0.01);

        element.setTranslateZ(-SQUARES_OFFSET * number);
        t.add(startAnimation(element, number));
        return element;
    }

    /**
     * Starts the animation
     *
     * @param node   the ImageView to animate
     * @param number the number of the ImageView in the animation group
     * @return the Timeline of the animation
     */
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
