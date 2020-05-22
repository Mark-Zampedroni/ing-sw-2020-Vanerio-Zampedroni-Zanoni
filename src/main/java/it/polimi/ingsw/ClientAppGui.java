package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.gui.Gui;
import javafx.application.Application;

public class ClientAppGui {
    public static void main(String[] args){
        Gui.getInstance().init((args.length == 0) ? "127.0.0.1" : args[0], 7654);
            }
}
