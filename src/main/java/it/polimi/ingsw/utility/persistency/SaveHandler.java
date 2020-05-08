package it.polimi.ingsw.utility.persistency;


import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.network.server.Server;

import java.util.Scanner;

public class SaveHandler extends Thread {

    Server server;
    SessionController sessionController;
    Boolean input;

    public SaveHandler(Server server, SessionController sessionController, Boolean input){
        this.server= server;
        this.sessionController = sessionController;
        this.input=input;
    }

    public void setInput(Boolean pass) {
        input=pass;
    }

    static String savedGame=null;

    @Override
    public void run() {
        while (input) {
            Scanner input = new Scanner(System.in);
            String s = input.nextLine();
            if (s.equals("Save All")) {
                break;
            }
        }
        new SaveGame(sessionController);
        server.interrupt();
        this.interrupt();
        System.out.println("Game Saved");
    }
}
