package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.enumerations.Colors;

import java.io.PrintStream;
import java.util.*;

public class CliScene {


    private static PrintStream out = new PrintStream(System.out, true);

    private static final int SCREEN_WIDTH = 192;

    private static int cursorOffset;
    private static boolean enableInput;
    private static String windowMessage = " Empty Message ...";


    public static double getScreenCharSize() {
        return SCREEN_WIDTH;
    }

    public static void setMessage(String message, boolean input) {
        windowMessage = message;
        enableInput = input;
    }

    public static void inputOutputSlot(StringBuilder b, int width) {
        b.append("-".repeat(width)).append("\n");
        appendEmptyRow(b, width, 1);
        appendAllLinesCentered(b, windowMessage, width, false);
        appendEmptyRow(b, width, 1);
        if(enableInput) {
            b.append(fixSlots("   >>> ", width)).append("\n");
            appendEmptyRow(b, width, 1);
        }
    }

    public static void setCursor(int x, int y) {
        if(enableInput) {
            out.print(Ansi.moveCursorE(cursorOffset+x));
            out.print(Ansi.moveCursorN(y));
        }
    }

    public static void printStartScreen(String message, boolean input) {
        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        int width = ROW.length();
        appendEmptyRow(b, width, 2);
        //Adds title
        appendAllLinesCentered(b, fixSlots(TITLE), width,false);
        appendEmptyRow(b, width, 2);
        //Adds credits
        appendAllLinesCentered(b, CREDITS, width,false);
        appendEmptyRow(b, width, 1);
        //Adds input box
        inputOutputSlot(b, width);
        //Creates square
        b = decorateSquare(b,100);

        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        setCursor(8,5);
        out.flush();
    }

