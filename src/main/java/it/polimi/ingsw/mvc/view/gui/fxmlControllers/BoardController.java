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


    private int numberOfPlayers;
    private boolean firstTurn = true;

    private boolean turnOwner = false;

    private Map<Action,List<DtoPosition>> possibleActions;
    private Action currentAction;

    private BoardScene boardSubScene;

    private List<BorderPane> actionButtons;

    private Map<BorderPane,BorderPane> playerGod = new HashMap<>();
    private Map<BorderPane,BorderPane> playerTurn = new HashMap<>();


    public void initialize() throws Exception {
        super.initialize(this);
        if(BoardScene.getTileEvent() == null) {
            BoardScene.startBoardLoad(gui.getPlayers(), gui.LOG);
        }
        BoardScene.getTileEvent().addObserver(this);
        initBoard();
        initFeatures();
    }



    private void initFeatures(){
        hideNode(endgameScreen);
        setFontRatio(endgameLabel);
        actionButtons = new ArrayList<>(Arrays.asList(testButton,testButton1,testButton2));
        actionButtons.forEach(this::initFont);
        actionButtons.forEach(this::hideNode);
        numberOfPlayers = Integer.parseInt(gui.getNumberOfPlayers());
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
        //showReconnection(true); // <----------- Test che mostra layer wifi in caso di disconnessione + attesa riconnessione
    }


    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        turnOwner = true;
        boardSubScene.turnAction(possibleActions,session);
        this.possibleActions = possibleActions;
        List<Action> actions = possibleActions.keySet().stream().sorted().collect(Collectors.toList());
        showButtons(actions);
        setDefaultAction();
        if(firstTurn || numberOfPlayers != Integer.parseInt(gui.getNumberOfPlayers())) {
            updatePlayerSlots(colors, gods);
            firstTurn = false;
        }
        setCurrentPlayer(gui.getUsername());
    }

    private void setCurrentPlayer(String player){
        for(BorderPane main : playerTurn.keySet()){
            if(((Label) main.getChildren().get(0)).getText().equals(player)){
                showNode(playerTurn.get(main));
            }
            else{
                hideNode(playerTurn.get(main));
            }
        }
    }

    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        boardSubScene.updateBoard(session);
        if(firstTurn || numberOfPlayers != Integer.parseInt(gui.getNumberOfPlayers())) {
            updatePlayerSlots(colors, gods);
            firstTurn = false;
            //setCurrentPlayer(); Da aggiungere poi
        }
    }

    private void updatePlayerSlots(Map<String, Colors> colors, Map<String, String> gods){
        Map<String, Colors> temp1 = new HashMap<>(colors);
        Map<String, String> temp2 = new HashMap<>(gods);

        playerGod = Map.of(playerSlot1,godSlot1,playerSlot2,godSlot2);
        playerTurn = Map.of(playerSlot1,currentPlayer1,playerSlot2,currentPlayer2);

        if(Integer.parseInt(gui.getNumberOfPlayers()) == 3) {
            playerGod.put(playerSlot3,godSlot3);
            playerTurn.put(playerSlot3,currentPlayer3);
        }
        else{
            hideNode(playerSlot3);
            hideNode(godSlot3);
            hideNode(currentPlayer3);
            hideNode(info3);
        }

        playerTurn.keySet().forEach(b-> {
            initFont(b);
            initFont(playerTurn.get(b));
        });

        for(BorderPane main : playerGod.keySet()){
            Label temp =  ((Label) main.getChildren().get(0));
            temp.setText(temp1.keySet().stream().findFirst().get());
            main.setId((temp1.get(temp.getText()).toString()).toLowerCase() + "big");
            playerGod.get(main).setId(temp2.get(temp.getText()));
            temp1.remove(temp.getText());
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
        showNode(endgameScreen);
        endgameScreen.setId("loser");
        endgameLabel.setText("Players who suck: " + playerName);
        /* manca far sparire il nome dalla sidebar */
    }

    public void showWin(String playerName) {
        showNode(endgameScreen);
        endgameScreen.setId("winner");
        endgameLabel.setText("Biggest chad evah: " + playerName);
    }

    public void clear() {
        boardSubScene.clear();
    }


}
