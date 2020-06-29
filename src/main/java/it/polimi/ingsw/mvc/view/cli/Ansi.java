package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

/**
 * Utility class; methods used to color Strings with ANSI characters - works only on supported terminals
 */
public class Ansi {

    /**
     * ANSI character that clears the whole console window
     */
    public static final String CLEAR_CONSOLE = "\033[H\033[2J";

    private static final String PRE = "\u001B[";
    /**
     * ANSI characters terminator
     */
    public static final String RESET = PRE + "0m";

    private static final String BLACK = "30";
    private static final String RED = "31";
    private static final String GREEN = "32";
    private static final String YELLOW = "33";
    private static final String BLUE = "34";
    private static final String PURPLE = "35";
    private static final String CYAN = "36";
    private static final String WHITE = "37";
    private static final int E_BROWN = 94;
    private static final int E_WHITE = 231;
    private static final int E_BLUE = 75;

    /**
     * This class shouldn't be instantiated
     *
     * @throws NotInstantiableClass when the constructor is called
     */
    private Ansi() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * Colors the given string with the specified color
     *
     * @param color color used
     * @return the colored string
     */
    private static String addColor(String color) {
        color = color.toUpperCase();
        String brightness = "m";
        int backgroundOffset = 0;

        switch (color) {
            case "BLACK":
                return buildColor(BLACK, brightness, backgroundOffset);
            case "RED":
                return buildColor(RED, brightness, backgroundOffset);
            case "GREEN":
                return buildColor(GREEN, brightness, backgroundOffset);
            case "YELLOW":
                return buildColor(YELLOW, brightness, backgroundOffset);
            case "BLUE":
                return buildColor(BLUE, brightness, backgroundOffset);
            case "PURPLE":
                return buildColor(PURPLE, brightness, backgroundOffset);
            case "CYAN":
                return buildColor(CYAN, brightness, backgroundOffset);
            default:
                return buildColor(WHITE, brightness, backgroundOffset);
        }
    }

    /**
     * Decorates the given string adding a color / changing its brightness / adding a background color
     *
     * @param color            color to use on the string
     * @param brightness       id for the desired brightness
     * @param backgroundOffset id of the color to use on the background
     * @return the decorated string
     */
    private static String buildColor(String color, String brightness, int backgroundOffset) {
        return PRE + (Integer.parseInt(color) + backgroundOffset) + brightness;
    }

    /**
     * Converts a color to its numeric id
     *
     * @param color color to convert
     * @return id of the color
     */
    public static int colorToInt(String color) {
        switch (color) {
            case "BROWN":
                return E_BROWN;
            case "BLUE":
                return E_BLUE;
            default:
                return E_WHITE;
        }
    }

    /**
     * Decorates a string adding a colored background
     *
     * @param colorCode id of the color to add on the background
     * @param string    string to decorate
     * @return decorated string
     */
    public static String addBg(int colorCode, String string) {
        return "\u001b[48;5;" + colorCode + "m" + string + RESET;
    }


    /**
     * Colors a string
     *
     * @param text  string to decorate
     * @param color color to add
     * @return colored string
     */
    public static String decorateColorString(String text, String color) {
        return addColor(color) + text + RESET;
    }

    /**
     * Moves the cursor to the right by the number of characters given
     *
     * @param columns width of the movement (as number of characters)
     * @return string decorated with the cursor movement
     */
    public static String moveCursorE(int columns) {
        return "\033[" + columns + "C" + "\033[0B";
    }

    /**
     * Moves the cursor to the top by the number of characters given
     *
     * @param rows height of the movement (as number of characters)
     * @return string decorated with the cursor movement
     */
    public static String moveCursorN(int rows) {
        return "\u001b[" + rows + "A";
    }


}
