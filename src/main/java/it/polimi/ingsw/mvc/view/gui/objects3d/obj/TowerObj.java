package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;

import java.io.IOException;

/**
 * 3D object representing the {@link it.polimi.ingsw.mvc.model.map.Tile tower of a tile} on the 3D SubScene
 */
public class TowerObj extends TrackedGroup {

    private static final String BOTTOM_OBJ = "/obj/BuildingBlock01.fxml";
    private static final String BOTTOM_TEXTURE = "/texture/BuildingBlock01_v001.png";
    private static final String MIDDLE_OBJ = "/obj/BuildingBlock02.fxml";
    private static final String MIDDLE_TEXTURE = "/texture/BuildingBlock02_v001.png";
    private static final String TOP_OBJ = "/obj/BuildingBlock03.fxml";
    private static final String TOP_TEXTURE = "/texture/BuildingBlock03_v001.png";
    private static final String DOME_OBJ = "/obj/Dome.fxml";
    private static final String DOME_TEXTURE = "/texture/Dome.png";
    private static PhongMaterial topTexture;
    private static PhongMaterial midTexture;
    private static PhongMaterial botTexture;
    private static PhongMaterial domeTexture;
    private Node bottom;
    private Node middle;
    private Node top;
    private Node dome;
    private int height;

    /**
     * Constructor, places a tower node at the coordinates given. It's height is 0
     *
     * @param coordinates coordinates of the tile where to place the tower
     * @throws IOException if any of the fxml files can't be read
     */
    public TowerObj(BoardCoordinates3D coordinates) throws IOException {
        this(coordinates, 0);
    }

    /**
     * Constructor, places a tower node at the coordinates given. It's starting height is
     * defined by the argument
     *
     * @param coordinates coordinates of the tile where to place the tower
     * @param height      starting height (number of floors shown)
     * @throws IOException if any of the fxml files can't be read
     */
    public TowerObj(BoardCoordinates3D coordinates, int height) throws IOException {
        super(-11.95, 12.53, 0.5,
                0, 0, 0);

        if (topTexture == null) {
            loadAssets();
        }

        bottom = NodeOperation.getModel(BOTTOM_OBJ, botTexture);
        middle = NodeOperation.getModel(MIDDLE_OBJ, midTexture);
        top = NodeOperation.getModel(TOP_OBJ, topTexture);
        dome = NodeOperation.getModel(DOME_OBJ, domeTexture);

        getChildren().addAll(bottom, middle, top, dome);

        bottom.setVisible(false);
        middle.setVisible(false);
        top.setVisible(false);
        dome.setVisible(false);

        middle.setTranslateY(-3.85);
        top.setTranslateY(-7.7);

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);

        setCoordinates(coordinates);

        this.height = height;
        setHeight(height);
    }

    /**
     * Loads the textures of the tower floors.
     * It's done only on time at the first call of the constructor,
     * then the textures are saved as static variables within the class
     */
    private static void loadAssets() {
        topTexture = NodeOperation.getTexture(TOP_TEXTURE);
        midTexture = NodeOperation.getTexture(MIDDLE_TEXTURE);
        botTexture = NodeOperation.getTexture(BOTTOM_TEXTURE);
        domeTexture = NodeOperation.getTexture(DOME_TEXTURE);
    }

    /**
     * Adds a dome at the current height
     */
    public void placeDome() {
        if (height == 1)
            dome.setTranslateY(dome.getTranslateY() - 4.3);
        else if (height == 2)
            dome.setTranslateY(dome.getTranslateY() - 8);
        else if (height == 3)
            dome.setTranslateY(dome.getTranslateY() - 9.8);
        dome.setVisible(true);
    }

    /**
     * Increases the height of the tower by 1.
     * Ignores the call if the max height is already reached or there is a dome blocking
     * the building
     */
    public void increaseHeight() {
        if (!dome.visibleProperty().getValue()) {
            height++;
            setHeight(height);
        }
    }

    /**
     * Getter for the height of the tower
     *
     * @return height of the tower (number of floors built)
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the tower
     *
     * @param newHeight height being set
     */
    private void setHeight(int newHeight) {
        if (newHeight > 2)
            top.setVisible(true);
        if (newHeight > 1)
            middle.setVisible(true);
        if (newHeight > 0)
            bottom.setVisible(true);
    }

    /**
     * Implementation ignored, as all the tower groups start
     * at stay at height 0 (base of the tower)
     *
     * @param height height of the tower, may be 1, 2 or 3
     */
    @Override
    public void setFloor(int height) { /*Ignore, towers cant float */ }

}
