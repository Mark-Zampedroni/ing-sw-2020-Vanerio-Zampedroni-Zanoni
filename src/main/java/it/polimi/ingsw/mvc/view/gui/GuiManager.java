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
 * Handles all the interactions between Client and GUI
 */
public class GuiManager extends Client {

    private static GuiManager instance = null;
    private static Logger guilog;

    private static double width;

    private GenericController currentController;

    /**
     * Constructor for GuiManager
     */
    private GuiManager(boolean log) {
        super(log);
        loggerSetup(this.log);
    }


    /**
     * Creates a unique instance of GuiManager, it also could use a logger to store events
     *
     * @param log if {@code true} creates a logger
     * @return the {@link GuiManager GuiManager} instance
     */
    public static GuiManager getInstance(boolean log) {
        if (instance == null)
            instance = new GuiManager(log);
        return instance;
    }

    /**
     * Sets a given logger as default for Gui Manager
     *
     * @param defLogger logger
     */
    private static void loggerSetup(Logger defLogger){
        guilog =defLogger;
    }

    /**
     * Creates a unique instance of GuiManager
     *
     * @return the {@link GuiManager GuiManager} instance
     */
    public static GuiManager getInstance() {
        return getInstance(false);
    }


    /**
     * Sets a layout for an already existing scene
     *
     * @param scene the scene
     * @param path the path of the Fxml file
     */
    public static void setLayout(Scene scene, String path) {
        setLayout(scene, path, false);
    }


    /**
     * Sets a layout for a scene
     *
     * @param scene the scene
     * @param isNewScene defines if the scene is a new one
     * @param path the path of the Fxml file
     * @return the {@link Gui gui} instance
     */
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


    /**
     * Loads a pane for the Fxml file
     *
     * @param path the path of the Fxml file
     * @return the pane
     */
    private static Pane loadFxmlPane(String path) throws IOException {
        try {
            return FXMLLoader.load(GuiManager.class.getResource(path));
        } catch (final IOException e) {
            String errorText = "Resource at " + path + " couldn't be loaded";
            guilog.severe(errorText);
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
     * @return a map of players-colors
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
     * @param c a generic class
     */
    private void runUpdate(Class<?> c, Runnable request) {
        if (currentController.getWindowName() != c) {
            Platform.runLater(() -> setLayout(
                    (currentController.getWindowName() == BoardController.class) ? new Scene(new Pane()) : Gui.getStage().getScene(),
                    GuiManager.getFxmlPath(c),
                    (currentController.getWindowName() == BoardController.class)));
        }
        Platform.runLater(request);
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param text text to display
     */
    @Override
    public void showQueueInfo(String text) {
        runUpdate(TitleController.class, () -> ((TitleController) currentController).showInfo(text));
    }

    /**
     * Method used for the interaction between client and gui
     */
    @Override
    public void requestNumberOfPlayers() {
        runUpdate(TitleController.class, ((TitleController) currentController)::requestNumberOfPlayers);
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param availableColors list of colors
     */
    @Override
    public void showLobby(List<Colors> availableColors) {
        runUpdate(LobbyController.class, () -> ((LobbyController) currentController).showLobby(availableColors));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param chosenGods list of chosen gods
     */
    @Override
    public void updateChallengerGodSelection(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).updateChallengerGodSelection(chosenGods));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param chosenGods list of chosen gods
     */
    @Override
    public void requestChallengerGod(List<String> chosenGods) {
        runUpdate(ChallengerSelectionController.class, () -> ((ChallengerSelectionController) currentController).requestChallengerGod(chosenGods));
    }

    /**
     * Method used for the interaction between client and gui
     */
    @Override
    public void disconnectClient() {
        super.disconnectClient();
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param chosenGods list of chosen gods
     * @param turnOwner current player
     * @param choices map containing players' name and their gods
     */
    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updatePlayerGodSelection(turnOwner, choices, chosenGods));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param chosenGods list of chosen gods
     * @param choices map containing players' name and their gods
     */
    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestPlayerGod(chosenGods, choices));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param choices map containing players' name and their gods
     */
    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).updateStarterPlayerSelection(choices));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param choices map containing players' name and their gods
     */
    @Override
    public void requestStarterPlayer(Map<String, String> choices) {
        runUpdate(GodSelectionController.class, () -> ((GodSelectionController) currentController).requestStarterPlayer(choices));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param possibleActions possible action that a player can execute
     * @param gods  map containing players' name and their gods
     * @param colors map containing player's name and their colors
     * @param session game session
     * @param specialPower defines a god's special power
     */
    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, boolean specialPower) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).requestTurnAction(possibleActions, session, colors, gods));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param gods  map containing players' name and their gods
     * @param colors map containing player's name and their colors
     * @param session game session
     */
    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String turnOwner) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showBoard(session, colors, gods, turnOwner));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param isReconnecting defines if a player is reconnecting
     */
    @Override
    public void showReconnection(boolean isReconnecting) {
        Platform.runLater(() -> currentController.showReconnection(isReconnecting));
    }

    /**
     * Method used for the interaction between client and gui
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
     * Method used for the interaction between client and gui
     *
     * @param playerName player's name
     */
    @Override
    public void showWin(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showWin(playerName));
    }

    /**
     * Method used for the interaction between client and gui
     *
     * @param playerName player's name
     */
    @Override
    public void showLose(String playerName) {
        runUpdate(BoardController.class, () -> ((BoardController) currentController).showLose(playerName));
    }

    /**
     * Method used for the interaction between client and gui
     */
    @Override
    public void requestLogin() { /* Logic already implemented within showLobby */ }
}
