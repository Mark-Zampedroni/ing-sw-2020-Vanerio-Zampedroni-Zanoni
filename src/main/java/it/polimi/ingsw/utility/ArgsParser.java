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
     * Mark will fill it
     */
    public static Map<String, String> parseArgs(String[] args) {
        Map<String, String> temp = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-")) {
                if (i < args.length - 1 && !args[i + 1].startsWith("-")) {
                    temp.put(args[i], args[i + 1]);
                } else {
                    temp.put(args[i], "");
                }
            }
        }
        return temp;
    }

}
