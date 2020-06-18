package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.gui.Gui;
import it.polimi.ingsw.utility.ArgsParser;

import java.util.Map;

public class ClientApp {


    public static void main(String[] args) {

        Map<String, String> a = ArgsParser.parseArgs(args);

        try {
            if (args[0].toLowerCase().equals("gui")) {
                Gui.getInstance().init(a.getOrDefault("-i", "127.0.0.1"),
                                        (a.containsKey("-p")) ? Integer.parseInt(a.get("-p")) : 7654,
                                        a.containsKey("-log"));
            } else {
                new Cli(a.getOrDefault("-i", "127.0.0.1"),
                        (a.containsKey("-p")) ? Integer.parseInt(a.get("-p")) : 7654,
                        a.containsKey("-log"));
            }
        } catch(Exception e) {
            System.out.println("\nSomething went wrong! Error: "+e.getMessage()+"\nBe sure you are adding the correct args.\n\n" +
                    "E.g. for gui :\njava --module-path \"C:/JAVA_DIRECTORY_PATH/javafx-sdk-11.0.2/lib\" --add-modules javafx.controls --add-modules javafx.fxml -jar client.jar gui 127.0.0.1\n\n" +
                    "E.g. for cli :\njava -jar client.jar cli 127.0.0.1\n\n");
        }
    }
}
