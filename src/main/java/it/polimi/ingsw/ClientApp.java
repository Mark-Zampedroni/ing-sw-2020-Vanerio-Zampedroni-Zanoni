package it.polimi.ingsw;

import it.polimi.ingsw.mvc.view.cli.Cli;
import it.polimi.ingsw.mvc.view.gui.Gui;
import it.polimi.ingsw.utility.ArgsParser;

import java.util.Map;
import java.util.logging.Logger;
/**
 * Executable class, it starts the client
 */
public class ClientApp {

    /**
     * Starts the client
     *
     * @param args contains the indication for the ip, the port, the type of client and the logging
     */
    public static void main(String[] args) {

        Map<String, String> a = ArgsParser.parseArgs(args);

        try {
            if (args[0].equalsIgnoreCase("cli")) {
                new Cli(a.getOrDefault("-i", "127.0.0.1"),
                        (a.containsKey("-p")) ? Integer.parseInt(a.get("-p")) : 7654,
                        a.containsKey("-log"));
            } else {
                Gui.getInstance().init(a.getOrDefault("-i", "127.0.0.1"),
                        (a.containsKey("-p")) ? Integer.parseInt(a.get("-p")) : 7654,
                        a.containsKey("-log"));
            }
        } catch (Exception e) {
            Logger.getLogger("ClientApp").info("\n\nSomething went wrong! Error: " + e.getMessage() + "\nBe sure you are adding the correct args.\n\n" +
                    "E.g. for gui :\njava --module-path \"C:/JAVA_DIRECTORY_PATH/javafx-sdk-11.0.2/lib\" --add-modules javafx.controls --add-modules javafx.fxml -jar client.jar gui 127.0.0.1\n\n" +
                    "E.g. for cli :\njava -jar client.jar cli 127.0.0.1\n\n");
        }
    }
}
