package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.obj.BoardObj;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.ObservableTileEvent;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.dto.DtoWorker;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.observer.Observer;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.*;
import java.util.stream.Collectors;

public class BoardController extends GenericController implements Observer<DtoPosition> {

    @FXML
    public SubScene gameScene;
    @FXML
    public BorderPane sceneContainer;
    @FXML
    public BorderPane main;
    @FXML
    public BorderPane testButton;
    @FXML
    public BorderPane testButton2;
    @FXML
    public BorderPane testButton3;


    private boolean turnOwner = false;

    private DtoSession localSession;

    private ObservableTileEvent tileEvent;

    private Group objects = new Group();
    private BoardObj board;
    private Map<Action,Group> animations;

    private final Object boardLock = new Object();

    private Map<Action,List<DtoPosition>> possibleActions;
    private Action currentAction;

    private List<BorderPane> actionButtons;


    public void initialize() throws Exception {
        super.initialize(this);
        tileEvent = new ObservableTileEvent();
        tileEvent.addObserver(this);
        initBoard();
        actionButtons = new ArrayList<>(Arrays.asList(testButton3,testButton2,testButton));
        actionButtons.forEach(this::hideNode);
    }

    private void initBoard() throws Exception {
        board = new BoardObj(tileEvent);
        objects.getChildren().addAll(board);

        gameScene = new BoardScene(objects, board,840,700);
        sceneContainer.setCenter(gameScene);

        gameScene.heightProperty().bind((sceneContainer.heightProperty()));
        gameScene.widthProperty().bind(sceneContainer.widthProperty());

        gameScene.setManaged(false);
        new BoardCamera(gameScene);
        //showReconnection(true); // <----------- Test che mostra layer wifi in caso di disconnessione + attesa riconnessione
    }

    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        showBoard(session,colors,gods);
        turnOwner = true;
        addAnimations(possibleActions);

        this.possibleActions = possibleActions;

        localSession = session;

        List<Action> actions = possibleActions.keySet().stream().sorted().collect(Collectors.toList());
        showButtons(actions);

    }

    private void addAnimations(Map<Action, List<DtoPosition>> possibleActions) {
        animations = new HashMap<>();
        for(Action action : possibleActions.keySet()) {
            Group temp = new Group();
            temp.setVisible(false);
            possibleActions.get(action).forEach(p -> temp.getChildren().add(board.getTile(p.getX(),p.getY()).addEffect(action)));
            board.getChildren().add(temp);
            animations.put(action,temp);
        }
    }

    private void clearAnimations() {
        if(animations != null) {
            animations.values().forEach(group -> board.getChildren().remove(group));
            animations = null;
        }
    }

    private void showAnimations(Action action) {
        animations.keySet().forEach(k -> animations.get(k).setVisible(k == action));
    }

    private void updateButton(BorderPane button, Action action) {
        ((Label) button.getChildren().get(0)).setText(action.toString());
        button.setOnMouseClicked( event -> {
            currentAction = action;
            showAnimations(action);
            if(possibleActions.get(action).isEmpty()) {
                update(null);
            }
        });
        showNode(button);
    }

    @Override
    public void update(DtoPosition position) {
        if(turnOwner && currentAction!=null && gui.validateAction(currentAction,position,possibleActions)) {
           clearTurn();
        }
    }

    private void clearTurn() {
        turnOwner = false;
        currentAction = null;
        hideButtons();
        clearAnimations();
    }

    private void showButtons(List<Action> p) {
        System.out.println("Show buttons: "+actionButtons+" for actions: "+p);
        p.forEach(action -> updateButton(actionButtons.get(p.indexOf(action)),action));
    }

    private void hideButtons() {
        actionButtons.forEach(this::hideNode);
    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        System.out.println("Showing board update ..."); // <-------------------------------------- TEST
        synchronized(boardLock) {
            updateWorkers(session.getWorkers());
        }
        localSession = session;
    }

    private void updateWorkers(List<DtoWorker> workers) {
        if(workers == null || localSession == null) { return; }
        Map<DtoPosition,String> localPositions = new HashMap<>();
        localSession.getWorkers().forEach(w -> localPositions.put(w.getPosition(),w.getMasterUsername()));

        if(localSession.getWorkers().size() < workers.size()) { // A worker is added
            System.out.println("Worker added"); // <-------------------------------------- TEST
            workers.stream().filter(w -> localPositions.keySet().stream().noneMatch(k -> k.equals(w.getPosition()))).forEach(this::addWorker);
        }
        /*
        else if(localSession.getWorkers().size() == workers.size()) { // A worker was possibly moved
            DtoPosition newPosition = workers.stream().filter(w -> w.getPosition())
            }
        }*/
    }

    private void addWorker(DtoWorker w) {
        try {
            board.getTile(w.getPosition().getX(),w.getPosition().getY()).addWorker(objects, gui.getPlayers().get(w.getMasterUsername()));
        } catch(Exception e) { gui.LOG.severe("[BOARD_CONTROLLER_FX] Worker couldn't be loaded"); }
    }

    public void clear() {
        clearAnimations();
    }

}
