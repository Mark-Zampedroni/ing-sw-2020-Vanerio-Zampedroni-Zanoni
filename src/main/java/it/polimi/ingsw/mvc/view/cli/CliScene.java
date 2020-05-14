package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.enumerations.Colors;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Map;


 /// Uso String.repeat(int) !


public class CliScene {


    private static PrintStream out = new PrintStream(System.out, true);

    private static final int SCREEN_WIDTH = 192;

    private static int cursorOffset;
    private static boolean enableInput;
    private static String windowMessage;


    public static double getScreenCharSize() {
        return SCREEN_WIDTH;
    }

    public static void setMessage(String message, boolean input) {
        windowMessage = message;
        enableInput = input;
    }

    public static void inputOutputSlot(StringBuilder b, int width) {
        b.append(ROW).append("\n");
        appendEmptyRow(b, width, 1);
        appendAllLinesCentered(b, windowMessage, width);
        appendEmptyRow(b, width, 1);
        if(enableInput) {
            b.append(fixSlots("   >>> ", width)).append("\n");
            appendEmptyRow(b, width, 1);
        }
    }

    public static void setCursor() {
        if(enableInput) {
            out.print(Ansi.moveCursorE(cursorOffset+8));
            out.print(Ansi.moveCursorN(5));
        }
    }

    public static void printStartScreen(String message, boolean input) {
        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        int width = ROW.length();
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, fixSlots(TITLE), width);
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, CREDITS, width);
        appendEmptyRow(b, width, 1);
        inputOutputSlot(b, width);
        b = decorateSquare(b);
        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b));
        setCursor();
        out.flush();
    }

    public static void printLobbyScreen(String inputMessage, Map<String, Colors> players) {
        int defaultLength = 10;
        int defaultSpace = 5;
        final StringBuilder b = new StringBuilder();
        appendEmptyLine(b,2);
        b.append("Players in Lobby:\n");
        if(!players.keySet().isEmpty()) {
            appendEmptyLine(b,2);
            b.append("Name:").append(getEmptyLength(defaultSpace)).append("Color:\n");
            appendEmptyLine(b);
            players.keySet().forEach(p -> b
                    .append(p)
                    .append(getEmptyLength(defaultLength-p.length()))
                    .append(Ansi.decorateColorString(players.get(p).toString(),players.get(p).toString()))
                    .append("\n"));
        }
        else {
            appendEmptyLine(b,2);
            b.append("No one registered yet");
        }
        appendEmptyLine(b,2);
        b.append("Available colors: ");
        Arrays.stream(Colors.values()).filter(c -> !players.containsValue(c)).map(Enum::toString).forEach(c -> b
                .append(Ansi.decorateColorString(c,c))
                .append(" "));
        b.append("\n").append(inputMessage).append("\n");
        System.out.println(centerScreen(Ansi.CLEAR_CONSOLE+b));
    }

    // Dato uno scenario in testo lo centra nello schermo del terminale // (non so se funzioni per tutti)
    private static StringBuilder centerScreen(String text) {
        StringBuilder temp = new StringBuilder();
        String offset = getEmptyLength((int) ((getScreenCharSize()-getLongestLine(text))/2));
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

    private static String fixSlots(String string, int length) {
        return string+" ".repeat(Math.max(0, length - string.length()));
    }


    // Aggiunge una riga vuota
    private static void appendEmptyLine(StringBuilder b) {
        b.append("\n");
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

    /*private static int getLongestWord(List<String> words) {
        return words.stream().map(String::length).max(Integer::compareTo).get();
    }*/

    // Centra ogni riga a capo su un pezzo di lunghezza length (se non ci sta non funziona)
    private static void appendAllLinesCentered(StringBuilder b, String text, int length) {
        for(String line : text.split("\\r?\\n")) {
            String temp = centerLine(line,length)+"\n";
            b.append(temp);
        }
    }

    // Centra una stringa in un pezzo di lunghezza length (se non ci sta non funziona)
    private static String centerLine(String text, int length) {
        StringBuilder temp = new StringBuilder();
        temp.append(text);
        for(int i = (length - text.length())/2; i>0; i--) {
            temp.insert(0," ");
            temp.append(" ");
        }
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


    private static StringBuilder decorateSquare(StringBuilder b) {
        StringBuilder temp = new StringBuilder();
        appendEmptyLine(temp,3);
        temp.append(TOP_ROW);
        for (String string : b.toString().split("\\r?\\n")) {
            temp.append("|").append(string).append("|");
            temp.append("\n");
        }
        temp.append(BOTTOM_ROW);
        return temp;
    }


    //100
    //public static final String ROW = "──────────────────────────────────────────────────────────────────────────────────────────────────";
    public static final String ROW = "-".repeat(100);
    public static final String TOP_ROW = "."+ROW+".\n";
    public static final String BOTTOM_ROW = "'"+ROW+"'\n";

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



    public static final String LOBBY = "                                         __--_-_--__                                                  \n" +
            "                                     __--__--   --__--__                                                   \n" +
            "                                 __--__--           --__--__\n" +
            "                             __--__--                   --__--__                                                                \n" +
            "                         __--__--                           --__--__                                                          \n" +
            "                     __--__--                                   --__--__                                                                                                                                         \n" +
            "                 __--__--              LOBBY IN ASCII               --__--__                                          \n" +
            "             __--__--                                                   --__--__                                  \n" +
            "         __--__--                                                           --__--__                          \n" +
            "     __--__--                                                                   --__--__              \n" +
            " __--__--___________________________________________________________________________--__--__\n" +
            " ═══════════════════════════════════════════════════════════════════════════════════════════\n" +
            "  | | |                                                                               | | |\n" +
            "  | | |                                                                               | | |  \n" +
            "  |═══════════════════════════════════════════════════════════════════════════════════════|\n" +
            "  |                                                                                       |\n" +
            "  |'____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'''''____'|\n" +
            "   / _  \\═══/  _ \\                                                         / _  \\═══/  _ \\   \n" +
            "  ( (. \\     / .) )                                                       ( (. \\     / .) ) \n" +
            "   \\___/═════\\___/                                                         \\___/═════\\___/            \n" +
            "       | | | |                                                                 | | | |               \n" +
            "       | | | |                       REGISTERED PLAYERS                        | | | |       \n" +
            "       | | | |                                                                 | | | |      \n" +
            "       | | | |                      GIOCATORE 1 - COLORE                       | | | |         \n" +
            "       | | | |                                                                 | | | |       \n" +
            "       | | | |                      GIOCATORE 2 - COLORE                       | | | |        \n" +
            "       | | | |                                                                 | | | |         \n" +
            "       | | | |                                                                 | | | |       \n" +
            "       | | | |                                                                 | | | |          \n" +
            "       | | | |                                                                 | | | |      \n" +
            "       | | | |                                                                 | | | |          \n" +
            "       | | | |                                                                 | | | |       \n" +
            "       | | | |                                                                 | | | |           \n" +
            "       | | | |                                                                 | | | |         \n" +
            "      (0O0o0o0)                                                               (0O0o0o0)                   \n" +
            "      #########                                                               #########            \n" +
            "    zzzzzzzzzzzzz                                                           zzzzzzzzzzzzz     \n" +
            "  ══════════════════════════════════════════════════════════════════════════════════════════\n" +
            " _|________________________________________________________________________________________|_   ";
}
