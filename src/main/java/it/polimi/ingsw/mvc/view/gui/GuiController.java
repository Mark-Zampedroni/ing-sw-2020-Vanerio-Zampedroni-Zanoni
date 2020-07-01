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

/**
 * Client with graphical interface (GUI)
 */
public class GuiController extends Client {

    private static GuiController instance = null;
    private static Logger guiLog;

    private static double width;

    private GenericController currentController;

    /**
     * Constructor
     */
    private GuiController(boolean log) {
        super(log);
        setGuiLog(this.log);
    }


    /**
     * Gets the Singleton instance of GuiController
     *
     * @param log if {@code true} creates a logger
     * @return the {@link GuiController GuiController} instance
     */
    public static GuiController getInstance(boolean log) {
        if (instance == null)
            instance = new GuiController(log);
        return instance;
    }

    /**
     * Sets a logger for Gui Manager
     *
     * @param defLogger log where any event will be recorded
     */
    private static void setGuiLog(Logger defLogger) {
        guiLog = defLogger;
    }

    /**
     * Gets the Singleton instance of GuiController.
     * No logger is attached if the instance is created
     *
     * @return the {@link GuiController GuiController} instance
     */
    public static GuiController getInstance() {
        return getInstance(false);
    }


    /**
     * Loads a layout from an fxml file and applies it to a Scene
     *
     * @param scene the scene where the layout will be applied
     * @param path  the path of the Fxml file
     */
    public static void setLayout(Scene scene, String path) {
        setLayout(scene, path, false);
    }


    /**
     * Loads a layout from a Fxml file and applies it to a Scene.
     * If the scene is new sets it to the stage
     *
     * @param scene the scene
     * @param isNewScene defines if the scene is a new one
     * @param path the path of the Fxml file
     */
    public static void setLayout(Scene scene, String path, boolean isNewScene) {
        try {
            Pane pane = loadFxmlPane(path);
            scene.setRoot(pane);
            if (isNewScene) Gui.getStage().setScene(scene);
        } catch (IOException e) {
            guiLog.severe("Can't load " + path);
        }
        Gui.getInstance().setMouse(scene);
    }


    /**
     * Loads a pane from a Fxml file
     *
     * @param path the path of the Fxml file
     * @return the pane
     */
    private static Pane loadFxmlPane(String path) throws IOException {
        try {
            return FXMLLoader.load(GuiController.class.getResource(path));
        } catch (final IOException e) {
            String errorText = "Resource at " + path + " couldn't be loaded";
            guiLog.severe(errorText);
            throw new IOException(errorText);
        }
    }

    /**
     * Getter for the path of a Fxml file
     *
     * @param c a generic class
     * @return the path
     */
    public static String getFxmlPath(Class<?> c) {
        List<String> s = Arrays.asList(c.toString().split("\\."));
        String name = s.get(s.size() - 1);
        return "/fxmlFiles/" + name.substring(0, name.length() - 10) + ".fxml";
    }

    /**
     * Getter for the stage
     *
     * @return the stage
     */
    public Stage getStage() {
        return Gui.getStage();
    }


    /**
     * Getter for the default width of the window
     *
     * @return a numerical value used as default width
     */
    public double getDefaultWidth() {
        return width;
    }


    /**
     * Setter for the default width of the window
     *
     * @param w used to define the default width of a window
     */
    public static void setDefaultWidth(double w) {
        width = w;
    }

    public void setCurrentController(GenericController currentController) {
        this.currentController = currentController;
    }


    /**
     * Getter for a map containing the players and their colors
     *
     * @return a map of players to colors
     */
    public Map<String, Colors> getPlayers() {
        return players;
    }


    /**
     * Getter for the number of players
     *
     * @return the number of players as a string
     */
    public String getNumberOfPlayers() {
        return (players == null) ? String.valueOf(0) : Integer.toString(players.size());
    }


    /**
     * Getter for the player's username
     *
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }


    /**
     * Called when the Client runs a request of update using a method implemented from View.
     * If the current fxml controller supports the request then applies it, otherwise
     * the default fxml for the request is loaded and applied to the Scene
     *
     * @param c class of the fxml controller needed to run the request
     * @param request requested update by View
     */
    private void runUpdate(Class<?> c, Runnable request) {
        if (currentController.getWindowClass() != c) {
            Platform.runLater(() -> setLayout(
                    (currentController.getWindowClass() == BoardController.class) ? new Scene(new Pane()) : Gui.getStage().getScene(),
                    GuiController.getFxmlPath(c),
                    (currentController.getWindowClass() == BoardController.class)));
        }
        Platform.runLater(request);
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param text text to display
     */
    @Override
    public void showQueueInfo(String text) {
        runUpdate(TitleController.class, () -> ((TitleController) currentController).showInfo(text));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     */
    @Override
    public void requestNumberOfPlayers() {
        runUpdate(TitleController.class, ((TitleController) currentController)::requestNumberOfPlayers);
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param availableColors list of available colors
     */
    @Override
    public void showLobby(List<Colors> availableColors) {
        runUpdate(LobbyController.class, () -> ((LobbyController) currentController).showLobby(availableColors));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param chosenGods list of chosen gods
     */
    @Override
    public void updateChallengerGodSelection(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).updateChallengerGodSelection(chosenGods));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param chosenGods list of chosen gods
     */
    @Override
    public void requestChallengerGod(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).requestChallengerGod(chosenGods));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     */
    @Override
    public void disconnectClient() {
        super.disconnectClient();
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param chosenGods list of chosen gods
     * @param turnOwner current player
     * @param choices map connecting a player's name to its god
     */
    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updatePlayerGodSelection(turnOwner, choices, chosenGods));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param chosenGods list of chosen gods
     * @param choices map connecting a player's name to its god
     */
    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestPlayerGod(chosenGods, choices));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param choices map connecting a player's name to its god
     */
    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updateStarterPlayerSelection(choices));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param choices map connecting a player's name to its god
     */
    @Override
    public void requestStarterPlayer(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestStarterPlayer(choices));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param possibleActions      possible action that a player can execute
     * @param gods                 map connecting a player's name to its god
     * @param colors               map connecting a player's name to its color
     * @param session              game session
     * @param isSpecialPowerActive {@code true} if the god's passive special power is active
     */
    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, boolean isSpecialPowerActive) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).requestTurnAction(possibleActions, session, colors, gods));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param gods  map connecting a player's name to its god
     * @param colors map connecting a player's name to its color
     * @param session game session
     */
    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String turnOwner) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showBoard(session, colors, gods, turnOwner));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param isReconnecting {@code ture} if a player is reconnecting
     */
    @Override
    public void showReconnection(boolean isReconnecting) {
        Platform.runLater(() -> currentController.showReconnection(isReconnecting));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param info disconnection message
     */
    @Override
    public void showDisconnected(String info) {
        if (currentController.getClass() == BoardController.class)
            Platform.runLater(((BoardController) currentController)::clear);
        if (currentController.getClass() == TitleController.class)
            runUpdate(TitleController.class, () -> ((TitleController) currentController).showDisconnected(info));
        else
            runUpdate(DisconnectionController.class, () -> ((DisconnectionController) currentController).showDisconnected(info));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param playerName player's name
     */
    @Override
    public void showWin(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showWin(playerName));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     *
     * @param playerName player's name
     */
    @Override
    public void showLose(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showLose(playerName));
    }

    /**
     * Implementation of {@link it.polimi.ingsw.mvc.view.View view}
     */
    @Override
    public void requestLogin() {
        log.info("[GUI] Request of login");
    }
}
