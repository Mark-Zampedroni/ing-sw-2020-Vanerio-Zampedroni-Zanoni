package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.dto.DtoTile;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

import java.io.PrintStream;
import java.util.*;

/**
 * Class used by the {@link Cli CLI} to create and print the scenes on the Terminal.
 * Colors are required for a better user experience, so be sure to use a terminal
 * compatible with ANSI characters. For further information refer to {@link Ansi this class}
 */
public class CliScene {

    private static final String P_TOP = "/ _  \\---/  _ \\";
    private static final String P_MID = "( (. \\     / .) )";
    private static final String P_BOT = "\\___/-----\\___/";
    private static final String C = "| | | |";
    private static final String B_TOP = "(0oUwUo0)";
    private static final String B_MID = "#".repeat(9);
    private static final String B_BOT = "z".repeat(13);
    public static final String ROW = "-".repeat(100);
    private static final String DOWN = "     \n";
    private static final String SPACE = "       ";
    private static final String SPACE2 = "      ";
    private static final String DOWN2 = "\\ \n";
    private static final String B_LINE = " ".repeat(31) + "|" + " ".repeat(30) + "|" + " ".repeat(31);
    private static final String B_LINE_SMALL = " ".repeat(34) + "|" + " ".repeat(34);
    private static final String B_LINE_B = " ".repeat(26) + "\n";
    private static final String SPLIT_VALUE = "\\r?\\n";

    private static final String TOP_LOBBY =
            "    " + "_".repeat(94) + "   \n" +
                    "   /" + " ".repeat(94) + "\\  \n" +
                    "  |" + "_".repeat(96) + "| \n" +
                    "  |" + " ".repeat(96) + "| \n" +
                    "  |'" + "____'''''".repeat(10) + "____'| \n" +
                    "   " + P_TOP + " ".repeat(66) + P_TOP + "  \n" +
                    "  " + P_MID + " ".repeat(64) + P_MID + " \n" +
                    "   " + P_BOT + " ".repeat(66) + P_BOT + "  \n" +
                    SPACE + C + " ".repeat(28) + "REGISTERED PLAYERS" + " ".repeat(28) + C + SPACE2;
    private static final String TITLE =
            "      .---.                                          ,___ \n" +
                    "     / .-, .                   ________            .'  _  \\  [ ]          [ ]\n" +
                    "     , \\ |_/   ,'.         .- '--. .---' ..--..   /  .' \\  . .-.     ._   .-.\n" +
                    "      \\ \\     .   \\    |\\ /  '\\  | |     ' --. \\   | |  / /  | | |\\ /   \\ | |\n" +
                    "       \\ \\    | |\\ \\   | \\ /| .  | |    .--.  \\ \\  | | / /   | | | \\ /| . | |\n" +
                    "        \\ \\   | | | .  |  . | |  | |   / __ \\  \\ . |  ' /    | | |  . | | | |\n" +
                    "  --.    \\ \\  | '-' |  | |  | |  | |  / /  \\ . | | |   \\     | | | |  | | | |\n" +
                    " / /      . \\(  .-. |  | |  | .  | | .  . \\// / /  | |\\ \\    | | | |  | . | |\n" +
                    " \\ \\     /  / | | | |  | |  |  \\ | |  \\  \\   / /   | | \\ \\   | | | |  | \\ | |\n" +
                    "  \\ '---'  /  | | | |  ._.   '--'| |   \\  '-' /    .-   \\ \\  | | ._.   '- | |\n" +
                    "   '-.___.'   '-'  \\.)           '-'    '----'           '-' '-'          '-'";
    public static final String CREDITS = "Game developed by Stefano Vanerio, Mark Zampedroni and Marco Zanoni \n" +
            "Original board version published by Roxley Games";
    private static final int SCREEN_WIDTH = 192;
    private static final String BOTTOM_LOBBY =
            SPACE + C + " ".repeat(74) + C + "      \n" +
                    SPACE2 + B_TOP + " ".repeat(72) + B_TOP + DOWN +
                    SPACE2 + B_MID + " ".repeat(72) + B_MID + DOWN +
                    "   ." + B_BOT + "-".repeat(68) + B_BOT + ".  \n" +
                    "  /" + "_".repeat(96) + "\\ ";
    private static final String TOP_CHALLENGER =
            "    " + "_".repeat(114) + "\n" +
                    "   /" + " ".repeat(114) + DOWN2 +
                    "  |'____'''''____''''____''''___''''___''''_____''''____''''_____''''_____''''_____''''_____''''_____''''____'''''____'|   \n" +
                    "   " + P_TOP + "                           ( )                              ( )                       " + P_TOP + "\n" +
                    "  " + P_MID + "                           |                                |                       " + P_MID + "\n" +
                    "   " + P_BOT + "                            |                                |                        " + P_BOT + " \n" +
                    SPACE + C + "                                |                                |                            " + C + "     ";
    private static final String BOTTOM_CHALLENGER =
            SPACE2 + B_TOP + "                               |                                |                           " + B_TOP + "\n" +
                    SPACE2 + B_MID + "                              ( )                              ( )                          " + B_MID + "    \n" +
                    "   ." + B_BOT + "-".repeat(88) + B_BOT + ".\n" +
                    "  /" + "_".repeat(116) + "\\";
    private static final String TOP_SELECTION_3 =
            "    " + "_".repeat(114) + " \n" +
                    "   /" + " ".repeat(114) + DOWN2 +
                    "  |'____'''''____'''____''''____''''____''''____'''____''''____''''____'''____''''____''''____''''____'''____'''''____'|\n" +
                    "   " + P_TOP + "                          ( )                            ( )                          " + P_TOP + "\n" +
                    "  " + P_MID + "                          |                              |                          " + P_MID + "\n" +
                    "   " + P_BOT + "                           |                              |                           " + P_BOT + " \n" +
                    SPACE + C + B_LINE + C + DOWN +
                    SPACE + C + B_LINE + C + "\n" +
                    SPACE + C + B_LINE + C;
    private static final String BOTTOM_SELECTION_3 =
            SPACE + C + B_LINE + C + "\n" +
                    SPACE + C + B_LINE + C + "\n" +
                    SPACE2 + B_TOP + "                              |                              |                              " + B_TOP + "\n" +
                    SPACE2 + B_MID + "                             ( )                            ( )                             " + B_MID + "  \n" +
                    "   ." + B_BOT + "-".repeat(88) + B_BOT + ".\n" +
                    "  /" + "_".repeat(116) + "\\";
    private static final String TOP_SELECTION_2 =
            "    " + "_".repeat(89) + " \n" +
                    "   /" + " ".repeat(89) + DOWN2 +
                    "  |'____'''''____''''____''''____''''____'''''_____'''''____''''____''''____''''____'''''____'|\n" +
                    "   " + P_TOP + "                             ( )                             " + P_TOP + "\n" +
                    "  " + P_MID + "                             |                             " + P_MID + "\n" +
                    "   " + P_BOT + "                              |                              " + P_BOT + " \n" +
                    SPACE + C + B_LINE_SMALL + C + DOWN +
                    SPACE + C + B_LINE_SMALL + C + "\n" +
                    SPACE + C + B_LINE_SMALL + C;
    private static final String BOTTOM_SELECTION_2 =
            SPACE + C + B_LINE_SMALL + C + "\n" +
                    SPACE + C + B_LINE_SMALL + C + "\n" +
                    SPACE + C + B_LINE_SMALL + C + "\n" +
                    SPACE2 + B_TOP + "                                 |                                 " + B_TOP + "\n" +
                    SPACE2 + B_MID + "                                ( )                                " + B_MID + "\n" +
                    "   ." + B_BOT + "-".repeat(63) + B_BOT + ".\n" +
                    "  /" + "_".repeat(91) + "\\";

