package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;

import java.io.IOException;


public class TowerObj extends TrackedGroup {

    private static final String BOTTOM_OBJ = "/obj/BuildingBlock01.fxml";
    private static final String BOTTOM_TEXTURE = "/texture/BuildingBlock01_v001.png";
    private static final String MIDDLE_OBJ = "/obj/BuildingBlock02.fxml";
    private static final String MIDDLE_TEXTURE = "/texture/BuildingBlock02_v001.png";
    private static final String TOP_OBJ = "/obj/BuildingBlock03.fxml";
    private static final String TOP_TEXTURE = "/texture/BuildingBlock03_v001.png";
    private static final String DOME_OBJ = "/obj/Dome.fxml";
    private static final String DOME_TEXTURE = "/texture/Dome.png";
    private static PhongMaterial topMaterial;
    private static PhongMaterial midMaterial;
    private static PhongMaterial botMaterial;
    private static PhongMaterial domeMaterial;
    private Node bottom;
    private Node middle;
    private Node top;
    private Node dome;
    private int height;

    public TowerObj(BoardCoords3D coords) throws IOException {
        this(coords, 0);
    }

    public TowerObj(BoardCoords3D coords, int height) throws IOException {
        super(-11.95, 12.53, 0.5,
                0, 0, 0);

        if (topMaterial == null) {
            loadAssets();
        }

        bottom = NodeOperation.getModel(BOTTOM_OBJ, botMaterial);
        middle = NodeOperation.getModel(MIDDLE_OBJ, midMaterial);
        top = NodeOperation.getModel(TOP_OBJ, topMaterial);
        dome = NodeOperation.getModel(DOME_OBJ, domeMaterial);

        getChildren().addAll(bottom, middle, top, dome);

        bottom.setVisible(false);
        middle.setVisible(false);
        top.setVisible(false);
        dome.setVisible(false);

        middle.setTranslateY(-3.85);
        top.setTranslateY(-7.7);

        NodeOperation.setTranslate(this, zeroX, zeroZ, zeroY);

        setCoords(coords);

        this.height = height;
        setHeight(height);
    }

    private static void loadAssets() {
        topMaterial = NodeOperation.getTexture(TOP_TEXTURE);
        midMaterial = NodeOperation.getTexture(MIDDLE_TEXTURE);
        botMaterial = NodeOperation.getTexture(BOTTOM_TEXTURE);
        domeMaterial = NodeOperation.getTexture(DOME_TEXTURE);
    }

    public void placeDome() {
        if (height == 1) {
            dome.setTranslateY(dome.getTranslateY() - 4.3);
        } else if (height == 2) {
            dome.setTranslateY(dome.getTranslateY() - 8);
        } else if (height == 3) {
            dome.setTranslateY(dome.getTranslateY() - 9.8);
        }
        dome.setVisible(true);
    }

    public void increaseHeight() {
        if (!dome.visibleProperty().getValue()) {
            height++;
            setHeight(height);
        }
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int newHeight) {
        if (newHeight > 2) {
            top.setVisible(true);
        }
        if (newHeight > 1) {
            middle.setVisible(true);
        }
        if (newHeight > 0) {
            bottom.setVisible(true);
        }
    }

    @Override
    public void setFloor(int height) { /*Ignore, towers cant float */ }

}
