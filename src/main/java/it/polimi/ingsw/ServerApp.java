package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utility.ArgsParser;

import java.util.Map;

public class ServerApp {

    static boolean FAULT_TOLERANCE = true;

    public static void main(String []args) {

        Map<String, String> a = ArgsParser.parseArgs(args);

        try {
            new Server((a.containsKey("-p") ? Integer.parseInt(a.get("-p")) : 7654), a.containsKey("-log"));
        } catch (Exception e) {
            System.out.println("\nSomething went wrong! Error: "+e.getMessage()+"\nBe sure you are adding the correct args.\n\n" +
                    "E.g. :\njava -jar server.jar 10000\n\n");
        }
    }

    public static boolean isFeature() {
        return FAULT_TOLERANCE;
    }
}