    private static final String[] TOP_SELECTION = {TOP_SELECTION_2, TOP_SELECTION_3};
    private static final String[] BOTTOM_SELECTION = {BOTTOM_SELECTION_2, BOTTOM_SELECTION_3};

    private static final String ARROWS_CHALLENGER =
            B_LINE_B +
                    B_LINE_B +
                    "  Type 1 and 2 to browse  \n" +
                    "  more available gods     \n" +
                    B_LINE_B +
                    B_LINE_B +
                    "      /|__       __|\\      \n" +
                    "   1 /    |     |    \\ 2   \n" +
                    "     \\  __|     |__  /     \n" +
                    "      \\|           |/      ";

    private static PrintStream out = new PrintStream(System.out, true);

    private static int cursorOffset;
    private static boolean enableInput;

    private static String windowMessage = " Empty Message ...";

    private static int[] levellength = {17, 13, 9, 5}; //-4 for each level
    private static int[] levelheight = {8, 6, 4, 2}; //-2 for each level

    /**
     * CliScene is not instantiable
     *
     * @throws NotInstantiableClass when instantiated
     */
    private CliScene() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * Gets the screen width
     *
     * @return the screen width as number of characters
     */
    private static double getScreenCharSize() {
        return SCREEN_WIDTH;
    }

    /**
     * Sets a message on the input box of the current scene
     *
     * @param message message to be shown
     * @param input   true if an input reply is required (for input will be added), otherwise false
     */
    public static void setMessage(String message, boolean input) {
        windowMessage = message;
        enableInput = input;
    }

    /**
     * Adds an input box of length width to the scene contained in b
     *
     * @param b     temporary builder for the scene
     * @param width length of the input box (generally the same as the scene)
     */
    private static void inputOutputSlot(StringBuilder b, int width) {
        b.append("-".repeat(width)).append("\n");
        appendEmptyRow(b, width, 1);
        appendAllLinesCentered(b, windowMessage, width, false);
        appendEmptyRow(b, width, 1);
        if (enableInput) {
            b.append(extendSlots("   >>> ", width)).append("\n");
            appendEmptyRow(b, width, 1);
        }
    }

    /**
     * Moves the cursor up of y rows and to the right of x columns
     *
     * @param x horizontal positive (right) movement
     * @param y vertical negative (up) movement
     */
    private static void setCursor(int x, int y) {
        if (enableInput) {
            out.print(Ansi.moveCursorE(cursorOffset + x)); // Da togliere cursorOffset, a volte da problemi
            out.print(Ansi.moveCursorN(y));
            out.flush();
        }
    }

    /**
     * Prints the board screen on console, hides the action candidates
     *
     * @param session         session containing the board to show
     * @param colors          map of each player name to its color
     * @param gods            map of each player name to its god
     * @param turnOwner       name of the current turn owner
     * @param areActionsShown {@code true} if the action candidates marks must be shown
     */
    public static void printBoardScreen(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String turnOwner, boolean areActionsShown) {
        printBoardScreen("", session, colors, gods, null, turnOwner, areActionsShown);
    }

    /**
     * Prints the board screen on console, shows the action candidates
     * @param inputRequest text of the input request
     * @param session session containing the board to show
     * @param colors map of each player name to its color
     * @param gods map of each player name to its god
     * @param possibleActions map of each possible action to the list of the candidate positions for the action
     * @param turnOwner name of the current turn owner
     * @param areActionsShown {@code true} if the action candidates marks must be shown
     */
    public static void printBoardScreen(String inputRequest, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, Map<Action, List<DtoPosition>> possibleActions, String turnOwner, boolean areActionsShown) {
        String board = addBoardScreenBorder(addCoordinates(createBoard(session, colors, possibleActions, areActionsShown))).toString();
        out.println(Ansi.CLEAR_CONSOLE + centerScreen(addSideBar(inputRequest, board, colors, gods, possibleActions, turnOwner).toString(), 20));
        if (possibleActions != null) setCursor(105, 14);
        out.flush();
    }

    /**
     * Adds the sidebar to the board scene
     * @param inputRequest text of the input request
     * @param board board as string (already built)
     * @param colors map of each player name to its color
     * @param gods map of each player name to its god
     * @param possibleActions map of each possible action to the list of the candidate positions for the action
     * @param turnOwner name of the current turn owner
     * @return the board given as parameter decorated with a sidebar
     */
    private static StringBuilder addSideBar(String inputRequest, String board, Map<String, Colors> colors, Map<String, String> gods, Map<Action, List<DtoPosition>> possibleActions, String turnOwner) {
        StringBuilder temp = new StringBuilder();
        String[] pBox = createSideBar(inputRequest, colors, gods, possibleActions, turnOwner).split(SPLIT_VALUE);
        int i = 0;
        for (String s : board.split(SPLIT_VALUE)) {
            temp.append(s);
            if (i < pBox.length) {
                temp.append(pBox[i]);
                i++;
            }
            temp.append("\n");
        }
        return temp;
    }

    /**
     * Creates a sidebar displaying the given parameters
     * @param inputRequest text of the input request
     * @param colors map of each player name to its color
     * @param gods map of each player name to its god
     * @param possibleActions map of each possible action to the list of the candidate positions for the action
     * @param turnOwner name of the current turn owner
     * @return the sidebar built
     */
    private static String createSideBar(String inputRequest, Map<String, Colors> colors, Map<String, String> gods, Map<Action, List<DtoPosition>> possibleActions, String turnOwner) {
        StringBuilder temp = new StringBuilder();
        addPlayersBoardBox(temp, colors, gods);
        addPlayerTurnBox(temp, possibleActions, turnOwner);
        addBoardRequest(temp, inputRequest);
        return temp.toString();
    }

    /**
     * Adds to the sidebar the box with the input request
     * @param sidebar StringBuilder containing the sidebar
     * @param inputRequest text of the input request
     */
    private static void addBoardRequest(StringBuilder sidebar, String inputRequest) {
        String emptyRow = (extendSlots("", 34) + "|\n");
        sidebar.append("  ").append(extendSlots(inputRequest, 32)).append("|\n");
        sidebar.append(emptyRow);
        if (!inputRequest.equals("")) sidebar.append(extendSlots("  >>> ", 34)).append("|\n");
        else sidebar.append(emptyRow);
        sidebar.append(emptyRow.repeat(10));
        sidebar.append("-".repeat(35));
    }

