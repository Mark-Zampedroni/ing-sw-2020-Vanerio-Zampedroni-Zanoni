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
import it.polimi.ingsw.utility.enumerations.GameState;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

class SavedDataClass implements Serializable {

    private static final long serialVersionUID= 4L;

    private Session session;
    private StateController stateController;
    private int state; //1 lobby, 2 turno, 3 selezione

    private GameState gameState;

    private int gameCapacity;

    private String turnOwner;

    private List<Player> views;
    //qualcosa che abbia i dati relativi alla connessione

    SavedDataClass(SessionController sessionController){
        session = sessionController.getSession();
        stateController = sessionController.getStateController();
        saveSessionController(sessionController);
        if (stateController instanceof LobbyController) {
            //salvo le cose della lobby
            state=1;
        }
        if (stateController instanceof TurnController) {
            state=2;
            //salvo le cose del turno
            //salvo le cose dell'action controller
        }
        if (stateController instanceof SelectionController) {
            state=3;
            //salvo le cose della selezione
        }
    }

    GameState getGameState() {return gameState;}

    int getState() {return state;}

    Session getSession() { return session; }

    StateController getStateController() {return stateController;}

    List<Player> getPlayersNames() {return views;}

    int getGameCapacity() {return gameCapacity;}

    String getTurnOwner() {return turnOwner;}

    void saveSessionController(SessionController sessionController) {
        gameState = sessionController.getState();
        gameCapacity = sessionController.getGameCapacity();
        turnOwner = sessionController.getTurnOwner();
        views = sessionController.getPlayers();
    }
}
