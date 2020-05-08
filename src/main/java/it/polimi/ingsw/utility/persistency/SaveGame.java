package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.mvc.controller.SessionController;

import java.io.*;

public class SaveGame {

    public static void saveGame(SessionController sessionController) {
        SavedDataClass savedDataClass = new SavedDataClass(sessionController);

        try (FileOutputStream game = new FileOutputStream(new File("santorini.game"))) {
            ObjectOutputStream outputStream = new ObjectOutputStream(game);

            outputStream.writeObject(savedDataClass);
            //va segnalato al server che sto salvando

            outputStream.close();
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("IOException is caught");
        }
    }
}
