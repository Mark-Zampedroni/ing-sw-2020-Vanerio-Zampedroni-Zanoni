package it.polimi.ingsw.MVC.view.CLI;

import it.polimi.ingsw.utility.enumerations.Colors;

import java.util.List;
import java.util.Map;

public class SceneBuilder {

    private static final int SCREEN_WIDTH = 205;
    private static int cursorOffset;

    private static StringBuilder builder = new StringBuilder();

    private static String outputRequest = "";
    private static String scenario = "";

    public static void putOutputRequest(String text) {
        outputRequest = text;
    }
    public static void addToScenario(String text) { scenario += text; }
    public static void clearScenario() { scenario = ""; }

    private static String buildScene() {
         return Ansi.CLEAR_CONSOLE + "\n--------------------------------\n" +
                scenario +
                outputRequest + "\n--------------------------------";
    }

    public static void printScene() {
        System.out.println(buildScene());
    }


    public static double getScreenCharSize() {
        return SCREEN_WIDTH;
    }

    // Dato uno scenario in testo lo centra nello schermo del terminale // (non so se funzioni per tutti)
    private static StringBuilder centerScreen(String text) {
        StringBuilder temp = new StringBuilder();
        String offset = getEmptyLength((int) ((getScreenCharSize()-getLongestLine(text))/2));
        for(String line : text.split("\\r?\\n")) {
            String newLine = offset + line + "\n";
            temp.append(newLine);
        }
        cursorOffset = offset.length();
        return temp;
    }

    // Restituisce una con "length" spazi vuoti
    private static String getEmptyLength(int length) {
        StringBuilder temp = new StringBuilder();
        for(int x = 0; x < length; x++) { temp.append(" "); }
        return temp.toString();
    }

    // Stampa la schermata iniziale
    public static void printStartScreen(String inputMessage) {
        builder.setLength(0);
        int width = getLongestLine(TITLE);
        appendEmptyLine(2);
        builder.append(TITLE);
        appendEmptyLine(3);
        appendAllLinesCentered(CREDITS,width);
        appendEmptyLine();
        appendAllLinesCentered(inputMessage, width);
        builder = centerScreen(Ansi.CLEAR_CONSOLE+builder);
        System.out.println(builder);
        System.out.print(Ansi.moveCursorE(cursorOffset));
    }

    public static void printLobbyScreen(String inputMessage, Map<String, Colors> players) {
        int defaultLength = 10;
        int defaultSpace = 5;
        builder.setLength(0);
        appendEmptyLine(2);
        builder.append("Players in Lobby:\n");
        if(!players.keySet().isEmpty()) {
            appendEmptyLine(2);
            builder.append("Name:"+getEmptyLength(defaultSpace)+"Color:\n");
            appendEmptyLine();
            players.keySet().forEach(p -> builder.append(p + (getEmptyLength(defaultLength-p.length())) + Ansi.decorateColorString(players.get(p).toString(),players.get(p).toString()) + "\n"));
        }
        else {
            appendEmptyLine(2);
            builder.append("No one registered yet");
        }
        appendEmptyLine(2);
        builder.append(inputMessage+"\n");
        builder = centerScreen(Ansi.CLEAR_CONSOLE+builder);
        System.out.println(builder);
    }

    // Aggiunge una riga vuota
    private static void appendEmptyLine() {
        builder.append("\n");
    }

    // Aggiunge un numero "number" di righe vuote
    private static void appendEmptyLine(int number) {
        for(int x = 0; x < number; x++) { builder.append("\n"); }
    }

    /*private static int getLongestWord(List<String> words) {
        return words.stream().map(String::length).max(Integer::compareTo).get();
    }*/

    // Centra ogni riga a capo su un pezzo di lunghezza length (se non ci sta non funziona)
    private static void appendAllLinesCentered(String text, int length) {
        for(String line : text.split("\\r?\\n")) {
            String temp = centerLine(line,length)+"\n";
            builder.append(temp);
        }
    }

    // Centra una stringa in un pezzo di lunghezza length (se non ci sta non funziona)
    private static String centerLine(String text, int length) {
        StringBuilder temp = new StringBuilder();
        for(int i = (length - text.length())/2; i>0; i--) {
            temp.insert(0," ");
        }
        temp.append(text);
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


    public static String board =  "╔═════════════════════════════════════╤════════════════╗\n" +
            "║                                     │                ║\n" +
            "║   ┌─────┬─────┬─────┬─────┬─────┐   │  # is Mark     ║\n" +
            "║   │  #  │     │     │     │     │   │  @ is Glu      ║\n" +
            "║   │  0  │  1  │  0  │  0  │  0  │   │                ║\n" +
            "║   ├─────┼─────┼─────┼─────┼─────┤   │  X is a dome   ║\n" +
            "║   │     │     │  @  │     │  @  │   │                ║\n" +
            "║   │  0  │  2  │  0  │  0  │  0  │   │                ║\n" +
            "║   ├─────┼─────┼─────┼─────┼─────┤   │                ║\n" +
            "║   │  X  │     │     │     │     │   │                ║\n" +
            "║   │  3  │  1  │  0  │  1  │  0  │   │                ║\n" +
            "║   ├─────┼─────┼─────┼─────┼─────┤   │                ║\n" +
            "║   │     │     │  #  │     │     │   │                ║\n" +
            "║   │  0  │  0  │  1  │  2  │  0  │   │                ║\n" +
            "║   ├─────┼─────┼─────┼─────┼─────┤   │                ║\n" +
            "║   │     │     │     │     │     │   │                ║\n" +
            "║   │  0  │  0  │  0  │  0  │  0  │   │                ║\n" +
            "║   └─────┴─────┴─────┴─────┴─────┘   │                ║\n" +
            "║                                     │                ║\n" +
            "╚═════════════════════════════════════╧════════════════╝\n" +
            " Waiting for input ...                                  \n" +
            "                                                        \n" +
            " [Input line] (23)                                        ";


    private static final String TITLE = "      .---.                                          ,___ \n" +
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

    private static final String CREDITS = "Game developed by Stefano vanerio, Mark Zampedroni and Marco Zanoni\n"+
                                          "Original board version published by Roxley Games";


}
