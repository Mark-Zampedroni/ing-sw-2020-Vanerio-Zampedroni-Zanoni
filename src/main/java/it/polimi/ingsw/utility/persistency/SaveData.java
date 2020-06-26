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
 * Utility class that saves the game in a save file
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
     * Constructor of this object, that will be serialized and stored within the save file
     *
     * @param flag              indicates if the save occurs before or after a message is parsed
     * @param lastMessage       the last message received
     * @param sessionController the controller of the MVC
     * @param stateController   the state of the controller
     */
    private SaveData(SessionController sessionController, StateController stateController, Message lastMessage, Boolean flag) {
        actionDone = flag;
        session = sessionController.getSession();
        state = stateController;
        saveSessionController(sessionController);
        message = lastMessage;
    }

    /**
     * Creates the save file
     *
     * @param flag indicates if the save occurs before or after a message is parsed
     * @param lastMessage the last message received
     * @param sessionController the controller of the MVC
     * @param stateController the state of the controller
     * @param log logger where any event will be stored
     */
    public static void saveGame(SessionController sessionController, StateController stateController, Message lastMessage, Boolean flag, Logger log) {
        if (ServerApp.isFeature()) {
            SaveData saveData = new SaveData(sessionController, stateController, lastMessage, flag);
            try (FileOutputStream game = new FileOutputStream(new File("saved.game.ser"))) {
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
     * @return the session of the saved game
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
     * Getter for the capacity (as number of players) of the saved game
     *
     * @return the capacity of the saved game
     */
    public int getGameCapacity() {
        return gameCapacity;
    }

    /**
     * Getter for the last turn owner
     *
     * @return the name of the turn owner at the moment of the save
     */
    public String getTurnOwner() {
        return turnOwner;
    }

    /**
     * Getter for the last message received
     *
     * @return the last message received at the moment of the save
     */
    public Message getMessage() {
        return message;
    }

    /**
     * Returns if the parsing of the last message ended before the save or not
     *
     * @return {@code true} if the parsing of the last message ended before the save or not
     */
    public boolean getActionDone() {
        return actionDone;
    }

    /**
     * Saves all the data from session controller (not serializable class)
     *
     * @param sessionController the controller of the session
     */
    void saveSessionController(SessionController sessionController) {
        gameState = sessionController.getState();
        gameCapacity = sessionController.getGameCapacity();
        turnOwner = sessionController.getTurnOwner();
    }

}
