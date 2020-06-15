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
        try {
            if (args[0].toLowerCase().equals("gui")) {
                Gui.getInstance().init((args.length == 1) ? "127.0.0.1" : args[1], 7654);
            } else {
                new Cli((args.length == 2) ? "127.0.0.1" : args[1], 7654); //79.50.167.55:7654
            }
        } catch(IndexOutOfBoundsException e) {
            System.out.println("\nSomething went wrong! Be sure you are adding the correct args.\n\n" +
                    "E.g. for gui :\njava --module-path \"C:/JAVA_DIRECTORY_PATH/javafx-sdk-11.0.2/lib\" --add-modules javafx.controls --add-modules javafx.fxml -jar client.jar gui 127.0.0.1\n\n" +
                    "E.g. for cli :\njava -jar client.jar cli 127.0.0.1\n");
        }
    }
}
