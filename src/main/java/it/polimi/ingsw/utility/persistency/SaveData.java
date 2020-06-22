package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.GameState;

import java.io.*;
import java.util.logging.Logger;

/**
 * Contains all the information of a previous moment of the game, it's the class where is contained
 *  all the information used during the reload of a game
 */
public class SaveData implements Serializable {

    private static final long serialVersionUID = -8284194909335487737L;

    private boolean actionDone;

    private Session session;
    private StateController state;
    private Message message;

    private GameState gameState;

    private int gameCapacity;

    private String turnOwner;

    /**
     * Constructor of the class
     *
     * @param flag indicates if the save is before or after the modify of the model for the arrive of the message
     * @param lastMessage the last message received
     * @param sessionController the controller of the session
     * @param stateController the controller of the state of the game
     */
    SaveData(SessionController sessionController, StateController stateController, Message lastMessage, Boolean flag) {
        actionDone = flag;
        session = sessionController.getSession();
        state = stateController;
        saveSessionController(sessionController);
        message = lastMessage;
    }

    /**
     * Saves all the information of the game
     *
     * @param flag indicates if the save is before or after the modify of the model for the arrive of the message
     * @param lastMessage the last message received
     * @param sessionController the controller of the session
     * @param stateController the controller of the state of the game
     * @param log logger of the server
     */
    public static void saveGame(SessionController sessionController, StateController stateController, Message lastMessage, Boolean flag, Logger log) {
        if (ServerApp.isFeature()) {
            SaveData saveData = new SaveData(sessionController, stateController, lastMessage, flag);
            try (FileOutputStream game = new FileOutputStream(new File("santorini.game.ser"))) {
                ObjectOutputStream outputStream = new ObjectOutputStream(game);
                outputStream.writeObject(saveData);
                outputStream.close();
                outputStream.flush();
            } catch (IOException e) {
                log.severe(() -> "[SAVE_DATA] The game couldn't be saved because " + e.getMessage());
            }
        }
    }

    /**
     * Getter for the state of the game
     *
     * @return the state of the saved game
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Getter for the session of the game
     *
     * @return the ssession of the saved game
     */
    public Session getSession() {
        return session;
    }

    /**
     * Getter for the state controller
     *
     * @return the state controller of the saved game
     */
    public StateController getStateController() {
        return state;
    }

    /**
     * Getter for the capacity of the saved game
     *
     * @return the capacity of the the daved game
     */
    public int getGameCapacity() {
        return gameCapacity;
    }

    /**
     * Getter for the last turn owner
     *
     * @return the name of the last turn owner
     */
    public String getTurnOwner() {
        return turnOwner;
    }

    /**
     * Getter for the last message
     *
     * @return the last message received
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Returns if the execution of the action in last message is terminated before saving or not
     *
     * @return {@code true} if the execution of the action in last message is terminated before saving
     */
    public boolean getActionDone() {
        return actionDone;
    }

    /**
     * Saves all the important data from session controller (not serializable class)
     *
     * @param sessionController the controller of the session
     */
    void saveSessionController(SessionController sessionController) {
        gameState = sessionController.getState();
        gameCapacity = sessionController.getGameCapacity();
        turnOwner = sessionController.getTurnOwner();
    }

}
