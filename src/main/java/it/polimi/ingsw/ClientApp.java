package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;

public class ClientApp {

    public static void main(String[] args) {
        new Cli("127.0.0.1", 7654);
        /*System.out.println(Ansi.addStringColor("RED") + SceneBuilder.SANTORINI_TITLE + Ansi.RESET);
        System.out.println(Ansi.addStringColor("RED", true) + SceneBuilder.SANTORINI_TITLE + Ansi.RESET);
        System.out.println(Ansi.addBackgroundColor("CYAN") + SceneBuilder.SANTORINI_TITLE + Ansi.RESET);
        System.out.println(Ansi.addBackgroundColor("CYAN",true) + SceneBuilder.SANTORINI_TITLE + Ansi.RESET);*/
    }
}
