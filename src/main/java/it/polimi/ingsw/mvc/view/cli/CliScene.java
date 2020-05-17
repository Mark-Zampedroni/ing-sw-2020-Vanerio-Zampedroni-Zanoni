package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;

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
            out.print(Ansi.moveCursorE(cursorOffset+x)); // Da togliere cursorOffset, a volte da problemi
            out.print(Ansi.moveCursorN(y));
        }
    }


    public static void printPlayerGodSelection(String message, Map<String,String> choices, List<String> chosenGods, int numberOfPlayers, boolean input) {

        int outPutWidth = (numberOfPlayers == 3) ? 118 : 93;
        int godsSlotsWidth = 26;
        int godsSlotsHeight = 14;
        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        b.append(createSelectedGodsRow(chosenGods, choices, godsSlotsWidth, godsSlotsHeight, numberOfPlayers));
        b.append(createSelectedSlot(chosenGods,choices,godsSlotsWidth));
        b = closeSelectionWindow(b,numberOfPlayers,outPutWidth);
        out.println(centerScreen( Ansi.CLEAR_CONSOLE + b));
    }

    private static String createSelectedSlot(List<String> chosenGods, Map<String,String> choices, int length) {
        StringBuilder temp = new StringBuilder();
        temp.append(" ".repeat((chosenGods.size() == 3) ? 1 : 0));
        boolean found = false;
        for(String god : chosenGods) {
            temp.append(" ".repeat(chosenGods.size() == 3 ? 2 : 4));
            for(String player : choices.keySet()) {
                if(choices.get(player).equals(god)) {
                    temp.append(fixSlots("Selected by "+player,length));
                    found = true;
                    break;
                }
            }
            if(!found) { temp.append(fixSlots("Not selected",length)); }
            temp.append(" ".repeat(chosenGods.size() == 3 ? 2 : 4)).append("|");
            found = false;
        }
        return temp.toString().substring(0,temp.toString().length()-1)+" ".repeat((chosenGods.size() == 3) ? 1 : 0);
    }

    private static String createSelectedGodsRow(List<String> chosenGods, Map<String,String> choices, int length, int height, int numberOfPlayers) {
        List<String> chosen = new ArrayList<>();
        for(String god : chosenGods) {
            chosen.add(createGodWindow(god,length,height));
        }
        return createRowOfGodsBoxes(chosen,height,numberOfPlayers);

    }

    public static String createPlayersChoiceBox(List<String> chosenGods, int length, int height, int numberOfPlayers) {
        List<String> chosen = new ArrayList<>();
        for(String god : chosenGods) {
            chosen.add(createGodWindow(god,length,height));
        }
        while(chosen.size() < numberOfPlayers) {
            chosen.add(createChoosingBox(length,height));
        }
        return createRowOfGodsBoxes(chosen,height,numberOfPlayers);
    }


    public static void printChallengerGodsUpdate(String message, List<String> chosenGods, int numberOfPlayers) {
        int outPutWidth = (numberOfPlayers == 3) ? 118 : 93;
        int godsSlotsWidth = 26;
        int godsSlotsHeight = 14;
        setMessage(message,false);
        StringBuilder b = new StringBuilder();
        b.append(createPlayersChoiceBox(chosenGods, godsSlotsWidth, godsSlotsHeight, numberOfPlayers));
        b = closeSelectionWindow(b,numberOfPlayers,outPutWidth);
        out.println(centerScreen( Ansi.CLEAR_CONSOLE + b));
    }

    public static StringBuilder closeSelectionWindow(StringBuilder temp, int numberOfPlayers, int outPutWidth) {
        StringBuilder b = new StringBuilder();
        temp = decorateColumns(temp);
        appendAllLinesCentered(b,temp.toString(),6,0); //14
        b.insert(0,"\n"+TOP_SELECTION[numberOfPlayers-2]+"\n");
        b.append(BOTTOM_SELECTION[numberOfPlayers-2]).append("\n");
        temp.setLength(0);
        inputOutputSlot(temp, outPutWidth);
        temp = decorateSquare(temp,outPutWidth);
        appendAllLinesCentered(b,removeLines(temp.toString(),5),1,0);
        return b;
    }

    private static String createRowOfGodsBoxes(List<String> chosen, int height, int numberOfPlayers) {
        StringBuilder temp = new StringBuilder();
        StringBuilder r = new StringBuilder();
        for(int row = 0; row < height-1; row++) {
            r.append(" ".repeat((numberOfPlayers == 3) ? 1 : 0));
            for(String box : chosen) {
                r.append(" ".repeat((numberOfPlayers == 3) ? 2 : 4))
                        .append(box.split("\\r?\\n")[row])
                        .append(" ".repeat((numberOfPlayers == 3) ? 2 : 4))
                        .append("|");
            }
            temp.append(r.toString(), 0, r.toString().length()-1);
            r.setLength(0);
            temp.append(" ".repeat((numberOfPlayers == 3) ? 1 : 0)).append("\n");
        }
        return temp.toString();
    }

    public static String createChoosingBox(int length, int height) {
        StringBuilder temp = new StringBuilder();
        temp.append(centerLine("The challenger is", length, false)).append(" \n").append(centerLine("choosing ...",length,false)).append("\n");
        for(int row = 3; row < height; row ++) {
            temp.append(getEmptyLength(length)).append("\n");
        }
        return temp.toString();
    }



    public static void printChallengerSelection(String message, List<String> chosenGods, int page, int numberOfPlayers, boolean input) {
        int outPutWidth = 116;
        int godsSlotsWidth = 28;
        int godsSlotsHeight = 10;

        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        createGodsInfoBox(b,page,godsSlotsWidth,godsSlotsHeight);
        addBrowseArrows(b,chosenGods,numberOfPlayers);
        String temp = decorateColumns(b).toString();
        b.setLength(0);
        for(String line : temp.split("\\r?\\n")) { b.append(centerLine(line,6,0)).append("\n"); }
        b.insert(0,"\n"+TOP_CHALLENGER+"\n");
        b.append(BOTTOM_CHALLENGER).append("\n");
        StringBuilder outputSlot = new StringBuilder();
        inputOutputSlot(outputSlot, outPutWidth);
        outputSlot = decorateSquare(outputSlot,outPutWidth);
        appendAllLinesCentered(b,removeLines(outputSlot.toString(),5),2,0);
        out.println(centerScreen( Ansi.CLEAR_CONSOLE + b));
        setCursor(10,5);

        out.flush();
    }

    public static void printStartScreen(String message, boolean input) {
        setMessage(message,input);
        StringBuilder b = new StringBuilder();
        int width = ROW.length();
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, fixSlots(TITLE), width,false);
        appendEmptyRow(b, width, 2);
        appendAllLinesCentered(b, CREDITS, width,false);
        appendEmptyRow(b, width, 1);
        inputOutputSlot(b, width);
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
        appendAllLinesCentered(b, fixSlots(TOP_LOBBY), width, false);
        appendAllLinesCentered(b, createPlayersBox(players, innerWidth), width, true);
        b.append(createColorsBox(players,innerWidth));
        appendAllLinesCentered(b, fixSlots(BOTTOM_LOBBY), width, false);
        StringBuilder temp = new StringBuilder();
        inputOutputSlot(temp, 96);
        temp = decorateSquare(temp,96);
        appendAllLinesCentered(b,fixSlots(removeLines(temp.toString(),5)),width+2,false);

        out.println(centerScreen(Ansi.CLEAR_CONSOLE + b, 42));
        setCursor(10,5);
        out.flush();
    }

    public static void createGodsInfoBox(StringBuilder b, int page, int length, int height) {
        List<String> godBoxes = getGodPage(page,length,height);
        addGodsRow(b,godBoxes,0,length,height);
        b.append("-".repeat(2+length+2)).append("|").append("-".repeat(2+length+2)).append("|").append("\n");
        b.append(" ".repeat(2+length+2)).append("|").append(" ".repeat(2+length+2)).append("|").append("\n");
        addGodsRow(b,godBoxes,2,length,height);
    }

    public static void addBrowseArrows(StringBuilder b, List<String> selectedGods, int numberOfPlayers) {
        String []arrow = ARROWS_CHALLENGER.split("\\r?\\n");
        String []godBox = b.toString().split("\\r?\\n");
        b.setLength(0);
        b.append(godBox[0]).append("  You selected ").append(selectedGods.size()).append("/").append(numberOfPlayers).append(" gods     ").append("\n");
        for(int row = 1; row<10; row++) {
            b.append(godBox[row]).append(fixSlots(arrow[row],28)).append("\n");
        }
        for(int row = 10; row < 13; row++) {
            b.append(godBox[row]).append(getEmptyLength(28)).append("\n");
        }
        if(selectedGods.size() == 0) { selectedGods.add("None yet"); }
        b.append(godBox[13]).append(fixSlots("  Gods selected:",28)).append("\n");
        b.append(godBox[14]).append(getEmptyLength(28)).append("\n");
        for(int row = 15; row<godBox.length; row++) {
            b.append(godBox[row]);
            b.append((row-15<selectedGods.size()) ? fixSlots("  "+selectedGods.get(row-15),28) : getEmptyLength(28));
            b.append("\n");
         }

    }

    public static void addGodsRow(StringBuilder b, List<String> godBoxes, int block, int length, int height) {
        for(int h = 0; h<height; h++) {
            b.append(" ".repeat(2));
            b.append(godBoxes.get(block).split("\\r?\\n")[h]);
            b.append(" ".repeat(2)).append("|").append(" ".repeat(2));
            b.append(godBoxes.get(block+1).split("\\r?\\n")[h]);
            b.append(" ".repeat(2)).append("|").append("\n");
        }
        b.append(" ".repeat(2+length+2)).append("|").append(" ".repeat(2+length+2)).append("|").append("\n");
    }

    public static List<String> getGodPage(int page, int length, int height) {
        int subListSize = 4;
        List<String> gods = Gods.getGodsStringList();
        List<List<String>> temp = new ArrayList<>();
        for (int i=0; i<gods.size(); i += subListSize) {
            temp.add(gods.subList(i, Math.min(i + subListSize, gods.size())));
        }
        gods = new ArrayList<>();
        List<String> pageGods = (page>-1) ? temp.get((page%(temp.size()))) : temp.get((temp.size()-((-page)%temp.size())));
        for(String god : pageGods) {
            gods.add(createGodWindow(god,length, height));
        }
        while(gods.size() < subListSize) {
            gods.add(createEmptyWindow(length,height));
        }
        return gods;
    }

    public static String createGodWindow(String godName, int length, int height) {
    StringBuilder temp = new StringBuilder();
    String []description = Gods.valueOf(godName).getDescription().split(":");
    temp.append(fixSlots("Name: "+godName,length)).append("\n");
    appendEmptyRow(temp, length, 1);
    temp.append(fixSlots("Effect ("+description[0]+"):",length)).append("\n");
    appendEmptyRow(temp, length, 1);
    for(String string : divideInRows(description[1].substring(1),length)) {
        temp.append(fixSlots(string,length)).append("\n");
    }
    appendEmptyRow(temp,length,height-temp.toString().split("\\r?\\n").length+1);
    return temp.toString();
    }

    public static String createEmptyWindow(int length, int height) {
        return ("\n"+" ".repeat(length)).repeat(height).substring(1);
    }

    public static List<String> divideInRows(String message, int rowLength) {
        List<String> temp = new ArrayList<>();
        StringBuilder row = new StringBuilder();
        for(String word : message.split(" ")) {
            if(row.toString().length() + word.length() > rowLength) {
                temp.add(row.toString());
                row.setLength(0);
            }
                row.append(word).append((row.toString().length() == rowLength) ? "" : " ");
        }
        temp.add(row.toString());
        return temp;
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
        String offset = " ".repeat(Math.max(0,(int)o));
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

    private static void appendAllLinesCentered(StringBuilder b, String text, int left, int right) {
        for(String line : text.split("\\r?\\n")) {
            b.append(" ".repeat(left)).append(line).append(" ".repeat(right)).append("\n");
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

    private static final String TOP_CHALLENGER = "    __________________________________________________________________________________________________________________ \n" +
            "   /                                                                                                                  \\ \n" +
            "  |'____'''''____''''____''''___''''___''''_____''''____''''_____''''_____''''_____''''_____''''_____''''____'''''____'|   \n" +
            "   /    \\---/    \\                           ( )                              ( )                       /    \\---/    \\\n" +
            "  ( (. \\     / .) )                           |                                |                       ( (. \\     / .) )\n" +
            "   \\___/-----\\___/                            |                                |                        \\___/-----\\___/ \n" +
            "       | | | |                                |                                |                            | | | |     ";

    private static final String BOTTOM_CHALLENGER = "      (0OUwUo0)                               |                                |                           (0OUwUo0)\n" +
            "      #########                              ( )                              ( )                          #########    \n" +
            "   .zzzzzzzzzzzzz----------------------------------------------------------------------------------------zzzzzzzzzzzzz.\n" +
            "  /____________________________________________________________________________________________________________________\\";

    private static final String TOP_SELECTION_3 = "    __________________________________________________________________________________________________________________ \n" +
            "   /                                                                                                                  \\ \n" +
            "  |'____'''''____'''____''''____''''____''''____'''____''''____''''____'''____''''____''''____''''____'''____'''''____'|\n" +
            "   /    \\---/    \\                          ( )                            ( )                          /    \\---/    \\\n" +
            "  ( (. \\     / .) )                          |                              |                          ( (. \\     / .) )\n" +
            "   \\___/-----\\___/                           |                              |                           \\___/-----\\___/ \n" +
            "       | | | |                               |                              |                               | | | |     \n" +
            "       | | | |                               |                              |                               | | | |\n" +
            "       | | | |                               |                              |                               | | | |";

    private static final String BOTTOM_SELECTION_3 = "       | | | |                               |                              |                               | | | |\n" +
            "       | | | |                               |                              |                               | | | |\n" +
            "      (0OUwUo0)                              |                              |                              (0OUwUo0)\n" +
            "      #########                             ( )                            ( )                             #########  \n" +
            "   .zzzzzzzzzzzzz----------------------------------------------------------------------------------------zzzzzzzzzzzzz.\n" +
            "  /____________________________________________________________________________________________________________________\\";

    private static final String TOP_SELECTION_2 = "    _________________________________________________________________________________________ \n" +
            "   /                                                                                         \\ \n" +
            "  |'____'''''____''''____''''____''''____'''''_____'''''____''''____''''____''''____'''''____'|\n" +
            "   /    \\---/    \\                             ( )                             /    \\---/    \\\n" +
            "  ( (. \\     / .) )                             |                             ( (. \\     / .) )\n" +
            "   \\___/-----\\___/                              |                              \\___/-----\\___/ \n" +
            "       | | | |                                  |                                  | | | |     \n" +
            "       | | | |                                  |                                  | | | |\n" +
            "       | | | |                                  |                                  | | | |";

    private static final String BOTTOM_SELECTION_2 = "       | | | |                                  |                                  | | | |\n" +
            "       | | | |                                  |                                  | | | |\n" +
            "       | | | |                                  |                                  | | | |\n" +
            "      (0OUwUo0)                                 |                                 (0OUwUo0)\n" +
            "      #########                                ( )                                #########\n" +
            "   .zzzzzzzzzzzzz---------------------------------------------------------------zzzzzzzzzzzzz.\n" +
            "  /___________________________________________________________________________________________\\";

    private static final String []TOP_SELECTION = {TOP_SELECTION_2, TOP_SELECTION_3};
    private static final String []BOTTOM_SELECTION = {BOTTOM_SELECTION_2, BOTTOM_SELECTION_3};

    private static final String ARROWS_CHALLENGER = "                          \n" +
            "                          \n" +
            "  Type 1 and 2 to browse  \n" +
            "  more available gods     \n" +
            "                          \n" +
            "                          \n" +
            "      /|__       __|\\      \n" +
            "   1 /    |     |    \\ 2   \n" +
            "     \\  __|     |__  /     \n" +
            "      \\|           |/      ";

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
