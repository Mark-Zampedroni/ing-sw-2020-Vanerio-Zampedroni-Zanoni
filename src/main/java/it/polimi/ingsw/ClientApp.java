package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Ansi;
import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;
import it.polimi.ingsw.utility.enumerations.Colors;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ClientApp {

    public static void main(String[] args) {
        new Cli("127.0.0.1", 7654);


        //Map<String, Colors> players = new HashMap<>();
        //players.put("Mark",Colors.BLUE);
        //players.put("Marco",Colors.WHITE);
        //players.put("Stefano",Colors.BROWN);
        //CliScene.printLobbyScreen("Test",players,false);
    }
}
