package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.GameState;

import java.io.*;
import java.util.logging.Logger;

public class SaveData implements Serializable {

    private static final long serialVersionUID = -8284194909335487737L;

    private boolean actionDone;

    private Session session;
    private StateController state;
    private Message message;

    private GameState gameState;

    private int gameCapacity;

    private String turnOwner;

    SaveData(SessionController sessionController, StateController stateController, Message lastMessage, Boolean flag) {
        actionDone = flag;
        session = sessionController.getSession();
        state = stateController;
        saveSessionController(sessionController);
        message = lastMessage;
    }

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

    public GameState getGameState() {
        return gameState;
    }

    public Session getSession() {
        return session;
    }

    public StateController getStateController() {
        return state;
    }

    public int getGameCapacity() {
        return gameCapacity;
    }

    public String getTurnOwner() {
        return turnOwner;
    }

    public Message getMessage() {
        return message;
    }

    public boolean getActionDone() {
        return actionDone;
    }

    void saveSessionController(SessionController sessionController) {
        gameState = sessionController.getState();
        gameCapacity = sessionController.getGameCapacity();
        turnOwner = sessionController.getTurnOwner();
    }

}
