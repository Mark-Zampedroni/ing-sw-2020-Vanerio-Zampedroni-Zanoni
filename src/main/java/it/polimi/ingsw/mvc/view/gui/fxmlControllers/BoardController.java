package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.ObservableTileEvent;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
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

    private Map<Action,List<DtoPosition>> possibleActions;
    private Action currentAction;

    private BoardScene boardSubScene;

    private List<BorderPane> actionButtons;



    public void initialize() {
        super.initialize(this);

        gui.getTileEvent().addObserver(this);
        initBoard();
        actionButtons = new ArrayList<>(Arrays.asList(testButton3,testButton2,testButton));
        actionButtons.forEach(this::hideNode);
    }

    private void initBoard() {

        gameScene = gui.getBoardLoadedScene();
        boardSubScene = (BoardScene) gameScene;
        sceneContainer.setCenter(gameScene);

        gameScene.heightProperty().bind((sceneContainer.heightProperty()));
        gameScene.widthProperty().bind(sceneContainer.widthProperty());

        gameScene.setManaged(false);
        new BoardCamera(gameScene);
        //showReconnection(true); // <----------- Test che mostra layer wifi in caso di disconnessione + attesa riconnessione
    }


    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        turnOwner = true;
        boardSubScene.turnAction(possibleActions,session);
        this.possibleActions = possibleActions;
        List<Action> actions = possibleActions.keySet().stream().sorted().collect(Collectors.toList());
        showButtons(actions);
        setDefaultAction();
    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        boardSubScene.updateBoard(session);
    }

    private void setDefaultAction() {
        List<Action> c = possibleActions.keySet().stream().filter(k -> !Action.getNullPosActions().contains(k)).sorted().collect(Collectors.toList());
        if(!c.isEmpty()) {
            Action dAction = c.get(c.size() - 1);
            currentAction = dAction;
            boardSubScene.showAnimations(dAction);
        }
    }


    private void updateButton(BorderPane button, Action action) {
        ((Label) button.getChildren().get(0)).setText(action.toString());
        button.setOnMouseClicked( event -> {
            currentAction = action;
            boardSubScene.showAnimations(action);
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
        boardSubScene.clear();
    }

    private void showButtons(List<Action> p) {
        p.forEach(action -> updateButton(actionButtons.get(p.indexOf(action)),action));
    }

    private void hideButtons() {
        actionButtons.forEach(this::hideNode);
    }


    public void showLose(String playerName) {
        /* aggiornamento board */
        boardSubScene.notifyLose(playerName);
        /* manca far sparire il nome dalla sidebar */
    }

    public void showWin(String playerName) {
        // Mostra robo vittoria playerName (distinguiamo finestra con trombette rotte se perde e belle se vince (?))
    }

    public void clear() {
        boardSubScene.clear();
    }


}
