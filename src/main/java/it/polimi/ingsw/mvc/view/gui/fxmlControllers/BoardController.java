package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.observer.Observer;
import javafx.fxml.FXML;
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
    public BorderPane testButton1;
    @FXML
    public BorderPane testButton2;
    @FXML
    public Label endgameLabel;
    @FXML
    public BorderPane endgameScreen;
    @FXML
    public BorderPane playerSlot1;
    @FXML
    public BorderPane playerSlot2;
    @FXML
    public BorderPane playerSlot3;
    @FXML
    public BorderPane godSlot1;
    @FXML
    public BorderPane godSlot2;
    @FXML
    public BorderPane godSlot3;
    @FXML
    public BorderPane currentPlayer1;
    @FXML
    public BorderPane currentPlayer2;
    @FXML
    public BorderPane currentPlayer3;
    @FXML
    public BorderPane info3;


    private boolean turnOwner = false;

    private Map<Action,List<DtoPosition>> possibleActions;
    private Action currentAction;

    private final Object sidePaneLock = new Object();

    private BoardScene boardSubScene;

    private List<BorderPane> actionButtons;

    private List<BorderPane> playerGod, playerTurn, playerSlot;

    private String username;


    public void initialize() throws Exception {
        super.initialize(this);
        if(BoardScene.getTileEvent() == null) {
            BoardScene.startBoardLoad(gui.getPlayers(), gui.LOG);
        }
        BoardScene.getTileEvent().addObserver(this);
        initLists();
        initBoard();
        initFeatures();
        username = gui.getUsername();
    }


    private void initLists() {
        playerSlot = new ArrayList<>(Arrays.asList(playerSlot1, playerSlot2, playerSlot3));
        playerTurn = new ArrayList<>(Arrays.asList(currentPlayer1, currentPlayer2, currentPlayer3));
        playerGod = new ArrayList<>(Arrays.asList(godSlot1, godSlot2, godSlot3));
        playerSlot.forEach(b -> {
            initFont(b);
            initFont(playerTurn.get(playerSlot.indexOf(b)));
        });
    }


    private void initFeatures(){
        hideNode(endgameScreen);
        setFontRatio(endgameLabel);
        actionButtons = new ArrayList<>(Arrays.asList(testButton,testButton1,testButton2));
        actionButtons.forEach(this::initFont);
        actionButtons.forEach(this::hideNode);
    }

    private void initFont(BorderPane borderPane){
        setFontRatio((Label)borderPane.getChildren().get(0));
    }

    private void initBoard() {

        gameScene = BoardScene.getBoardLoadedScene();
        boardSubScene = (BoardScene) gameScene;
        sceneContainer.setCenter(gameScene);

        gameScene.heightProperty().bind((sceneContainer.heightProperty()));
        gameScene.widthProperty().bind(sceneContainer.widthProperty());

        gameScene.setManaged(false);
        new BoardCamera(gameScene);
    }


    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        turnOwner = true;
        boardSubScene.turnAction(possibleActions,session);
        this.possibleActions = possibleActions;
        List<Action> actions = possibleActions.keySet().stream().sorted().collect(Collectors.toList());
        showButtons(actions);
        setDefaultAction();
        updatePlayerSlots(colors, gods, username);
    }

    private void setCurrentPlayer(String player) {
        playerSlot.forEach(p -> hideNode(playerTurn.get(playerSlot.indexOf(p))));
        playerSlot.stream()
                .filter(p -> ((Label) p.getChildren().get(0)).getText().equals(player))
                .forEach(p -> showNode(playerTurn.get(playerSlot.indexOf(p))));
    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String currentPlayer) {
        boardSubScene.updateBoard(session);
        updatePlayerSlots(colors, gods, currentPlayer);
    }

    private void updatePlayerSlots(Map<String, Colors> colors, Map<String, String> gods, String currentPlayer) {
        synchronized (sidePaneLock) {
            List<String> playerNames = new ArrayList<>(colors.keySet());

            for (int i = 0; i < 3; i++) {
                if (i >= playerNames.size()) {
                    ((Label)playerSlot.get(i).getChildren().get(0)).setText("");
                    hideNode(playerSlot.get(i));
                    hideNode(playerTurn.get(i));
                    hideNode(playerGod.get(i));
                    if (i == 2) {
                        hideNode(info3);
                    }
                } else {
                    Label name = (Label) playerSlot.get(i).getChildren().get(0);
                    name.setText(playerNames.get(i));
                    playerSlot.get(i).setId(colors.get(playerNames.get(i)).toString().toLowerCase() + "big");
                    playerGod.get(i).setId(gods.get(playerNames.get(i)));
                }
            }
            setCurrentPlayer(currentPlayer);
        }
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
        showNode(endgameScreen);
        endgameScreen.setId((playerName.equals(username)) ? "winner" : "loser");
        endgameLabel.setText((playerName.equals(username)) ? "You won!" : "You lost ...\n"+playerName+" is the winner");
    }

    public void clear() {
        boardSubScene.clear();
    }


}
