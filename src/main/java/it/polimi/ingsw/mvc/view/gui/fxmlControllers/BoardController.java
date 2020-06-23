package it.polimi.ingsw.mvc.view.gui.fxmlControllers;

import it.polimi.ingsw.mvc.view.gui.GuiManager;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardCamera;
import it.polimi.ingsw.mvc.view.gui.objects3D.utils.BoardScene;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.observer.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardController extends GenericController implements Observer<DtoPosition> {

    private final Object sidePaneLock = new Object();
    @FXML
    public SubScene gameScene;
    @FXML
    public BorderPane sceneContainer;
    @FXML
    public BorderPane actionButton;
    @FXML
    public BorderPane actionButton1;
    @FXML
    public BorderPane actionButton2;
    @FXML
    public Label endgameLabel;
    @FXML
    public BorderPane endgameScreen;
    @FXML
    public BorderPane endgameButton;
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
    @FXML
    public BorderPane godCard;
    @FXML
    public Label godNameLabel;
    @FXML
    public Label conditionLabel;
    @FXML
    public Label descriptionLabel;
    @FXML
    public BorderPane padding1;
    @FXML
    public BorderPane padding2;
    @FXML
    public BorderPane padding3;
    @FXML
    public BorderPane buttonPadding1;
    @FXML
    public BorderPane buttonPadding2;
    @FXML
    public BorderPane buttonPadding3;

    private boolean turnOwner = false;
    private Map<Action, List<DtoPosition>> possibleActions;
    private Action currentAction;
    private BoardScene boardSubScene;

    private List<BorderPane> actionButtons;

    private List<BorderPane> playerGod, playerTurn, playerSlot;

    private String username;


    public void initialize() throws Exception {
        super.initialize(this);
        if (BoardScene.getTileEvent() == null) {
            BoardScene.startBoardLoad(gui.getPlayers(), gui.log);
        }
        BoardScene.getTileEvent().addObserver(this);
        godCard.setVisible(false);
        initLists();
        initBoard();
        initFeatures();
        initLabels();
        username = gui.getUsername();

        playerGod.forEach(g -> {
            g.setOnMouseEntered(event -> showCard(g));
            g.setOnMouseExited(event -> godCard.setVisible(false));
        });
    }

    private void showCard(BorderPane god){
        String[] temp = Gods.valueOf(god.getId()).getDescription().split(":");
        if (temp.length > 1) {
            godCard.setVisible(true);
            godNameLabel.setText(god.getId());
            conditionLabel.setText("Effect\n(" + temp[0] + ")");
            descriptionLabel.setText(temp[1]);
        }
    }

    private void initLists() {
        playerSlot = new ArrayList<>(Arrays.asList(playerSlot1, playerSlot2, playerSlot3));
        playerTurn = new ArrayList<>(Arrays.asList(currentPlayer1, currentPlayer2, currentPlayer3));
        playerGod = new ArrayList<>(Arrays.asList(godSlot1, godSlot2, godSlot3));
    }

    private void initFeatures() {
        initEndGame();
        actionButtons = new ArrayList<>(Arrays.asList(actionButton, actionButton1, actionButton2));
        actionButtons.forEach(this::initFont);
        actionButtons.forEach(this::hideNode);
    }

    private void initLabels() {
        playerSlot.forEach(p -> setFontRatio((Label) p.getChildren().get(0)));
        playerSlot.forEach(this::setPaddingRatio);
        playerTurn.forEach(this::setPaddingRatio);
        playerGod.forEach(this::setPaddingRatio);
        setFontRatio(descriptionLabel);
        setFontRatio(godNameLabel);
        setFontRatio(conditionLabel);
        setPaddingRatio(padding1);
        setPaddingRatio(padding2);
        setPaddingRatio(padding3);
        setPaddingRatio(buttonPadding1);
        setPaddingRatio(buttonPadding2);
        setPaddingRatio(buttonPadding3);
    }

    private void toggleButton(BorderPane button) {
        actionButtons.stream().filter(b -> b == button).findFirst().ifPresent(b -> b.setId(b.getId() + ((b.getId().contains("_pressed") ? "" : "_pressed"))));
        actionButtons.stream().filter(b -> b != button).forEach(b -> b.setId(b.getId().replace("_pressed", "")));
    }

    private void toggleButton(Action action) {
        actionButtons.stream().filter(b -> ((Label) b.getChildren().get(0)).getText().equals(action.toString().replace("_", " "))).findFirst().ifPresent(this::toggleButton);
    }

    private void removeToggle() {
        actionButtons.forEach(b -> b.setId(b.getId().replace("_pressed", "")));
    }

    private void initFont(BorderPane borderPane) {
        setFontRatio((Label) borderPane.getChildren().get(0));
    }

    private void initEndGame() {
        hideNode(endgameScreen);
        setFontRatio(endgameLabel);
        initFont(endgameButton);
        endgameButton.setOnMousePressed(event -> endgameButton.setId("buttonPressed"));
        endgameButton.setOnMouseReleased(event -> {
            endgameButton.setId("buttonConfirm");
            Platform.runLater(() -> GuiManager.setLayout(this.getScene(), GuiManager.getFxmlPath(TitleController.class)));
        });
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
        boardSubScene.turnAction(possibleActions, session);
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
                    hidePlayerTag(i);
                    if (i == 2) hideNode(info3);
                } else showPlayerTag(i, playerNames, colors, gods);
            }
            setCurrentPlayer(currentPlayer);
        }
    }

    private void showPlayerTag(int i, List<String> playerNames, Map<String, Colors> colors, Map<String, String> gods) {
        Label name = (Label) playerSlot.get(i).getChildren().get(0);
        name.setText(playerNames.get(i));
        playerSlot.get(i).getChildren().get(0).setId(colors.get(playerNames.get(i)).toString().toLowerCase() + "big");
        playerGod.get(i).setId(gods.get(playerNames.get(i)));
    }

    private void hidePlayerTag(int i) {
        ((Label) playerSlot.get(i).getChildren().get(0)).setText("");
        hideNode(playerSlot.get(i));
        hideNode(playerTurn.get(i));
        hideNode(playerGod.get(i));
    }


    private void setDefaultAction() {
        List<Action> c = possibleActions.keySet().stream().filter(k -> !Action.getNullPosActions().contains(k)).sorted().collect(Collectors.toList());
        if (!c.isEmpty()) {
            Action dAction = c.get(c.size() - 1);
            currentAction = dAction;
            toggleButton(dAction);
            boardSubScene.showAnimations(dAction);
        }

    }

    private void updateButton(BorderPane button, Action action) {

        if (action != Action.SPECIAL_POWER) setPositionButton(button, action);
        else setPowerButton(button, action);
        button.setOnMouseClicked(event -> {
            currentAction = action;
            if (action == Action.SPECIAL_POWER)
                button.setId(button.getId().contains("_on") ? button.getId().replace("_on", "") : button.getId() + "_on");
            else toggleButton(button);
            boardSubScene.showAnimations(action);
            if (possibleActions.get(action).isEmpty()) {
                update(null);
            }
        });
        showNode(button);
    }

    private void setPositionButton(BorderPane button, Action action) {
        button.setId("button" + action.toString().toUpperCase());
        ((Label) button.getChildren().get(0)).setText(action.toString().replace("_", " "));
        button.setOnMousePressed(event -> button.setId("button" + action.toString().toUpperCase() + "_pressed"));
        button.setOnMouseReleased(event -> button.setId(button.getId().replace("_pressed", "")));
    }

    private void setPowerButton(BorderPane button, Action action) {
        ((Label) button.getChildren().get(0)).setText("");
        if (!button.getId().contains(action.toString().toUpperCase()))
            button.setId("button" + action.toString().toUpperCase());
        button.setOnMousePressed(null);
        button.setOnMouseReleased(null);
    }

    @Override
    public void update(DtoPosition position) {
        if (turnOwner && currentAction != null && gui.validateAction(currentAction, position, possibleActions)) {
            removeToggle();
            clear();
        }
    }

    public void clear() {
        turnOwner = false;
        currentAction = null;
        hideButtons();
        boardSubScene.clear();
    }

    private void showButtons(List<Action> p) {
        p.forEach(action -> updateButton(actionButtons.get(p.indexOf(action)), action));
    }

    private void hideButtons() {
        actionButtons.forEach(this::hideNode);
    }


    public void showLose(String playerName) {
        boardSubScene.notifyLose(playerName);
    }

    public void showWin(String playerName) {
        showNode(endgameScreen);
        endgameScreen.setId((playerName.equals(username)) ? "winner" : "loser");
        endgameLabel.setText((playerName.equals(username)) ? "You won!" : "You lost\n" + playerName + " is the winner");
    }

}
