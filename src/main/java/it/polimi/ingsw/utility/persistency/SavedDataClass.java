package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.controller.states.LobbyController;
import it.polimi.ingsw.mvc.controller.states.SelectionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.controller.states.actionControl.ActionController;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.TurnController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.utility.enumerations.GameState;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SavedDataClass implements Serializable {

    private static final long serialVersionUID = -8284194909335487737L;

    private Session session;
    private StateController state;
    private Message message;

    private GameState gameState;

    private int gameCapacity;

    private String turnOwner;

    //qualcosa che abbia i dati relativi alla connessione

    SavedDataClass(SessionController sessionController, StateController stateController, Message lastMessage){
        session = sessionController.getSession();
        state = stateController;
        saveSessionController(sessionController);
        message = lastMessage;
    }

    GameState getGameState() {return gameState;}

    Session getSession() { return session; }

    StateController getStateController() {return state;}

    int getGameCapacity() {return gameCapacity;}

    String getTurnOwner() {return turnOwner;}

    Message getMessage() {return message;}

    void saveSessionController(SessionController sessionController) {
        gameState = sessionController.getState();
        gameCapacity = sessionController.getGameCapacity();
        turnOwner = sessionController.getTurnOwner();
    }

}