    /**
     * Adds to the sidebar the box containing the possible actions
     * @param sidebar StringBuilder containing the sidebar
     * @param possibleActions map of each possible action to the list of positions candidates
     * @param turnOwner name of the current turn owner
     */
    private static void addPlayerTurnBox(StringBuilder sidebar, Map<Action, List<DtoPosition>> possibleActions, String turnOwner) {
        String emptyRow = (extendSlots("", 34) + "|\n");
        String separator = "-".repeat(34);
        sidebar.append(emptyRow).append(centerLine("ACTIONS", 34, false)).append("|\n")
                .append(separator).append("|\n").append(emptyRow);
        if (possibleActions == null)
            sidebar.append(centerLine("It's " + turnOwner + " turn", 34, false)).append("|\n").append(emptyRow.repeat(9));
        else {
            sidebar.append(centerLine("It's your turn!", 34, false)).append("|\n");
            sidebar.append(emptyRow.repeat(2));
            sidebar.append(extendSlots("  Available actions:", 34)).append("|\n");
            int i = 0;
            for (Action action : possibleActions.keySet()) {
                sidebar.append(emptyRow);
                sidebar.append(extendSlots("  " + i + " -> " + action.toString(), 34)).append("|\n");
                i++;
            }
            while (i < 3) {
                sidebar.append(emptyRow.repeat(2));
                i++;
            }
        }
        sidebar.append(emptyRow);
    }

    /**
     * Adds to the sidebar the box containing the players names, their colors and their gods
     * @param sidebar StringBuilder containing the sidebar
     * @param colors map of each player name to its color
     * @param gods map of each player name to its god
     */
    private static void addPlayersBoardBox(StringBuilder sidebar, Map<String, Colors> colors, Map<String, String> gods) {
        String emptyRow = (extendSlots("", 34) + "|\n");
        String separator = "-".repeat(34);
        sidebar.append(separator).append("-\n")
                .append(emptyRow).append(centerLine("PLAYERS", 34, false)).append("|\n")
                .append(separator).append("|\n").append(emptyRow)
                .append("  Player     - God       - Color  |\n")
                .append(emptyRow);
        for (Map.Entry<String, Colors> e : colors.entrySet()) {
            sidebar.append(" ".repeat(2))
                    .append(extendSlots(e.getKey(), 13))
                    .append(extendSlots(gods.get(e.getKey()), 12))
                    .append(extendSlots(e.getValue().toString(), 5)).append("  |\n")
                    .append(emptyRow);
        }
        if (colors.size() == 2) sidebar.append(emptyRow.repeat(2));
        sidebar.append(emptyRow.repeat(8)).append(separator).append("|\n");
    }

    /**
     * Adds the outer border of the board
     * @param boardWithCoordinates string containing the board decorated with coordinates
     * @return board decorated with the outer border
     */
    private static StringBuilder addBoardScreenBorder(String boardWithCoordinates) {
        StringBuilder temp = new StringBuilder();
        temp.append("-".repeat(99)).append("\n");
        for (String line : boardWithCoordinates.split(SPLIT_VALUE)) {
            temp.append("|").append(line).append(" ".repeat((line.equals("") ? 97 : 2))).append("|").append("\n");
        }
        temp.append("|").append(" ".repeat(97)).append("|").append("\n").append("-".repeat(99)).append("\n");
        return temp;
    }

    /**
     * Adds the coordinates values to the board
     * @param board string containing the board
     * @return board decorated with coordinates
     */
    private static String addCoordinates(String board) {
        StringBuilder temp = new StringBuilder();
        addXCoordinates(temp);
        addYCoordinates(temp, board);
        return temp.toString();
    }

    /**
     * Adds the X axis values to the board in the given StringBuilder
     * @param boardWithoutCoordinates StringBuilder containing the board
     */
    private static void addXCoordinates(StringBuilder boardWithoutCoordinates) {
        List<String> xC = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        boardWithoutCoordinates.append("\n").append(" ".repeat(13));
        xC.forEach(e -> boardWithoutCoordinates.append(e).append(" ".repeat((e.equals("E") ? 9 : 17))));
    }

    /**
     * Adds the Y axis values to the board in the given StringBuilder
     * @param temp StringBuilder where the result will be put
     * @param board string containing the board
     */
    private static void addYCoordinates(StringBuilder temp, String board) {
        int p = 0;
        int a = 0;
        int r = 1;
        temp.append("\n");
        for (String row : board.split(SPLIT_VALUE)) {
            temp.append(" ");
            if (p == 5 || a == 8) {
                temp.append(" ").append(r).append(" ");
                a = 0;
                r++;
            } else {
                temp.append(" ".repeat(3)); // <--- TEST_
                if (p > 4) a++;
            }
            p++;
            temp.append(row).append("\n");
        }
    }

    /**
     * Centers a scene on the screen
     *
     * @param text scene we want to center
     * @return centered scene
     */
    private static StringBuilder centerScreen(String text) {
        return addOffset(text, (getScreenCharSize() - getLongestLine(text)) / 2);
    }

    /**
     * Centers a scene on the screen of length width
     *
     * @param text  scene we want to center
     * @param width width of the screen
     * @return centered scene
     */
    private static StringBuilder centerScreen(String text, int width) {
        return addOffset(text, width);
    }

    /**
     * Adds an offset of whitespaces at the start of all lines in a text
     *
     * @param text text we want to offset
     * @param o    number of whitespaces we want to add (double so it can be given half of the screen length, etc.)
     * @return builder containing the new text
     */
    private static StringBuilder addOffset(String text, double o) {
        StringBuilder temp = new StringBuilder();
        String offset = " ".repeat(Math.max(0, (int) o));
        for (String line : text.split(SPLIT_VALUE)) {
            String newLine = offset + line + "\n";
            temp.append(newLine);
        }
        cursorOffset = offset.length(); // <---------
        return temp;
    }

    /**
     * Creates one line of a certain length containing only whitespaces
     *
     * @param length length of the line we want to create
     * @return generated line
     */
    private static String getEmptyLength(int length) {
        return " ".repeat(Math.max(0, length));
    }

    /**
     * Removes the number of specified lines from the start of a text
     *
     * @param text text we want to shorten from top
     * @return shortened text
     */
    private static String removeLines(String text) {
        StringBuilder b = new StringBuilder();
        int i = 1;
        for (String string : text.split(SPLIT_VALUE)) {
            if (i > 5) {
                b.append(string).append("\n");
            }
            i++;
        }
        return b.toString();
    }

    /**
     * Extends the length of all lines in a text to that of the longest line
     *
     * @param string text containing the lines
     * @return text with all the lines of the same length
     */
    private static String extendSlots(String string) {
        StringBuilder temp = new StringBuilder();
        int width = getLongestLine(string);
        for (String s : string.split(SPLIT_VALUE)) {
            temp.append(s);
            temp.append(" ".repeat(Math.max(0, width - s.length() + 1)));
            temp.append("\n");
        }
        return temp.toString();
    }

    /**
     * Extends the width of a text adding whitespace characters until the desired length is reached
     *
     * @param text   text to extend
     * @param length desired length for text
     * @return extended text
     */
    private static String extendSlots(String text, int length) {
        return text + " ".repeat(Math.max(0, length - text.length()));
    }

