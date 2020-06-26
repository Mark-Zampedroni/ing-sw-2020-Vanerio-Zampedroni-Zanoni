package it.polimi.ingsw.utility;

import java.util.HashMap;
import java.util.Map;

/**
 * Parser for the the characters passed from the command line
 */
public class ArgsParser {

    /**
     * This class shouldn't be instantiated
     */
    private ArgsParser() {
    }

    /**
     * Splits the arguments given to {@link it.polimi.ingsw.ClientApp ClientApp} or
     * {@link it.polimi.ingsw.ServerApp ServerApp} creating a map parameter-value
     *
     * @param args list of arguments
     * @return map parameter-value
     */
    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> temp = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-"))
                temp.put(args[i], (i < args.length - 1 && !args[i + 1].startsWith("-")) ? args[i + 1] : "");
        }
        return temp;
    }

}
