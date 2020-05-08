package it.polimi.ingsw.utility.persistency;


import it.polimi.ingsw.mvc.controller.SessionController;

import java.io.*;

public class SaveGame {

    public SaveGame(SessionController sessionController) {
        SavedDataClass savedDataClass = new SavedDataClass(sessionController);

        try (FileOutputStream game = new FileOutputStream(new File("santorini.game.ser"))) {

            ObjectOutputStream outputStream = new ObjectOutputStream(game);

            outputStream.writeObject(savedDataClass);

            outputStream.close();

            outputStream.flush();

        } catch (IOException e) {
            System.out.println("IOException is caught, during saving");
            e.printStackTrace();
        }
    }
}
