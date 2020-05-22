package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientAppCli {

    public static void main(String[] args) {
        //new Cli("127.0.0.1", 7654); //79.50.167.55:7654
        new Cli((args.length == 0) ? "127.0.0.1" : args[0], 7654); //79.50.167.55:7654

        /*
        for(int i = 0; i < 4; i++) {
            System.out.println(CliScene.getLevelUpTo(i)+"\n");
        }
         */

        /*
        System.out.println(CliScene.colorLevels(CliScene.getLevelUpTo(3)+"\n",3));
        System.out.println(CliScene.colorLevels(CliScene.getLevelUpTo(2)+"\n",2));
        System.out.println(CliScene.colorLevels(CliScene.getLevelUpTo(1)+"\n",1));
        System.out.println(CliScene.colorLevels(CliScene.getLevelUpTo(0)+"\n",0));
        */

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
