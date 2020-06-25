package it.polimi.ingsw.mvc.view.gui.objects3d.obj;

import it.polimi.ingsw.mvc.view.gui.objects3d.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.NodeOperation;
import it.polimi.ingsw.mvc.view.gui.objects3d.utils.ObservableTileEvent;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.scene.Group;
import javafx.scene.Node;

import java.io.IOException;

public class BoardObj extends Group {

    private static final String CLIFF_OBJ = "/obj/Cliff.fxml";
    private static final String ALLBOARD_TEXTURE = "/texture/Cliff_v001.png";
    private static final String OUTERWALL1_OBJ = "/obj/OuterWall1.fxml";
    private static final String OUTERWALL2_OBJ = "/obj/OuterWall2.fxml";
    private static final String BOARD_OBJ = "/obj/Board.fxml";
    private static final String INNERWALL_OBJ = "/obj/InnerWalls.fxml";
    private static final String SEA_OBJ = "/obj/Sea.fxml";
    private static final String SEA_TEXTURE = "/texture/Sea_v002.png";

    Node sea;
    Node plane;
    Node cliff;
    Node innerWall;
    Node outerWall1;
    Node outerWall2;

    TileObj[][] tiles = new TileObj[5][5];

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

    public TileObj getTile(int x, int y) {
        return (x >= 0 && x < 5 && y >= 0 && y < 5) ? tiles[x][y] : null;
    }

    public WorkerObj createWorker(Colors color) {
        try {
            return new WorkerObj(new BoardCoords3D(-1, -1, 0), color);
        } catch (Exception e) {
            return null;
        }
    }

}
