package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.MVC.controller.ActionController;
import it.polimi.ingsw.MVC.controller.SessionController;
import it.polimi.ingsw.MVC.controller.TurnController;
import it.polimi.ingsw.MVC.model.Session;


import java.io.Serializable;

class SavedDataClass implements Serializable {

    private static final long serialVersionUID= 4L;

    private Session session;
    private SessionController sessionController;
    private TurnController turnController;
    private ActionController actionController;
    //qualcosa che abbia i dati relativi alla connessione

    SavedDataClass(SessionController sessionController){
        session=sessionController.getSession();
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