    public static void printLobbyScreen(String message, Map<String, Colors> players, boolean input) {
        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        int width = getLongestLine(TOP_LOBBY);
        int innerWidth = 74;
        appendEmptyRow(b, width, 2);
        //Adds top fixed draw
        appendAllLinesCentered(b, fixSlots(TOP_LOBBY), width, false);
        //Adds registered players box
        appendAllLinesCentered(b, createPlayersBox(players, innerWidth), width, true);
        //Adds available colors box
        b.append(createColorsBox(players,innerWidth));
        //Adds bottom fixed draw
        appendAllLinesCentered(b, fixSlots(BOTTOM_LOBBY), width, false);
        //Adds input box
        StringBuilder temp = new StringBuilder();
        inputOutputSlot(temp, 96);
        temp = decorateSquare(temp,96);
        appendAllLinesCentered(b,fixSlots(removeLines(temp.toString(),5)),width+2,false);

        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b, 42));
        setCursor(10,5);
        out.flush();
    }

    private static String createColorsBox(Map<String, Colors> players, int width) {
        int maxColorLength = 5;
        StringBuilder b = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        temp.append(" ");
        int unusedColors = 0;
        for(Colors color : Colors.values()) {
            if(!players.containsValue(color)) {
                temp.append(Ansi.decorateColorString(color.toString(),color.toString())).append(" ");
                temp.insert(0," ".repeat(maxColorLength-color.toString().length()));
                unusedColors += 1;
            }
        }
        int usedColors = Colors.values().length-unusedColors;
        b.append(centerLine(temp.toString(),
                (usedColors*6+(width-18))/2 - 1,
                (usedColors*6+(width-18)/2) - ((usedColors == 0) ? 0 : 3*usedColors)));
        b = decorateColumns(b);
        b.insert(0," ".repeat(6));
        return b.toString();
    }

    private static String createPlayersBox(Map<String, Colors> players, int width) {
        int nameMaxLength = 13; // max + 1
        int colorMaxLength = 5;
        StringBuilder b = new StringBuilder();
        if(!players.keySet().isEmpty()) {
            players.keySet().forEach(p -> b
                    .append("\n")
                    .append(p)
                    .append(getEmptyLength(nameMaxLength-p.length()+(colorMaxLength-players.get(p).toString().length())))
                    .append(Ansi.decorateColorString(players.get(p).toString(),players.get(p).toString()))
                    .append("\n"));
            b.append(" \n".repeat((3-players.keySet().size())*2));
        }
        else { b.append("\n".repeat(2)).append("No one registered yet . . .").append("\n ".repeat(3)).append("\n"); }
        b.append(" \n").append(" ".repeat(4)).append("- Available colors -");

        String box = b.toString();
        b.setLength(0);
        appendAllLinesCentered(b,fixSlots(box),width, true);

        return decorateColumns(b).toString();

    }

    // Dato uno scenario in testo lo centra nello schermo del terminale // (non so se funzioni per tutti)
    private static StringBuilder centerScreen(String text) {
        return addOffset(text,(getScreenCharSize()-getLongestLine(text))/2);
    }

    private static StringBuilder centerScreen(String text, int width) {
        return addOffset(text,width);
    }

    private static StringBuilder addOffset(String text, double o) {
        StringBuilder temp = new StringBuilder();
        String offset = " ".repeat((int)o);
        for(String line : text.split("\\r?\\n")) {
            String newLine = offset + line + "\n";
            temp.append(newLine);
        }
        cursorOffset = offset.length(); // <---------
        return temp;
    }

    // Restituisce una con "length" spazi vuoti
    private static String getEmptyLength(int length) {
        return " ".repeat(Math.max(0, length));
    }

    private static String removeLines(String text, int number) {
        StringBuilder b = new StringBuilder();
        int i = 1;
        for(String string : text.split("\\r?\\n")) {
            if(i > number) {
                b.append(string).append("\n");
            }
            i++;
        }
        return b.toString();
    }

    private static String fixSlots(String string) {
        StringBuilder temp = new StringBuilder();
        int width = getLongestLine(string);
        for(String s : string.split("\\r?\\n")) {
            temp.append(s);
            temp.append(" ".repeat(Math.max(0, width - s.length() + 1)));
            temp.append("\n");
        }
        return temp.toString();
    }

    private static String fixSlots(String text, int length) {
        return text+" ".repeat(Math.max(0, length - text.length()));
    }

    private static String fixAnsi(String text) {
        // if there are ansi
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        int squareBrackets = text.length() - text.replaceAll("\\[","").length();
        if(squareBrackets > 1) {
            temp.append(" ".repeat(2*squareBrackets));
            temp.insert(0, " ".repeat(2*squareBrackets+1));
        }
        return temp.toString();
    }

    // Aggiunge un numero "number" di righe vuote
    private static void appendEmptyLine(StringBuilder b, int number) {
        b.append("\n".repeat(Math.max(0, number)));
    }

    private static void appendEmptyRow(StringBuilder b, int width, int times) {
        for(int x = 0; x<times; x++) {
            b.append(getEmptyLength(width)).append("\n");
        }
    }

    // Centra ogni riga a capo su un pezzo di lunghezza length (se non ci sta non funziona)
    private static void appendAllLinesCentered(StringBuilder b, String text, int length, boolean fixAnsi) {
        for(String line : text.split("\\r?\\n")) {
            b.append(centerLine(line,length,fixAnsi)).append("\n");
        }
    }

    // Centra una stringa in un pezzo di lunghezza length (se non ci sta non funziona)
    private static String centerLine(String text, int length, boolean fixAnsi) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        for(int i = (length - text.length())/2; i>0; i--) {
            temp.insert(0," ");
            temp.append(" ");
        }
        return (fixAnsi) ? fixAnsi(temp.toString()) : temp.toString();
    }

    private static String centerLine(String text, int left, int right) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        temp.insert(0," ".repeat(left));
        temp.append(" ".repeat(right));
        return temp.toString();
    }

    // Restituisce la lunghezza maggiore tra tutte le linee in "text" (divise da \n)
    private static int getLongestLine(String text) {
        int max = 0;
        for(String line : text.split("\\r?\\n")) {
            if(line.length() > max) { max = line.length(); }
        }
        return max;
    }


    private static StringBuilder decorateSquare(StringBuilder b, int width) {
        StringBuilder temp = new StringBuilder();
        appendEmptyLine(temp,3);
        temp.append(".").append("-".repeat(width)).append(".\n");
        for (String string : b.toString().split("\\r?\\n")) {
            temp.append("|").append(string).append("|");
            temp.append("\n");
        }
        temp.append("'").append("-".repeat(width)).append("'\n");
        return temp;
    }

    private static StringBuilder decorateColumns(StringBuilder b) {
        StringBuilder temp = new StringBuilder();
        for (String string : b.toString().split("\\r?\\n")) {
            temp.append(" | | | |").append(string).append("| | | |");
            temp.append("\n");
        }
        return temp;
    }


    //100
    //public static final String ROW = "──────────────────────────────────────────────────────────────────────────────────────────────────";
    public static final String ROW = "-".repeat(100);

    public static final String TOP_LOBBY = "    ______________________________________________________________________________________________   \n" +
            "   /                                                                                              \\  \n" +
            "  |________________________________________________________________________________________________| \n" +
            "  |                                                                                                | \n" +
            "  |'____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'| \n" +
            "   / _  \\---/  _ \\                                                                  / _  \\---/  _ \\  \n" +
            "  ( (. \\     / .) )                                                                ( (. \\     / .) ) \n" +
            "   \\___/-----\\___/                                                                  \\___/-----\\___/  \n" +
            "       | | | |                            REGISTERED PLAYERS                            | | | |      ";

    private static final String BOTTOM_LOBBY = "       | | | |                                                                          | | | |      \n" +
            "      (0OUwUo0)                                                                        (0OUwUo0)     \n" +
            "      #########                                                                        #########     \n" +
            "   .zzzzzzzzzzzzz--------------------------------------------------------------------zzzzzzzzzzzzz.  \n" +
            "  /________________________________________________________________________________________________\\ ";

    public static final String TITLE = "      .---.                                          ,___ \n" +
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

    public static final String CREDITS = "Game developed by Stefano Vanerio, Mark Zampedroni and Marco Zanoni \n"+
                                          "Original board version published by Roxley Games";

}
