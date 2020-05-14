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

public class Cli extends Client {


    private final Scanner input;

    private String inputSave = "";

    public Cli(String ip, int port) {
        input = new Scanner(System.in);
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
                                                       "This username is already taken, choose a different one: ",
                                                            (username) -> !validateUsername(username));
        String requestedColor = requestLobbyInput("Selected name: "+requestedUsername+", choose one of the available colors:",
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

    public void updateChallengerGodSelection() {
        //Scene.clearScenario();
        showAvailableGods();
        if(!chosenGods.isEmpty()) { showChosenGods(); }
        showInputText((challenger.equals(username))
                ? "You are the challenger! Choose "+players.size()+" gods\nType the name of 1 god:"
                : "You are not the challenger, wait while " +challenger+" chooses "+players.size()+" gods");
    }

    public void updatePlayerGodSelection() {
        //Scene.clearScenario();
        showChosenGods();
        showPlayerGod();
        //Scene.printScene();
    }

    public void showAvailablePlayers() {
        //Scene.clearScenario();
        //Scene.addToScenario("Players:\n");
        //players.keySet().forEach(p -> Scene.addToScenario(p+" with "+players.get(p).toString()+"\n"));
        //Scene.printScene();
    }

    public void showPlayerGod() {
        //Scene.addToScenario("Players with their Gods:\n");
        //gods.keySet().forEach(p -> Scene.addToScenario(p+" with "+gods.get(p)+"\n"));
    }

    public void requestChallengerGod(){
        while(true) {
            if(validateGods(requestInput().toUpperCase())) {
                break;
            }
        }
    }

    private void showAvailableGods() {
        //Scene.addToScenario("Available gods:\n");
        //getAvailableGods().forEach(g -> Scene.addToScenario("Name: " + g.toString() + ", Description: " + g.getDescription() + "\n"));
    }

    public void showChosenGods() {
        //Scene.addToScenario("\nChosen gods: \n");
        //chosenGods.stream().map(Gods::valueOf).forEach(g ->  Scene.addToScenario("Name: "+g.toString()+", Description: "+g.getDescription()+"\n"));
    }

    public void requestPlayerGod(){
        showInputText("Choose one of the available gods:");
        while(true) {
            if(validatePlayerGodChoice(requestInput().toUpperCase())) {
                break;
            }
        }
    }

    public void requestStarterPlayer(){
        showInputText("Choose the starting player: ");
        while(true) {
            if(validatePlayer(requestInput())) {
                break;
            }
        }
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
