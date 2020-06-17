package it.polimi.ingsw.mvc.view.gui.objects3D.obj;

import it.polimi.ingsw.mvc.view.gui.objects3D.TrackedGroup;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.NodeOperation;
import javafx.scene.Node;
import javafx.scene.paint.PhongMaterial;


public class TowerObj extends TrackedGroup {

    private final String BOTTOM_OBJ = "/obj/BuildingBlock01.fxml";
    private final String BOTTOM_TEXTURE = "/texture/BuildingBlock01_v001.png";
    private final String MIDDLE_OBJ = "/obj/BuildingBlock02.fxml";
    private final String MIDDLE_TEXTURE = "/texture/BuildingBlock02_v001.png";
    private final String TOP_OBJ = "/obj/BuildingBlock03.fxml";
    private final String TOP_TEXTURE = "/texture/BuildingBlock03_v001.png";
    private final String DOME_OBJ = "/obj/Dome.fxml";
    private final String DOME_TEXTURE = "/texture/Dome.png";

    private Node bottom, middle, top;
    private Node dome;

    private static PhongMaterial TOP_MATERIAL, MID_MATERIAL, BOT_MATERIAL, DOME_MATERIAL;

    private int height;

    public TowerObj(BoardCoords3D coords) throws Exception {
        this(coords,0);
    }

    public TowerObj(BoardCoords3D coords, int height) throws Exception {
        super(-11.95, 12.53,0.5,
                0,0,0);

        if(TOP_MATERIAL == null) {
            TOP_MATERIAL = NodeOperation.getTexture(TOP_TEXTURE);
            MID_MATERIAL = NodeOperation.getTexture(MIDDLE_TEXTURE);
            BOT_MATERIAL = NodeOperation.getTexture(BOTTOM_TEXTURE);
            DOME_MATERIAL = NodeOperation.getTexture(DOME_TEXTURE);
        }

        getChildren().addAll(
                    bottom = NodeOperation.getModel(BOTTOM_OBJ, BOT_MATERIAL),
                    middle = NodeOperation.getModel(MIDDLE_OBJ, MID_MATERIAL),
                    top = NodeOperation.getModel(TOP_OBJ, TOP_MATERIAL),
                    dome = NodeOperation.getModel(DOME_OBJ, DOME_MATERIAL)
        );
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

    public void placeDome() {
        if(height == 1) { dome.setTranslateY(dome.getTranslateY()-4.3); }
        else if(height == 2) { dome.setTranslateY(dome.getTranslateY()-8); }
        else if(height == 3) { dome.setTranslateY(dome.getTranslateY()-9.8); }
        dome.setVisible(true);
    }

    public void increaseHeight() {
        if (!dome.visibleProperty().getValue()) {
            height++;
            setHeight(height);
        }
    }

    private void setHeight(int newHeight) {
        if(newHeight > 2) { top.setVisible(true); }
        if(newHeight > 1) { middle.setVisible(true); }
        if(newHeight > 0) { bottom.setVisible(true); }
    }

    public int getHeight() {
        return height;
    }

    @Override
    public void setFloor(int height) { /*Ignore, towers cant float */ }

}
