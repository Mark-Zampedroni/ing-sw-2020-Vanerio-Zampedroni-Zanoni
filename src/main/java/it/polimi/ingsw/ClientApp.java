package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientApp {

    public static void main(String[] args) {
        //new Cli("127.0.0.1", 7654);

        System.out.println(CliScene.BOARD);

        //Map<String, Colors> players = new HashMap<>();
        //players.put("Mark",Colors.BLUE);
        //players.put("Marco",Colors.WHITE);
        //players.put("Stefano",Colors.BROWN);
        //CliScene.printLobbyScreen("Test",players,false);
        //List<String> chosen = new ArrayList<>();
        //CliScene.printChallengerSelection("Select god: ", chosen, 4, 3,true);

        //List<String> chosen = new ArrayList<>();
        //chosen.add("APOLLO");
        //chosen.add("HERA");

        //CliScene.printChallengerGodsUpdate("Test",chosen,3);

        //Map<String, String> choices = new HashMap();
        //choices.put("Marco","APOLLO");
        //choices.put("Mark","HERA");

        //CliScene.printPlayerGodSelection("Marco",choices,chosen,2);
    }
}