    /**
     * Adds left and right margin to a text with ANSI characters in order to center it
     *
     * @param text text with ANSI characters to center
     * @return centered text
     */
    private static String fixAnsi(String text) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        int squareBrackets = text.length() - text.replaceAll("\\[", "").length();
        if (squareBrackets > 1) {
            temp.append(" ".repeat(2 * squareBrackets));
            temp.insert(0, " ".repeat(2 * squareBrackets + 1));
        }
        return temp.toString();
    }

    /**
     * Appends to the StringBuilder b a number of empty lines
     *  @param b      builder to which the empty lines will be appended
     *
     */
    private static void appendEmptyLine(StringBuilder b) {
        b.append("\n".repeat(Math.max(0, 3)));
    }

    /**
     * Appends to the StringBuilder b a number of whitespace lines of length width
     *
     * @param b     builder to which the whitespace lines will be appended
     * @param width width of each line
     * @param times number of lines
     */
    private static void appendEmptyRow(StringBuilder b, int width, int times) {
        for (int x = 0; x < times; x++) {
            b.append(getEmptyLength(width)).append("\n");
        }
    }

    /**
     * Centers a text of one line on a specified length, if text is longer than length it doesn't apply changes
     *
     * @param b       builder to which the new lines will be appended
     * @param text    text containing the lines we want to be centered
     * @param length  total length of each new line (old text + same margin on both sides)
     * @param fixAnsi true if text contains ANSI characters, as they are read as normal characters by the parser
     *                but we do not need to count them for the total length
     */
    private static void appendAllLinesCentered(StringBuilder b, String text, int length, boolean fixAnsi) {
        for (String line : text.split(SPLIT_VALUE)) {
            b.append(centerLine(line, length, fixAnsi)).append("\n");
        }
    }

    /**
     * Adds a left and right margin to all the lines of a text
     *  @param b     builder to which the new lines will be appended
     * @param text  text we want to add margin to
     * @param left  left margin
     */
    private static void appendAllLinesCentered(StringBuilder b, String text, int left) {
        for (String line : text.split(SPLIT_VALUE)) {
            b.append(" ".repeat(left)).append(line).append(" ".repeat(0)).append("\n");
        }
    }

    /**
     * Centers a text of one line on a specified length, if text is longer than length it doesn't apply changes
     *
     * @param text    text we want to be centered
     * @param length  total length of the new text (old text + same margin on both sides)
     * @param fixAnsi true if text contains ANSI characters, as they are read as normal characters by the parser
     *                but we do not need to count them for the total length
     * @return text with equal margins on both sides and total width length
     */
    private static String centerLine(String text, int length, boolean fixAnsi) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        for (int i = (length - text.length()) / 2; i > 0; i--) {
            temp.insert(0, " ");
            temp.append(" ");
        }
        if ((length - text.length()) % 2 == 1) {
            temp.append(" ");
        }
        return (fixAnsi) ? fixAnsi(temp.toString()) : temp.toString();
    }

    /**
     * Adds a left and right margin to a text of one line
     *
     * @param text  text to which we want to add a margin
     * @param left  left margin length
     * @param right right margin length
     * @return text with the left and right margins added
     */
    private static String centerLine(String text, int left, int right) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        temp.insert(0, " ".repeat(left));
        temp.append(" ".repeat(right));
        return temp.toString();
    }

    /**
     * Gets the length of the longest line in a text that ends with a separator (\r or \n)
     *
     * @param text text from which we want to get the length of the longest line
     * @return length of the longest line
     */
    private static int getLongestLine(String text) {
        int max = 0;
        for (String line : text.split(SPLIT_VALUE)) {
            if (line.length() > max) {
                max = line.length();
            }
        }
        return max;
    }

    /**
     * Adds borders to a box or scene
     *
     * @param b     builder of the box or scene
     * @param width width of the scene
     * @return new builder with the same old box or scene surrounded by a border
     */
    private static StringBuilder decorateSquare(StringBuilder b, int width) {
        StringBuilder temp = new StringBuilder();
        appendEmptyLine(temp);
        temp.append(".").append("-".repeat(width)).append(".\n");
        for (String string : b.toString().split(SPLIT_VALUE)) {
            temp.append("|").append(string).append("|");
            temp.append("\n");
        }
        temp.append("'").append("-".repeat(width)).append("'\n");
        return temp;
    }

    /**
     * Adds ionic columns at the sides of a scene
     *
     * @param b temporary builder for the scene
     * @return new builder of the scene with the columns added
     */
    private static StringBuilder decorateColumns(StringBuilder b) {
        StringBuilder temp = new StringBuilder();
        for (String string : b.toString().split(SPLIT_VALUE)) {
            temp.append(" | | | |").append(string).append(C);
            temp.append("\n");
        }
        return temp;
    }

    /**
     * Creates a tile
     *
     * @param height   height of the tower on the tile
     * @param hasDome  true if the tower has a dome, otherwise false
     * @param occupant color of the worker that occupies the tower, if none then empty
     * @return the tile as string
     */
    private static String createTile(int height, boolean hasDome, String occupant) { // WHITE, BROWN, BLUE -> da cambiare negli ANSI se cambiamo
        String tile = getLevelUpTo(height);
        if (!occupant.equals("")) {
            tile = addWorker(tile);
        } else if (hasDome) {
            tile = addDome(tile);
        }
        return colorLevels(tile, height, occupant);
    }

    /**
     * Adds a dome on the last floor of a tile
     *
     * @param oldTile tile on where we want to add the dome
     * @return tile with the dome
     */
    private static String addDome(String oldTile) {
        return addTowerElem(oldTile, 7, 12, "/   \\", "\\   /");
    }

    /**
     * Adds a worker on the last floor of a tile
     *
     * @param oldTile tile on where we want to add the worker
     * @return tile with the worker
     */
    private static String addWorker(String oldTile) {
        return addTowerElem(oldTile, 8, 11, "(W)", "\\_/");
    }

    /**
     * Adds a 2 lines high element on the last floor of a tower (worker or dome)
     *
     * @param oldTile    tile on where we want to add the element
     * @param pos1       starting position of line 4 (top-mid row) where the element will be added
     * @param pos2       starting position of line 5 (top-bottom row) where the element will be added
     * @param firstElem  first line of the element to be added
     * @param secondElem second line of the element to be added
     * @return the tile with the element added
     */
    private static String addTowerElem(String oldTile, int pos1, int pos2, String firstElem, String secondElem) {
        StringBuilder tile = new StringBuilder();
        List<String> tempRow = Arrays.asList(oldTile.split(SPLIT_VALUE));
        for (int r = 0; r < tempRow.size(); r++) {
            if (r == 4 || r == 5) {
                tile.append(tempRow.get(r), 0, pos1).append((r == 4) ? firstElem : secondElem).append(tempRow.get(r).substring(pos2)).append("\n");
            } else {
                tile.append(tempRow.get(r)).append("\n");
            }
        }
        return tile.toString();
    }

    /**
     * Colors a tile
     *
     * @param oldTile  tile that will be colored
     * @param height   height of the tile
     * @param occupant builder or dome at the top tower level as string, if none it's empty
     * @return colored tile as string
     */
    private static String colorLevels(String oldTile, int height, String occupant) {
        StringBuilder tile = new StringBuilder();
        List<String> temp = Arrays.asList(oldTile.split(SPLIT_VALUE));
        List<String> overBase = new ArrayList<>();
        for (int row = 1; row < temp.size() - 1; row++) {
            overBase.add(temp.get(row).substring(1, temp.get(row).length() - 1));
        }

        for (String string : overBase) {
            List<String> row = Arrays.asList(string.split(""));
            tile.append(insertColors(row, height, occupant));
        }
        return tile.toString();
    }

    /**
     * Creates the board basing on a DtoBoard
     *
     * @param session         DtoSession containing the board to print
     * @param colors          players and their colors
     * @param possibleActions map that contains which actions can be performed and where
     * @param areActionsShown boolean value that represents if the actions are showned
     * @return the board as string
     */
    private static String createBoard(DtoSession session, Map<String, Colors> colors, Map<Action, List<DtoPosition>> possibleActions, boolean areActionsShown) {
        StringBuilder board = new StringBuilder();
        Map<Integer, Boolean> positions;
        for (int y = 0; y < 5; y++) {
            List<String>[] line = createBoardRow(session, colors, y);
            positions = createBorder((areActionsShown) ? possibleActions : new HashMap<>(), y, board);
            addSideDivisors(positions, board, line);
        }
        createBorder((areActionsShown) ? possibleActions : new EnumMap<>(Action.class), 5, board);
        return board.toString();
    }

    /**
     * Adds the divisors between the tiles in the board row given; colors them if adjacent to a tile
     * candidate for the action selected
     *
     * @param positions map position to {@code true} if the position is candidate for the action
     * @param board     board being built as string
     * @param line      list containing the tiles to decorate with divisors
     */
    private static void addSideDivisors(Map<Integer, Boolean> positions, StringBuilder board, List<String>[] line) {
        for (int r = 0; r < line[0].size(); r++) {
            board.append(Boolean.TRUE.equals(positions.get(0)) ? Ansi.addBg(111, "|") : "|");
            for (int x = 0; x < 5; x++) {
                board.append(line[x].get(r)).append((positions.get(x) || positions.get(x + 1)) ? Ansi.addBg(111, "|") : "|");
            }
            board.append("\n");
        }
    }

    /**
     * Creates the specified row of the board
     *
     * @param session session containing the board
     * @param colors  map of each player to its color; needed to paint the workers
     * @param row     row of the board (y coordinate) to build
     * @return list containing the board tiles of the row specified
     */
    private static List<String>[] createBoardRow(DtoSession session, Map<String, Colors> colors, int row) {
        List<String>[] line = new List[5];
        for (int x = 0; x < 5; x++) {
            DtoTile tile = session.getBoard().getTile(x, row);
            String master = session.getWorkerMasterOn(x, row);
            line[x] = Arrays.asList(createTile(tile.getHeight(), tile.hasDome(), (master == null || colors.get(master) == null) ? "" : colors.get(master).toString()).split(SPLIT_VALUE));
        }
        return line;
    }

    /**
     * Colors the border of tiles in positions where the action can be done - only if 1 action is selected
     *
     * @param possibleActions list of possible actions
     * @param row             row where to append the color
     * @param b               temp builder for the scene
     * @return after appending the colored rows to the builder returns a map of that shows in ascended order on which positions the action can be performed
     */
    private static Map<Integer, Boolean> createBorder(Map<Action, List<DtoPosition>> possibleActions, int row, StringBuilder b) {
        Map<Integer, Boolean> candidates = new HashMap<>();
        Map<Integer, Boolean> candidatesNext;
        if (possibleActions != null && possibleActions.size() == 1 && possibleActions.keySet().iterator().next() != Action.ADD_WORKER) {
            List<DtoPosition> positions = possibleActions.values().iterator().next();
            candidates = getRowCandidatePositions(positions, row);
            candidatesNext = getRowCandidatePositions(positions, row - 1);
            b.append((candidates.get(0) || candidatesNext.get(0)) ? Ansi.addBg(111, "-") : "-");
            addInnerBorders(b, candidates, candidatesNext);
            b.append("\n");
        } else {
            b.append("-".repeat(17 * 5 + 6)).append("\n");
            for (int x = -1; x < 6; x++) {
                candidates.put(x, false);
            }
        }
        return candidates;
    }

    /**
     * Adds the borders between the board tiles. Colors the border added if adjacent to a position candidate for
     * the action selected
     *
     * @param b              StringBuilder where the borders will be added
     * @param candidates     map position index to {@code true} if the position is candidate for the action
     * @param candidatesNext same as candidates but for the following row
     */
    private static void addInnerBorders(StringBuilder b, Map<Integer, Boolean> candidates, Map<Integer, Boolean> candidatesNext) {
        for (int x = 0; x < 5; x++) {
            b.append((candidates.get(x) || (candidatesNext.get(x))) ? Ansi.addBg(111, "-".repeat(17)) : "-".repeat(17));
            b.append((candidates.get(x) || candidates.get(x + 1) ||
                    (candidatesNext.get(x) || candidatesNext.get(x + 1))) ? Ansi.addBg(111, "-") : "-");
        }
    }

    /**
     * Calculates in which tiles the action can be done, starting from a candidate positions list
     *
     * @param positions list of positions it's possible to choose
     * @param row       number of the row of tiles
     * @return map that shows in ascended order on which positions the action can be performed
     */
    private static Map<Integer, Boolean> getRowCandidatePositions(List<DtoPosition> positions, int row) {
        Map<Integer, Boolean> temp = new HashMap<>();
        for (int x = -1; x < 6; x++) {
            temp.put(x, false);
        }
        for (DtoPosition position : positions) {
            if (position.getY() == row) {
                temp.put(position.getX(), true);
            }
        }
        return temp;
    }

    /**
     * Prints the screen where the players can choose their gods
     *
     * @param message         message printed on the input box
     * @param choices         map of players with the god they chose
     * @param chosenGods      list of gods already selected
     * @param numberOfPlayers number of players in game
     * @param input           true if the input is enabled, false if not
     */
    public static void printPlayerGodSelection(String message, Map<String, String> choices, List<String> chosenGods, int numberOfPlayers, boolean input) {
        int outPutWidth = (numberOfPlayers == 3) ? 118 : 93;
        int godsSlotsWidth = 26;
        int godsSlotsHeight = 14;

        setMessage(message, input);
        StringBuilder b = new StringBuilder();
        b.append(createSelectedGodsRow(chosenGods, godsSlotsWidth, godsSlotsHeight, numberOfPlayers));
        b.append(createSelectedSlot(chosenGods, choices, godsSlotsWidth));
        b = closeSelectionWindow(b, numberOfPlayers, outPutWidth);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        if (input) {
            setCursor(9, 5);
        }
        out.flush();
    }

    /**
     * Creates the row that shows if a god selected was already chosen by a player
     *
     * @param chosenGods gods selected by the challenger
     * @param choices    map of players with the god they chose
     * @param length     length of the row
     * @return the row as string
     */
    private static String createSelectedSlot(List<String> chosenGods, Map<String, String> choices, int length) {
        StringBuilder temp = new StringBuilder();
        temp.append(" ".repeat((chosenGods.size() == 3) ? 1 : 0));
        boolean found = false;
        for (String god : chosenGods) {
            temp.append(" ".repeat(chosenGods.size() == 3 ? 2 : 4));
            for (Map.Entry<String,String> entry : choices.entrySet()) {
                String player = entry.getKey();
                if (choices.get(player).equals(god)) {
                    temp.append(extendSlots("Selected by " + player, length));
                    found = true;
                    break;
                }
            }
            if (!found) {
                temp.append(extendSlots("Not selected", length));
            }
            temp.append(" ".repeat(chosenGods.size() == 3 ? 2 : 4)).append("|");
            found = false;
        }
        return temp.toString().substring(0, temp.toString().length() - 1) + " ".repeat((chosenGods.size() == 3) ? 1 : 0);
    }

    /**
     * Glues together the boxes displaying the gods selected by the challenger and chosen by the players
     *
     * @param chosenGods      gods selected by the challenger
     * @param length          length of the box
     * @param height          height of the box
     * @param numberOfPlayers number of players in game
     * @return the box as string
     */
    private static String createSelectedGodsRow(List<String> chosenGods, int length, int height, int numberOfPlayers) {
        List<String> chosen = new ArrayList<>();
        for (String god : chosenGods) {
            chosen.add(createGodWindow(god, length, height));
        }
        return createRowOfGodsBoxes(chosen, height, numberOfPlayers);

    }

    /**
     * Creates a box displaying the gods selected by the challenger
     *
     * @param chosenGods      gods selected by the challenger
     * @param length          length of the box
     * @param height          height of the box
     * @param numberOfPlayers number of players in game
     * @return the box as string
     */
    private static String createPlayersChoiceBox(List<String> chosenGods, int length, int height, int numberOfPlayers) {
        List<String> chosen = new ArrayList<>();
        for (String god : chosenGods) {
            chosen.add(createGodWindow(god, length, height));
        }
        while (chosen.size() < numberOfPlayers) {
            chosen.add(createChoosingBox(length, height));
        }
        return createRowOfGodsBoxes(chosen, height, numberOfPlayers);
    }

    /**
     * Prints to screen that updates the not-challenger players of the gods selected by the challenger
     *
     * @param message         message printed on the input box
     * @param chosenGods      gods selected by the challenger
     * @param numberOfPlayers number of players in game
     */
    public static void printChallengerGodsUpdate(String message, List<String> chosenGods, int numberOfPlayers) {
        int outPutWidth = (numberOfPlayers == 3) ? 118 : 93;
        int godsSlotsWidth = 26;
        int godsSlotsHeight = 14;
        setMessage(message, false);
        StringBuilder b = new StringBuilder();
        b.append(createPlayersChoiceBox(chosenGods, godsSlotsWidth, godsSlotsHeight, numberOfPlayers));
        b = closeSelectionWindow(b, numberOfPlayers, outPutWidth);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        out.flush();
    }

    /**
     * Glues all the boxes of the challenger selection window into a scene
     *
     * @param temp            temporary builder of the scene
     * @param numberOfPlayers number of player in game
     * @param outPutWidth     width of the input/output area
     * @return builder with the scene built
     */
    private static StringBuilder closeSelectionWindow(StringBuilder temp, int numberOfPlayers, int outPutWidth) {
        StringBuilder b = new StringBuilder();
        temp = decorateColumns(temp);
        appendAllLinesCentered(b, temp.toString(), 6); //14
        b.insert(0, "\n" + TOP_SELECTION[numberOfPlayers - 2] + "\n");
        b.append(BOTTOM_SELECTION[numberOfPlayers - 2]).append("\n");
        temp.setLength(0);
        inputOutputSlot(temp, outPutWidth);
        temp = decorateSquare(temp, outPutWidth);
        appendAllLinesCentered(b, removeLines(temp.toString()), 1);
        return b;
    }

    /**
     * Creates a row of a god box
     *
     * @param chosen          list of the gods already chosen by the challeger
     * @param height          height of the box
     * @param numberOfPlayers number of the players in game
     * @return the box as string
     */
    private static String createRowOfGodsBoxes(List<String> chosen, int height, int numberOfPlayers) {
        StringBuilder temp = new StringBuilder();
        StringBuilder r = new StringBuilder();
        for (int row = 0; row < height - 1; row++) {
            r.append(" ".repeat((numberOfPlayers == 3) ? 1 : 0));
            for (String box : chosen) {
                r.append(" ".repeat((numberOfPlayers == 3) ? 2 : 4))
                        .append(box.split(SPLIT_VALUE)[row])
                        .append(" ".repeat((numberOfPlayers == 3) ? 2 : 4))
                        .append("|");
            }
            temp.append(r.toString(), 0, r.toString().length() - 1);
            r.setLength(0);
            temp.append(" ".repeat((numberOfPlayers == 3) ? 1 : 0)).append("\n");
        }
        return temp.toString();
    }

    /**
     * Creates the box that replaces a god box showing it's not selected
     *
     * @param length length of the box
     * @param height height of the box
     * @return the box as string
     */
    private static String createChoosingBox(int length, int height) {
        StringBuilder temp = new StringBuilder();
        temp.append(centerLine("The challenger is", length, false)).append("\n").append(centerLine("choosing ...", length, false)).append("\n");
        for (int row = 3; row < height; row++) {
            temp.append(getEmptyLength(length)).append("\n");
        }
        return temp.toString();
    }

    /**
     * Prints to screen the challenger scene
     *
     * @param message         message printed on the input box
     * @param chosenGods      list of gods already selected
     * @param page            number of the god page to show
     * @param numberOfPlayers number of players in game
     * @param input           true if the input is enabled, false if not
     */
    public static void printChallengerSelection(String message, List<String> chosenGods, int page, int numberOfPlayers, boolean input) {
        int outPutWidth = 116;
        int godsSlotsWidth = 28;
        int godsSlotsHeight = 10;

        setMessage(message, input);
        StringBuilder b = new StringBuilder();
        createGodsInfoBox(b, page, godsSlotsWidth, godsSlotsHeight);
        addBrowseArrows(b, chosenGods, numberOfPlayers);
        String temp = decorateColumns(b).toString();
        b.setLength(0);
        for (String line : temp.split(SPLIT_VALUE)) {
            b.append(centerLine(line, 6, 0)).append("\n");
        }
        b.insert(0, "\n" + TOP_CHALLENGER + "\n");
        b.append(BOTTOM_CHALLENGER).append("\n");
        StringBuilder outputSlot = new StringBuilder();
        inputOutputSlot(outputSlot, outPutWidth);
        outputSlot = decorateSquare(outputSlot, outPutWidth);
        appendAllLinesCentered(b, removeLines(outputSlot.toString()), 2);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        if (input) {
            setCursor(10, 5);
        }
        out.flush();
    }

    /**
     * Prints to screen the start scene
     *
     * @param message message printed on the input box
     * @param input   true if the input is enabled, false if not
     */
    public static void printStartScreen(String message, boolean input) {
        setMessage(message, input);
        StringBuilder b = new StringBuilder();
        int width = ROW.length();
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, extendSlots(TITLE), width, false);
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, CREDITS, width, false);
        appendEmptyRow(b, width, 1);
        inputOutputSlot(b, width);
        b = decorateSquare(b, 100);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        if (input) {
            setCursor(8, 5);
        }
        out.flush();
    }

    /**
     * Prints to screen the lobby scene
     *
     * @param message message printed on the input box
     * @param players map of player in lobby and their chosen colors
     * @param input   true if the input is enabled, false if not
     */
    public static void printLobbyScreen(String message, Map<String, Colors> players, boolean input) {
        setMessage(message, input);
        StringBuilder b = new StringBuilder();
        int width = getLongestLine(TOP_LOBBY);
        int innerWidth = 74;
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, extendSlots(TOP_LOBBY), width, false);
        appendAllLinesCentered(b, createPlayersBox(players, innerWidth), width, true);
        b.append(createColorsBox(players, innerWidth));
        appendAllLinesCentered(b, extendSlots(BOTTOM_LOBBY), width, false);
        StringBuilder temp = new StringBuilder();
        inputOutputSlot(temp, 96);
        temp = decorateSquare(temp, 96);
        appendAllLinesCentered(b, extendSlots(removeLines(temp.toString())), width + 2, false);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b, 42));
        if (input) {
            setCursor(10, 5);
        }
        out.flush();
    }

    /**
     * Creates the box that displays the available gods info
     *
     * @param b      temporary builder used to create the scene
     * @param page   number of the page to display
     * @param length length of the box
     * @param height height of the box
     */
    private static void createGodsInfoBox(StringBuilder b, int page, int length, int height) {
        List<String> godBoxes = getGodPage(page, length, height);
        addGodsRow(b, godBoxes, 0, length, height);
        b.append("-".repeat(2 + length + 2)).append("|").append("-".repeat(2 + length + 2)).append("|").append("\n");
        b.append(" ".repeat(2 + length + 2)).append("|").append(" ".repeat(2 + length + 2)).append("|").append("\n");
        addGodsRow(b, godBoxes, 2, length, height);
    }

    /**
     * Add browse arrow, selected gods and number of players to the builder (right column of gods selection screen)
     *
     * @param b               temporary builder used to create the scene
     * @param selectedGods    list of gods selected
     * @param numberOfPlayers number of players in game
     */
    private static void addBrowseArrows(StringBuilder b, List<String> selectedGods, int numberOfPlayers) {
        String[] arrow = ARROWS_CHALLENGER.split(SPLIT_VALUE);
        String[] godBox = b.toString().split(SPLIT_VALUE);
        b.setLength(0);
        b.append(godBox[0]).append("  You selected ").append(selectedGods.size()).append("/").append(numberOfPlayers).append(" gods     ").append("\n");
        for (int row = 1; row < 10; row++) {
            b.append(godBox[row]).append(extendSlots(arrow[row], 28)).append("\n");
        }
        for (int row = 10; row < 13; row++) {
            b.append(godBox[row]).append(getEmptyLength(28)).append("\n");
        }
        if (selectedGods.isEmpty()) {
            selectedGods.add("None yet");
        }
        b.append(godBox[13]).append(extendSlots("  Gods selected:", 28)).append("\n");
        b.append(godBox[14]).append(getEmptyLength(28)).append("\n");
        for (int row = 15; row < godBox.length; row++) {
            b.append(godBox[row]);
            b.append((row - 15 < selectedGods.size()) ? extendSlots("  " + selectedGods.get(row - 15), 28) : getEmptyLength(28));
            b.append("\n");
        }
    }

    /**
     * Append all the godBoxes to the builder
     *
     * @param b        temporary builder to which append the rows
     * @param godBoxes list of god boxes
     * @param block    number id of the block
     * @param length   length of the box
     * @param height   height of the box
     */
    private static void addGodsRow(StringBuilder b, List<String> godBoxes, int block, int length, int height) {
        for (int h = 0; h < height; h++) {
            b.append(" ".repeat(2));
            b.append(godBoxes.get(block).split(SPLIT_VALUE)[h]);
            b.append(" ".repeat(2)).append("|").append(" ".repeat(2));
            b.append(godBoxes.get(block + 1).split(SPLIT_VALUE)[h]);
            b.append(" ".repeat(2)).append("|").append("\n");
        }
        b.append(" ".repeat(2 + length + 2)).append("|").append(" ".repeat(2 + length + 2)).append("|").append("\n");
    }

    /**
     * Returns a set (page) of gods boxes
     *
     * @param page   number of the page to display
     * @param length length of the page
     * @param height height of the page
     * @return whole page with a constant number of gods display boxes
     */
    public static List<String> getGodPage(int page, int length, int height) {
        int subListSize = 4; // length
        List<String> gods = Gods.getGodsStringList();
        List<List<String>> temp = new ArrayList<>();
        for (int i = 0; i < gods.size(); i += subListSize) {
            temp.add(gods.subList(i, Math.min(i + subListSize, gods.size())));
        }
        gods = new ArrayList<>();
        List<String> pageGods = (page > -1) ? temp.get((page % (temp.size()))) : temp.get((temp.size() - ((-page) % temp.size())));
        for (String god : pageGods) {
            gods.add(createGodWindow(god, length, height));
        }
        while (gods.size() < subListSize) {
            gods.add(createEmptyWindow(length, height));
        }
        return gods;
    }

    /**
     * Creates a box that displays the god's info
     *
     * @param godName name of the god to display
     * @param length  length of the box
     * @param height  height of the box
     * @return the box as string
     */
    private static String createGodWindow(String godName, int length, int height) {
        StringBuilder temp = new StringBuilder();
        String[] description = Gods.valueOf(godName).getDescription().split(":");
        temp.append(extendSlots("Name: " + godName, length)).append("\n");
        appendEmptyRow(temp, length, 1);
        temp.append(extendSlots("Effect (" + description[0] + "):", length)).append("\n");
        appendEmptyRow(temp, length, 1);
        for (String string : divideInRows(description[1].substring(1), length)) {
            temp.append(extendSlots(string, length)).append("\n");
        }
        appendEmptyRow(temp, length, height - temp.toString().split(SPLIT_VALUE).length + 1);
        return temp.toString();
    }

    /**
     * Creates an empty box with the specified length and height
     *
     * @param length length of the box
     * @param height height of the box
     * @return the box as string
     */
    private static String createEmptyWindow(int length, int height) {
        return ("\n" + " ".repeat(length)).repeat(height).substring(1);
    }

    /**
     * Splits an info message in rows of length rowLength
     *
     * @param message   message to split
     * @param rowLength max length of each row
     * @return list containing the substrings found
     */
    private static List<String> divideInRows(String message, int rowLength) {
        List<String> temp = new ArrayList<>();
        StringBuilder row = new StringBuilder();
        for (String word : message.split(" ")) {
            if (row.toString().length() + word.length() > rowLength) {
                temp.add(row.toString());
                row.setLength(0);
            }
            row.append(word).append((row.toString().length() == rowLength) ? "" : " ");
        }
        temp.add(row.toString());
        return temp;
    }

    /**
     * Creates all the boxes containing the colors chosen by the players
     *
     * @param players map with the player names and colors chosen
     * @param width   width of the boxes
     * @return the boxes with colors chosen centered
     */
    private static String createColorsBox(Map<String, Colors> players, int width) {
        int maxColorLength = 5;
        StringBuilder b = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        temp.append(" ");
        int unusedColors = 0;
        for (Colors color : Colors.values()) {
            if (!players.containsValue(color)) {
                temp.append(Ansi.decorateColorString(color.toString(), color.toString())).append(" ");
                temp.insert(0, " ".repeat(maxColorLength - color.toString().length()));
                unusedColors += 1;
            }
        }
        int usedColors = Colors.values().length - unusedColors;
        b.append(centerLine(temp.toString(),
                (usedColors * 6 + (width - 18)) / 2 - 1,
                (usedColors * 6 + (width - 18) / 2) - ((usedColors == 0) ? 0 : 3 * usedColors)));
        b = decorateColumns(b);
        b.insert(0, " ".repeat(6));
        return b.toString();
    }

    /**
     * Creates all the boxes containing the names of the players
     *
     * @param players map with the player names and colors chosen
     * @param width   width of the boxes
     * @return the boxes with name centered
     */
    private static String createPlayersBox(Map<String, Colors> players, int width) {
        int nameMaxLength = 13; // max + 1
        int colorMaxLength = 5;
        StringBuilder b = new StringBuilder();
        if (!players.keySet().isEmpty()) {
            players.keySet().forEach(p -> b
                    .append("\n")
                    .append(" ".repeat(3))
                    .append(p)
                    .append(getEmptyLength(nameMaxLength - p.length() + (colorMaxLength - players.get(p).toString().length())))
                    .append(players.get(p).toString())
                    .append("\n"));
            b.append(" \n".repeat((3 - players.keySet().size()) * 2));
        } else {
            b.append("\n".repeat(2)).append(" ").append("No one registered yet . . .").append("\n ".repeat(3)).append("\n");
        }
        b.append(" \n").append(" ".repeat((players.isEmpty() ? 4 : 2))).append("- Available colors -");

        String box = b.toString();
        b.setLength(0);
        appendAllLinesCentered(b, extendSlots(box), width, false);

        return decorateColumns(b).toString();

    }

    /**
     * Colors the tile rows
     *
     * @param row      list of the rows to color
     * @param height   height of the tower on the tile
     * @param occupant builder or dome at the top tower level as string, if none it's empty
     * @return the rows colored and joined
     */
    private static String insertColors(List<String> row, int height, String occupant) {
        StringBuilder newRow = new StringBuilder();
        List<Integer> match = new ArrayList<>();
        for (int i = 0; i < row.size(); i++) {
            if (row.get(i).equals("|")) {
                match.add(i);
            }
        }
        if (height == 0) {
            addLastFloor(row, match, height, newRow, occupant);
        } else if (match.isEmpty()) {
            newRow.append(Ansi.addBg(244, String.join("", row)));
        } else {
            recursiveColorsAdd(row, match, height, newRow, 0, occupant);
        }
        newRow.append("\n");
        return newRow.toString();
    }

    /**
     * Colors all the tower floors up to before the last one using recursive calls
     *
     * @param row        rows to color
     * @param match      list of positions where the floors are split
     * @param height     height of the tower
     * @param newRow     temporary builder for the row
     * @param callNumber sequential number of the call - used to stop the recursion when the last floor is reached
     * @param occupant   builder or dome at the top level as string, if none it's empty
     */
    private static void recursiveColorsAdd(List<String> row, List<Integer> match, int height, StringBuilder newRow, int callNumber, String occupant) {
        if (callNumber > match.size() / 2 - 1) {
            if (callNumber - height == 0) {
                addLastFloor(row, match, height, newRow, occupant);
            } else {
                newRow.append(Ansi.addBg(244 + match.size() * 2, String.join("", row.subList(match.get(match.size() / 2 - 1) + 1, match.get(match.size() / 2)))));
            }
        } else if (callNumber == 0) {
            newRow.append(Ansi.addBg(244 + 4 * callNumber, String.join("", row.subList(0, match.get(callNumber) + 1))));
            recursiveColorsAdd(row, match, height, newRow, callNumber + 1, occupant);
            newRow.append(Ansi.addBg(244 + 4 * callNumber, String.join("", row.subList(match.get(match.size() - 1 - callNumber), row.size()))));
        } else {
            newRow.append(Ansi.addBg(244 + 4 * callNumber, String.join("", row.subList(match.get(callNumber - 1) + 1, match.get(callNumber) + 1))));
            recursiveColorsAdd(row, match, height, newRow, callNumber + 1, occupant);
            newRow.append(Ansi.addBg(244 + 4 * callNumber, String.join("", row.subList(match.get(match.size() - 1 - callNumber), match.get(match.size() - callNumber)))));
        }
    }

    /**
     * Adds the last floor of a tower to its builder
     *
     * @param row      rows that contain the substrings to color
     * @param match    list of positions where the different floors start or end
     * @param height   height of the tower
     * @param newRow   temporary builder creating the floor
     * @param occupant builder or dome at the top level as string, if none it's empty
     */
    private static void addLastFloor(List<String> row, List<Integer> match, int height, StringBuilder newRow, String occupant) {
        String nextRow = (height == 0) ? String.join("", row) : String.join("", row.subList(match.get(match.size() / 2 - 1) + 1, match.get(match.size() / 2)));
        if (nextRow.contains("/   \\") || nextRow.contains("\\   /")) {
            colorLastFloorElem(newRow, nextRow, 6, 11, 19, height);
        } else if (nextRow.contains("(W)") || nextRow.contains("\\_/")) {
            colorLastFloorElem(newRow, nextRow, 7, 10, Ansi.colorToInt(occupant), height);
        } else {
            newRow.append(Ansi.addBg((height == 0) ? 112 : 240 + height * 4, nextRow));
        }
    }

    /**
     * Adds colors ANSI to last floor of a tower (may be 1,2,3)
     *
     * @param b      builder of the tower
     * @param row    row that contains the substring to color
     * @param start  start of the substring to color
     * @param end    end of the substring to color
     * @param color  color to apply
     * @param height height of the floor
     */
    private static void colorLastFloorElem(StringBuilder b, String row, int start, int end, int color, int height) {
        b.append(Ansi.addBg((height == 0) ? 112 : 240 + height * 4, row.substring(0, start - height * 2)))
                .append(Ansi.addBg(color, row.substring(start - height * 2, end - height * 2)))
                .append(Ansi.addBg((height == 0) ? 112 : 240 + height * 4, row.substring(end - height * 2)));

    }

    /**
     * Creates a whole tower up to height
     *
     * @param height height of the tower
     * @return the whole tower up to height as string
     */
    private static String getLevelUpTo(int height) {
        StringBuilder tile = new StringBuilder();
        tile.append(getLevel(0));
        int yPos = 1;
        int xPos = 1;
        for (int h = 1; h < height + 1; h++) {
            String[] oldTile = tile.toString().split(SPLIT_VALUE);
            tile.setLength(0);
            for (int r = 0; r < yPos; r++) {
                tile.append(oldTile[r]).append("\n");
            }
            String[] newLevel = getLevel(h).split(SPLIT_VALUE);
            for (int n = 0; n < newLevel.length; n++) {
                tile.append(oldTile[n + yPos], 0, xPos)
                        .append(newLevel[n])
                        .append(oldTile[n + yPos], xPos + newLevel[n].length(), oldTile[n + yPos].length()).append("\n");
            }
            for (int r = yPos + newLevel.length; r < oldTile.length; r++) {
                tile.append(oldTile[r]).append("\n");
            }
            xPos += 2;
            yPos++;
        }
        return tile.toString();
    }

    /**
     * Creates the tower level at height
     *
     * @param height height of the tower
     * @return tile tower's level at height
     */
    private static String getLevel(int height) {
        StringBuilder temp = new StringBuilder();
        temp.append(addTileLine(" .", ". ", "-", height, 2)).append("\n");
        for (int r = 0; r < levelheight[height]; r++) {
            temp.append(addTileLine(height)).append("\n");
        }
        temp.append(addTileLine(" '", "' ", "-", height, 2)).append("\n");
        return temp.toString();
    }

    /**
     * Creates a Tile line based on height
     *
     * @param leftBorder  left border length
     * @param rightBorder right border length
     * @param inner       inner space length
     * @param height      whole tile height
     * @param offset      offset value from left border
     * @return line of the tile
     */
    private static String addTileLine(String leftBorder, String rightBorder, String inner, int height, int offset) {
        return " ".repeat((height > 0) ? 1 : 0) +
                leftBorder +
                inner.repeat(levellength[height] - offset) +
                ((height > 0 && rightBorder.equals(". ")) ? "." + height : rightBorder) +
                " ".repeat((height > 0) ? 1 : 0);
    }

    /**
     * Creates a Tile line based on height with offset from left border of 0
     *
     * @param height      whole tile height
     * @return line of the tile with offset 0
     */
    private static String addTileLine(int height) {
        return addTileLine("|", "|", " ", height, 0);
    }
}
