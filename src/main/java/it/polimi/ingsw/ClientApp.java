package it.polimi.ingsw;


import it.polimi.ingsw.MVC.view.CLI.Ansi;
import it.polimi.ingsw.MVC.view.CLI.SceneBuilder;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.network.client.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientApp {

    public static List<Colors> colors = new ArrayList<>(Arrays.asList(Colors.values()));

    public static void main(String[] args) {
        new Client("127.0.0.1", 7654,0);
        /*System.out.println(Ansi.addStringColor("RED") + SceneBuilder.SANTORIN_TITLE + Ansi.RESET);
        System.out.println(Ansi.addStringColor("RED", true) + SceneBuilder.SANTORIN_TITLE + Ansi.RESET);
        System.out.println(Ansi.addBackgroundColor("CYAN") + SceneBuilder.SANTORIN_TITLE + Ansi.RESET);
        System.out.println(Ansi.addBackgroundColor("CYAN",true) + SceneBuilder.SANTORIN_TITLE + Ansi.RESET);*/
    }
}
