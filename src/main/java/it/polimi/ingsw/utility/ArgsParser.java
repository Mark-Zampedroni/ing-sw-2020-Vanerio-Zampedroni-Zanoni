package it.polimi.ingsw.utility;

import java.util.HashMap;
import java.util.Map;

public class ArgsParser {

    public static Map<String,String> parseArgs(String []args) {
        Map<String,String> temp = new HashMap<>();
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-")) {
                if(i < args.length-1 && !args[i+1].startsWith("-")) {
                    temp.put(args[i],args[i+1]);
                }
                else {
                    temp.put(args[i],"");
                }
            }
        }
        return temp;
    }

}
