package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

/**
 * Utility class; methods used to color Strings with ANSI characters (extended) - works only on supported terminals
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
        return addBg(colorToInt(color.toUpperCase()), text);
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
