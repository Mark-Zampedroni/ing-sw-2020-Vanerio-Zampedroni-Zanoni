package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoordinates3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.ObservableTileEvent;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.scene.Group;
import javafx.scene.Node;

import java.io.IOException;

/**
 * 3D object representing the {@link it.polimi.ingsw.mvc.model.map.Board board} on the 3D SubScene
 */
public class BoardObj extends Group {

    private static final String CLIFF_OBJ = "/obj/Cliff.fxml";
    private static final String ALLBOARD_TEXTURE = "/texture/Cliff_v001.png";
    private static final String OUTERWALL1_OBJ = "/obj/OuterWall1.fxml";
    private static final String OUTERWALL2_OBJ = "/obj/OuterWall2.fxml";
    private static final String BOARD_OBJ = "/obj/Board.fxml";
    private static final String INNERWALL_OBJ = "/obj/InnerWalls.fxml";
    private static final String SEA_OBJ = "/obj/Sea.fxml";
    private static final String SEA_TEXTURE = "/texture/Sea_v002.png";

    private Node sea;
    private Node plane;
    private Node cliff;
    private Node innerWall;
    private Node outerWall1;
    private Node outerWall2;

    private TileObj[][] tiles = new TileObj[5][5];

    /**
     * Constructor
     *
     * @param tileEventResponse Observable that triggers when a tile or an object on a tile is clicked
     * @throws IOException if any of the fxml files can't be read
     */
    public BoardObj(ObservableTileEvent tileEventResponse) throws IOException {

        cliff = NodeOperation.getModel(CLIFF_OBJ, ALLBOARD_TEXTURE);
        outerWall1 = NodeOperation.getModel(OUTERWALL1_OBJ, ALLBOARD_TEXTURE);
        outerWall2 = NodeOperation.getModel(OUTERWALL2_OBJ, ALLBOARD_TEXTURE);
        innerWall = NodeOperation.getModel(INNERWALL_OBJ, ALLBOARD_TEXTURE);
        plane = NodeOperation.getModel(BOARD_OBJ, ALLBOARD_TEXTURE);
        sea = NodeOperation.getModel(SEA_OBJ, SEA_TEXTURE);
        getChildren().addAll(cliff, outerWall1, outerWall2, innerWall, plane, sea);
        initBoard();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                tiles[x][y] = new TileObj(this, x, y, tileEventResponse);
                getChildren().add(tiles[x][y]);
            }
        }
    }

    /**
     * Initializes the 3D board elements, it's built
     * using various models and textures
     */
    private void initBoard() {
        NodeOperation.setScale(sea, 2.3);
        NodeOperation.setTranslate(sea, 1.5, -0.6, 7.5);  //71,203,244

        cliff.setTranslateY(5);
        NodeOperation.setScale(cliff, 20);

        NodeOperation.setScale(outerWall1, 2.5);
        NodeOperation.setTranslate(outerWall1, 9, 8.6, 4.45);

        NodeOperation.setScale(outerWall2, 2.5);
        NodeOperation.setTranslate(outerWall2, 0.34, -0.15, 1.75);

        NodeOperation.setScale(plane, 2.5);
        NodeOperation.setTranslate(plane, 0.3, -0.1, 0.45);

        NodeOperation.setScale(innerWall, 2.5);
        NodeOperation.setTranslate(innerWall, 8.9, 8.55, 4.3);

        NodeOperation.setTranslate(this, 0, 10, 0);
    }

    /**
     * Getter for the 3D tile object at the requested coordinates
     *
     * @param x coordinate on the X axis
     * @param y coordinate on the Y axis
     * @return the tile at (x,y)
     */
    public TileObj getTile(int x, int y) {
        return (x >= 0 && x < 5 && y >= 0 && y < 5) ? tiles[x][y] : null;
    }

    /**
     * Creates a worker object and adds outside the board with visibility false.
     * When a Tile "adds" a worker of the same color it will be moved there.
     * (In other words it preloads the worker to keep delay at minimum)
     *
     * @param color color of the worker
     * @return worker created
     */
    public WorkerObj createWorker(Colors color) {
        try {
            return new WorkerObj(new BoardCoordinates3D(-1, -1, 0), color);
        } catch (Exception e) {
            return null;
        }
    }

}
