package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.obj.WorkerObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCoords3D;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.layout.BorderPane;

import java.util.*;
import java.util.stream.Collectors;

public class BoardController extends GenericController {

    @FXML
    //public SubScene sidebarSub;
    public SubScene gameScene;
    public BorderPane sceneContainer;
    public BorderPane main;
    //public GridPane sidebarGrid;

    Group objects = new Group();

    DtoSession localSession;


    public void initialize() throws Exception {
        super.initialize(this);
        initBoard();
    }

    private void initBoard() throws Exception {
        WorkerObj worker;
        BoardObj board = new BoardObj();
        objects.getChildren().addAll(
                board,
                worker = new WorkerObj(new BoardCoords3D(4,0,0)) // <---------- TEST
        );

        gameScene = new BoardScene(objects, board,840,700);
        sceneContainer.setCenter(gameScene);

        gameScene.heightProperty().bind((sceneContainer.heightProperty()));
        gameScene.widthProperty().bind(sceneContainer.widthProperty());

        gameScene.setManaged(false);
        new BoardCamera(gameScene);
        initWorkerTest(board,worker);
        //showReconnection(true); // <----------- Test che mostra layer wifi in caso di disconnessione + attesa riconnessione
    }

    private void initWorkerTest(BoardObj board, WorkerObj worker) {
        //scene.setCursor(Cursor.OPEN_HAND); // si può cambiare il cursore
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
        localSession = session;

        List<Action> actions = possibleActions.keySet().stream().sorted().collect(Collectors.toList());
        actions.forEach(this::addButton);

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
        /*
        1) Arriva aggiornamento -> confronto board e worker -> applicare metodi board3D per visualizzare roba. Si salva da parte la nuova DtoSession al posto di quella vecchia

        2) Se è il turno del player riempie un hashmap nel controller <String,List<Animazioni> > generando i nodi per tutte le animazioni e settandoli invisibili

        3) Quando viene pigiato un tasto durante il turno del player setta la visibility a true al gruppo di nodi collegati all'azione chiamata con l'id del tasto ( map.get(Azione) )

        4) Quando viene cliccata l'animazione visualizzata dal tasto o la tile sotto all'animazione viene eseguita la lambda function che manda il messaggio al server specificando la posizione —---- come ? Probabilmente conviene passare alle tile e animazioni un puntatore ad una lambda function (o runnable) che poi viene solo sotituito nel controller quando si cambia il tasto cliccato —--

        5) A fine turno del giocatore corrente si puliscono (solo nel suo client) tutte le variabili relative ai nodi delle animazioni
         */
    }

    /*
    private void populateBoard(BoardObj board, WorkerObj worker){
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
    }*/

}
