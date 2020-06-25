package it.polimi.ingsw.mvc.view.gui;

import it.polimi.ingsw.mvc.view.gui.fxmlcontrollers.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GuiManager extends Client {

    private static GuiManager instance = null;
    private static Logger guilog;

    private static double width;

    private GenericController currentController;

    private GuiManager(boolean log) {
        super(log);
        loggerSetup(this.log);
    }

    public static GuiManager getInstance(boolean log) {
        if (instance == null)
            instance = new GuiManager(log);
        return instance;
    }

    private static void loggerSetup(Logger defLogger){
        guilog =defLogger;
    }

    public static GuiManager getInstance() {
        return getInstance(false);
    }

    public static void setLayout(Scene scene, String path) {
        setLayout(scene, path, false);
    }

    public static void setLayout(Scene scene, String path, boolean isNewScene) {
        try {
            Pane pane = loadFxmlPane(path);
            scene.setRoot(pane);
            if (isNewScene) {
                Gui.getStage().setScene(scene);
            }
        } catch (IOException e) {
            guilog.severe("Can't load " + path);
        }
        Gui.getInstance().setMouse(scene);
    }

    private static Pane loadFxmlPane(String path) throws IOException {
        try {
            return FXMLLoader.load(GuiManager.class.getResource(path));
        } catch (final IOException e) {
            String errorText = "Resource at " + path + " couldn't be loaded";
            guilog.severe(errorText);
            throw new IOException(errorText);
        }
    }

    public static String getFxmlPath(Class<?> c) {
        List<String> s = Arrays.asList(c.toString().split("\\."));
        String name = s.get(s.size() - 1);
        return "/fxmlFiles/" + name.substring(0, name.length() - 10) + ".fxml";
    }

    public Stage getStage() {
        return Gui.getStage();
    }

    public double getDefaultWidth() {
        return width;
    }

    public static void setDefaultWidth(double w) {
        width = w;
    }

    public void setCurrentController(GenericController currentController) {
        this.currentController = currentController;
    }

    public Map<String, Colors> getPlayers() {
        return players;
    }

    public String getNumberOfPlayers() {
        return (players == null) ? String.valueOf(0) : Integer.toString(players.size());
    }

    public String getUsername() {
        return username;
    }

    private void runUpdate(Class<?> c, Runnable request) {
        if (currentController.getWindowName() != c) {
            Platform.runLater(() -> setLayout(
                    (currentController.getWindowName() == BoardController.class) ? new Scene(new Pane()) : Gui.getStage().getScene(),
                    GuiManager.getFxmlPath(c),
                    (currentController.getWindowName() == BoardController.class)));
        }
        Platform.runLater(request);
    }

    @Override
    public void showQueueInfo(String text) {
        runUpdate(TitleController.class, () -> ((TitleController) currentController).showInfo(text));
    }

    @Override
    public void requestNumberOfPlayers() {
        runUpdate(TitleController.class, () -> ((TitleController) currentController).requestNumberOfPlayers());
    }

    @Override
    public void showLobby(List<Colors> availableColors) {
        runUpdate(LobbyController.class, () -> ((LobbyController) currentController).showLobby(availableColors));
    }

    @Override
    public void updateChallengerGodSelection(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).updateChallengerGodSelection(chosenGods));
    }

    @Override
    public void requestChallengerGod(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).requestChallengerGod(chosenGods));
    }

    @Override
    public void disconnectClient() {
        super.disconnectClient();
    }

    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updatePlayerGodSelection(turnOwner, choices, chosenGods));
    }

    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestPlayerGod(chosenGods, choices));
    }

    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updateStarterPlayerSelection(choices));
    }

    @Override
    public void requestStarterPlayer(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestStarterPlayer(choices));
    }

    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, boolean specialPower) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).requestTurnAction(possibleActions, session, colors, gods));
    }

    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String turnOwner) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showBoard(session, colors, gods, turnOwner));
    }

    @Override
    public void showReconnection(boolean isReconnecting) {
        Platform.runLater(() -> currentController.showReconnection(isReconnecting));
    }

    @Override
    public void showDisconnected(String info) {
        if (currentController.getClass() == BoardController.class)
            Platform.runLater(() -> ((BoardController) currentController).clear());
        if (currentController.getClass() == TitleController.class)
            runUpdate(TitleController.class, () -> ((TitleController) currentController).showDisconnected(info));
        else
            runUpdate(DisconnectionController.class, () -> ((DisconnectionController) currentController).showDisconnected(info));
    }

    @Override
    public void showWin(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showWin(playerName));
    }

    @Override
    public void showLose(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showLose(playerName));
    }

    @Override
    public void requestLogin() { /* Logic already implemented within showLobby */ }
}
