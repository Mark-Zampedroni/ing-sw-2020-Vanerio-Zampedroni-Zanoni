package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.mvc.model.Session;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class ReloadGame {

    public static void restartGame() {
        SavedDataClass savedGame = deserializeFile();
        Session session = savedGame.getSession();
        //SessionController sessionController= savedGame.getSessionController();
        //StateController stateController= savedGame.getStateController();
        //reinserisco i dati salvati all'interno del main(?) credo
    }


    static SavedDataClass deserializeFile() {
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
        return savedData;
    }
}