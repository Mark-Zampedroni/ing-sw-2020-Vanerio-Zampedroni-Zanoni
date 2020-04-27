package it.polimi.ingsw.MVC.view.CLI;

public class SceneBuilder {

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


    public static final String SANTORINI_TITLE = "      .---.                                          ,___ \n" +
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



}
