package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.obj.WorkerObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardGrid;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.*;

public class BoardController extends GenericController {

    @FXML
    public SubScene sidebarSub;
    public SubScene gameSub;
    public BorderPane gamePane;
    public GridPane sidebarGrid;
    public BorderPane main;

    BoardObj board;
    BoardGrid grid;
    Group objects = new Group();
    WorkerObj worker;
    SubScene scene;
    DtoSession localSession;


    public void initialize() throws Exception {
        super.initialize(this);
        initSidebar();
        initBoard();
    }

    private void initBoard() throws Exception {
        board = new BoardObj();
        grid = new BoardGrid();
        grid.setVisible(false);
        objects.getChildren().addAll(
                board,
                grid,
                worker = new WorkerObj(new BoardCoords3D(4,0,0))
        );

        scene = new BoardScene(objects,board,840,700);
        main.setLeft(scene);
        scene.heightProperty().bind((main.heightProperty()));
        scene.widthProperty().bind(main.widthProperty().multiply(0.8077));
        scene.setManaged(false);
        new BoardCamera(scene);
        //scene.setCursor(Cursor.OPEN_HAND); // si può cambiare il cursore

        //populateBoard();
        worker.setOnMouseClicked(event -> {
            BoardCoords3D newCoords = new BoardCoords3D(0,0,0);
            board.getTile(worker.getCoords().getValueX(),worker.getCoords().getValueY()).addEffect(Action.MOVE);
            if(worker.getCoords().getValueX() == 4 && worker.getCoords().getValueY() != 4) { newCoords = new BoardCoords3D(worker.getCoords().getValueX(),worker.getCoords().getValueY()+1,0); }
            else if(worker.getCoords().getValueY() == 4 && worker.getCoords().getValueX() != 0) { newCoords = new BoardCoords3D(worker.getCoords().getValueX()-1,worker.getCoords().getValueY(),0); }
            else if(worker.getCoords().getValueX() == 0 && worker.getCoords().getValueY() != 0) { newCoords = new BoardCoords3D(worker.getCoords().getValueX(),worker.getCoords().getValueY()-1,0); }
            else if(worker.getCoords().getValueY() == 0 && worker.getCoords().getValueX() != 4) { newCoords = new BoardCoords3D(worker.getCoords().getValueX()+1,worker.getCoords().getValueY(),0); }
            board.getTile(newCoords.getValueX(),newCoords.getValueY()).grabWorker(worker);
        });
    }

    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        updateBoard(localSession, session);
        localSession=session;

        List <Action> actions = (List<Action>) possibleActions.keySet();
        Collections.sort(actions);
        actions.stream().forEach( p -> addButton(p));

        //metodo per colori e nomi
        //metodo per dei e nomi
        //metodo per dtoSession

    }

    private void updateBoard(DtoSession localSession, DtoSession session) {

    }

    private void addButton(Action p) {

    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        // update da view che richiede di aggiornare la board a quella contenuta in session
        // il 3D è pesante da generare, va modificata solo la differenza tra la board vecchia (va salvata) e quella nuova
    }

    public void initSidebar() throws IOException {

        Timer timer = new Timer ();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gamePane.setVisible(false);
            }
        }, 8000);

        sidebarSub.heightProperty().bind(main.heightProperty());
        sidebarSub.widthProperty().bind(main.widthProperty().multiply(0.1923));
    }

    private void populateBoard(){
        board.getTile(0,0).increaseHeight();
        board.getTile(1,0).increaseHeight();
        board.getTile(1,0).increaseHeight();
        board.getTile(1,0).addEffect(Action.MOVE);
        board.getTile(0,0).addEffect(Action.MOVE);
        board.getTile(2,0).addEffect(Action.BUILD);
        board.getTile(0,4).grabWorker(worker);
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).increaseHeight();
        board.getTile(0,1).addEffect(Action.SELECT_WORKER);
        board.getTile(0,2).increaseHeight();
        board.getTile(0,2).increaseHeight();
        board.getTile(0,2).increaseHeight();
        board.getTile(2,2).increaseHeight();
        board.getTile(2,2).placeDome();
    }
}
