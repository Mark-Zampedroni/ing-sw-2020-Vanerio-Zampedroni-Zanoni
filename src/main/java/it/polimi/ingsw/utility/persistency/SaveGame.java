package it.polimi.ingsw.utility.persistency;


import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.controller.states.StateController;
import it.polimi.ingsw.network.messages.Message;

import java.io.*;

public class SaveGame {

    //lo faccio statico
    // salvo su doppio file

    public static void saveGame(SessionController sessionController, StateController stateController, Message lastmessage, Boolean flag) {
        if (ServerApp.isFeature()) {
        SavedDataClass savedDataClass = new SavedDataClass(sessionController, stateController, lastmessage, flag);

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
}
