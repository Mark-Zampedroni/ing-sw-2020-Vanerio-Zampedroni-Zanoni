package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

public class Ansi {

    public Ansi() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    private static final String PRE = "\u001B[";

    public static final String RESET = PRE+"0m";
    public static final String CLEAR_LINE = "\33[1A\33[2K";
    public static final String CLEAR_CONSOLE = "\033[H\033[2J";

    private static final String BLACK = "30";
    private static final String RED = "31";
    private static final String GREEN = "32";
    private static final String YELLOW = "33";
    private static final String BLUE = "34";
    private static final String PURPLE = "35";
    private static final String CYAN = "36";
    private static final String WHITE = "37";

    private static String addColor(String color, boolean bright, boolean background) {
        color = color.toUpperCase();
        String brightness = "m";
        int backgroundOffset = 0;
        if(bright) { brightness = ";1m"; }
        if(background) { backgroundOffset = 10; }

        switch (color) {
            case "BLACK" : return buildColor(BLACK,brightness,backgroundOffset);
            case "RED": return buildColor(RED,brightness,backgroundOffset);
            case "GREEN": return buildColor(GREEN,brightness,backgroundOffset);
            case "YELLOW": return buildColor(YELLOW,brightness,backgroundOffset);
            case "BLUE": return buildColor(BLUE,brightness,backgroundOffset);
            case "PURPLE": return buildColor(PURPLE,brightness,backgroundOffset);
            case "CYAN": return buildColor(CYAN,brightness,backgroundOffset);
            default: return buildColor(WHITE,brightness,backgroundOffset);
        }
    }

    private static String buildColor(String color, String brightness, int backgroundOffset) {
        return PRE+(Integer.parseInt(color)+backgroundOffset)+brightness;
    }

    public static String decorateColorString(String text, String color) {
        return addStringColor(color) + text + RESET;
    }

    public static String addStringColor(String color, boolean bright) {
        return addColor(color, bright,false);
    }

    public static String addStringColor(String color) {
        return addColor(color, false,false);
    }

    public static String addBackgroundColor(String color, boolean bright) {
        return addColor(color, bright, true);
    }

    public static String addBackgroundColor(String color) {
        return addColor(color, false, true);
    }

    public static String moveCursorE(int columns) {
        return "\033["+columns+"C"+"\033[0B";
    }

    public static String moveCursorS(int rows) {
        return "\033["+rows+"B";
    }


}
