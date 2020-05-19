package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.network.server.ServerConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.logging.Logger;

public class ReloadGame {

    static SavedDataClass savedData;
    static SessionController sessionController;

    public static void restartGame() {
        deserializeFile();
    }


    static void deserializeFile() {
        String filename = "santorini.game.ser";
        SavedDataClass savedData = null;
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream input = new ObjectInputStream(file);

            savedData = (SavedDataClass) input.readObject();

            input.close();
            file.close();
        } catch (IOException ex) {
            System.out.println("IOException is caught");
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException is caught");
        }
        ReloadGame.savedData = savedData;
    }

    /*
    public static SessionController reloadSessionController(List<ServerConnection> connections, Logger LOG) {
        sessionController = new SessionController(connections, LOG, savedData.getSession(), savedData.getGameState(), savedData.getStateController());
        return sessionController;
        //quando riprendo turno o selezione assegnargli il sessionController giusto
    }*/
}