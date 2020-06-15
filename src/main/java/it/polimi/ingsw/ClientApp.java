package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.cli.CliScene;
import it.polimi.ingsw.mvc.view.gui.Gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientApp {

    public static void main(String[] args) {
        /*
        if(args[0].toLowerCase().equals("cli")) {
            new Cli((args.length == 1) ? "127.0.0.1" : args[1], 7654); //79.50.167.55:7654
        } else if(args[0].toLowerCase().equals("gui")) {
            Gui.getInstance().init((args.length == 1) ? "127.0.0.1" : args[1], 7654);
        }
        else {
            new Cli( "127.0.0.1" , 7654); //79.50.167.55:7654
        }*/
        Gui.getInstance().init("127.0.0.1",7654);
    }
}
