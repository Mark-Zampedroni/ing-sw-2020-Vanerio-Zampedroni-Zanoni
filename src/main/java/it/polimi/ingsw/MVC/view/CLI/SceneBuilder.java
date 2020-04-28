package it.polimi.ingsw.MVC.view.CLI;

public class SceneBuilder {

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



    //public static void setScreenLength()

    // Stampa la schermata iniziale
    public static void printStartScreen(String inputMessage) {
        builder.setLength(0);
        int width = getLongestLine(TITLE);
        builder.append(TITLE+"\n\n");
        appendAllLinesCentered(CREDITS,width);
        appendEmptyLine();
        appendAllLinesCentered(inputMessage,width);
        System.out.println(Ansi.CLEAR_CONSOLE+builder);
        System.out.print(Ansi.moveCursorE(10));
    }

    private static void appendEmptyLine() {
        builder.append("\n");
    }

    private static void appendAllLinesCentered(String text, int length) {
        for(String line : text.split("\\r?\\n")) {
            String temp = centerLine(line,length)+"\n";
            builder.append(temp);
        }
    }

    // Centra una stringa sapendo che lo schermo è lungo "length" caratteri
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
