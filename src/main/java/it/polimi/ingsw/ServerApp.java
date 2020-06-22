package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.ArgsParser;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Executable class, it starts the server
 */
public class ServerApp {

    static final boolean FAULT_TOLERANCE = true;

    /**
     * Starts the server
     *
     * @param args contains the indication for the ip, the port and the logging
     */
    public static void main(String[] args) {

        Map<String, String> a = ArgsParser.parseArgs(args);

        try {
            new Server((a.containsKey("-p") ? Integer.parseInt(a.get("-p")) : 7654), a.containsKey("-log"));
        } catch (Exception e) {
            Logger.getLogger("ServerApp").info("\n\nSomething went wrong! Error: " + e.getMessage() + "\nBe sure you are adding the correct args.\n\n" +
                    "E.g. :\njava -jar server.jar 10000\n\n");
        }
    }

    /**
     * Returns {@code true} if the fault tolerance is active
     */
    public static boolean isFeature() {
        return FAULT_TOLERANCE;
    }
}
