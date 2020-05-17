package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.utility.serialization.dto.DtoPosition;
import it.polimi.ingsw.utility.serialization.dto.DtoSession;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Cli extends Client {


    private final Scanner input;

    private String inputSave;
    private int godPage;
    private List<String> allSelectedGods;

    public Cli(String ip, int port) {
        input = new Scanner(System.in);
        inputSave = "";
        allSelectedGods = new ArrayList<>();
        waitConnectionRequest(ip, port);
    }

    private String requestInput() {
        while(true) {
            try {
                return input.nextLine();
            } catch (IndexOutOfBoundsException e) { /* nothing */ }
        }
    }

    private void waitConnectionRequest(String ip, int port) {
        CliScene.printStartScreen("Press [ENTER] when you are ready",true);
        requestInput(); // waiting for [ENTER]
        while(!createConnection(ip, port)) {
            CliScene.printStartScreen("Connection failed! The server is unreachable, press [ENTER] to try again",true);
            requestInput(); // waiting for [ENTER]
        }
    }

    /* CREAZIONE PARTITA *///////////////////////////////////////////////////////////////////////////////////////
    public void requestNumberOfPlayers() {
        CliScene.printStartScreen("You are the first player to connect!\nchoose if you want to play as 2 or 3 people (Type 2 or 3) ",true);
        while(!validateNumberOfPlayers(requestInput().toUpperCase())) {
            CliScene.printStartScreen("The number you typed is not valid, please choose 2 or 3:",true);
        }
    }

    public void showInputText(String text) {
        //Scene.putOutputRequest("\n"+text);
        //Scene.printScene();
    }

    public void showInfo(String text) {
        if(state == GameState.PRE_LOBBY) {
            CliScene.printStartScreen(text,false);
        }

    }

    //^^^ CREAZIONE PARTITA ^^^///////////////////////////////////

    /* LOBBY *///////////////////////////////////////////////////////////////////////////////////////
    public void requestLogin() {
        String requestedUsername = requestLobbyInput("Input username: ",
                                                       "This username is invalid, choose a different one: ",
                                                            (username) -> !validateUsername(username));
        String requestedColor = requestLobbyInput("Selected name: "+requestedUsername+", choose one of the available colors: "
                                                          +(requestedUsername.length()%2==0 ? " " : ""),
                                                    "This color does not exist or was already chosen, select a different one:",
                                                          (color) -> !validateColor(color.toUpperCase())).toUpperCase();
        requestLogin(requestedUsername, Colors.valueOf(requestedColor));
    }

    private String requestLobbyInput(String request, String error, Function<String,Boolean> check) {
        inputSave = request;
        CliScene.printLobbyScreen(inputSave, players,true);
        String someInput = requestInput();
        while(check.apply(someInput)) {
            someInput = showWrongInput(error);
        }
        return someInput;
    }

    private String showWrongInput(String text) {
        inputSave = text;
        CliScene.printLobbyScreen(inputSave, players,true);
        return requestInput();
    }

    public void showLobby(List<Colors> availableColors) {
        boolean input = true;
        if(players.containsKey(username)) {
            inputSave = "Waiting for other players to join ... ";
            input = false;
        }
        CliScene.printLobbyScreen(inputSave, players,input);
    }

    //^^^ LOBBY ^^^//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //*GOD SELECTION*//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateChallengerGodSelection(List<String> gods) {
        inputSave = "You are not the challenger, wait while " +challenger+" chooses "+players.size()+" gods";
        CliScene.printChallengerGodsUpdate(inputSave, new ArrayList<>(gods), players.size());
    }

    public void updatePlayerGodSelection(String turnOwner, Map<String,String> choices, List<String> chosenGods) {
        if(allSelectedGods.isEmpty()) { allSelectedGods = new ArrayList<>(chosenGods); }
        inputSave = turnOwner+" is choosing his god, wait . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),false);
    }

    public void requestChallengerGod(List<String> gods) {
        inputSave = "You are the challenger! Type the name of one god to select it ";
        while(true) {
            CliScene.printChallengerSelection(inputSave, new ArrayList<>(gods), godPage, players.keySet().size(), true);
            String choice = requestInput().toUpperCase();
            if(choice.equals("1")) { godPage = (godPage == -3) ? 0 : godPage-1; }
            else if(choice.equals("2")) { godPage = (godPage == 3) ? 0 : godPage+1; }
            else {
                if (validateGods(choice)) { break; }
                else {
                    inputSave = "God not available, choose a different one:";
                    CliScene.printChallengerSelection(inputSave, new ArrayList<>(gods), godPage, players.keySet().size(), true);
                }
            }
        }
    }

    public void requestPlayerGod(List<String> chosenGods, Map<String,String> choices){
        if(allSelectedGods.isEmpty()) { allSelectedGods = new ArrayList<>(chosenGods); }
        inputSave = "Choose one of the available gods:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        while(!validatePlayerGodChoice(requestInput().toUpperCase())) {
            inputSave = "This god isn't available, please choose a different one: ";
            CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        }
    }

    public void requestStarterPlayer(Map<String,String> choices){
        inputSave = "As the challenger type the name of the starter player:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        while(!validatePlayer(requestInput())) {
            inputSave = "This player doesn't exist, choose again:";
            CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        }
    }

    public void updateStarterPlayerSelection(Map<String,String> choices) {
        inputSave = "The challenger "+challenger+" is choosing the starter player . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),false);
    }

    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions) {
        System.out.println("Possible actions:\n");
        possibleActions.keySet().forEach(action -> System.out.println(action + " on "+possibleActions.get(action)+"\n"));
        System.out.println("\n");
        Action action = requestAction(possibleActions.keySet());
        DtoPosition position = null;
        if(action != Action.END_TURN) {
            position = requestPosition(possibleActions.get(action));
        }
        sendMessage(new ActionMessage(username, "Action request", action, position));
        System.out.println("\n");
    }

    private Action requestAction(Set<Action> possibleActions) {
        String x;
        do{
            System.out.println("\nChoose an action (0, 1, 2, ...) on previous list:\n");
            x = requestInput();
        }
        while(!validateAction(possibleActions.size()-1,Integer.parseInt(x)));
        return new ArrayList<>(possibleActions).get(Integer.parseInt(x));
    }

    private DtoPosition requestPosition(List<DtoPosition> possiblePositions) {
        int x, y;
        do {
            System.out.println("\nChoose x:");
            x = Integer.parseInt(requestInput());
            System.out.println("\nChoose y:");
            y = Integer.parseInt(requestInput());
        }
        while(!validatePosition(possiblePositions, x, y));
        return new DtoPosition(new Position(x,y));
    }


    public void showBoard(DtoSession session){
        System.out.println(session);
    }

}
