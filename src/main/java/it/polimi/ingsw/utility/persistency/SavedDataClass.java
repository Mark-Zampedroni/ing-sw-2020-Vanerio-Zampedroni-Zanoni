package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.controller.states.actionControl.ActionController;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.TurnController;
import it.polimi.ingsw.mvc.model.Session;


import java.io.Serializable;

class SavedDataClass implements Serializable {

    private static final long serialVersionUID= 4L;

    private final Session session;
    private final SessionController sessionController;
    private TurnController turnController;
    private ActionController actionController;
    //qualcosa che abbia i dati relativi alla connessione

    SavedDataClass(SessionController sessionController){
        session = sessionController.getSession();
        this.sessionController = sessionController;
        //turnController = sessionController.getTurnController();
        //actionController = sessionController.getTurnController().getActionController();
    }

    Session getSession() {
        return session;
    }

    SessionController getSessionController() {
        return sessionController;
    }

    TurnController getTurnController(){
        return turnController;
    }

    ActionController getActionController(){
        return actionController;
    }
}
