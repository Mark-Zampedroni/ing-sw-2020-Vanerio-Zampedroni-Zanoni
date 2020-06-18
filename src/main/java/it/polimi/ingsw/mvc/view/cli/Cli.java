package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;

import java.util.*;
import java.util.function.Function;

public class Cli extends Client {

    private final Scanner input;

    private String inputSave;
    private int godPage;
    private List<String> allSelectedGods;
    private String ip;
    private int port;

    public Cli(String ip, int port, boolean log) {
        super(log);
        input = new Scanner(System.in);
        initCli();
        this.ip = ip;
        this.port = port;
        waitConnectionRequest(ip, port);
    }

    private void initCli(){
        inputSave = "";
        allSelectedGods = new ArrayList<>();
        godPage = 0;
    }


    private String requestInput() {
        while(true) {
            try {
                String in = input.nextLine();
                if(!reconnecting) {
                    return in;
                }
                else {
                    LOG.info("[CLI] Input refused because client lost connection to server");
                }
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

    @Override
    public void requestNumberOfPlayers() {
        CliScene.printStartScreen("You are the first player to connect!\nchoose if you want to play as 2 or 3 people (Type 2 or 3) ",true);
        while(!validateNumberOfPlayers(requestInput().toUpperCase())) {
            CliScene.printStartScreen("The number you typed is not valid, please choose 2 or 3:",true);
        }
    }

    @Override
    public void showQueueInfo(String text) {
        if(state == GameState.PRE_LOBBY) {
            CliScene.printStartScreen(text,false);
        }
    }

    @Override
    public void requestLogin() {
        String requestedUsername = requestLobbyInput("Input username: ",
                                                       "This username is invalid, choose a different one: ",
                                                        this::validateUsername);
        String requestedColor = requestLobbyInput("Selected name: "+requestedUsername+", choose one of the available colors: "
                                                          +(requestedUsername.length()%2==0 ? " " : ""),
                                                    "This color does not exist or was already chosen, select a different one:",
                                                          (color) -> validateColor(color.toUpperCase())).toUpperCase();
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

    @Override
    public void showLobby(List<Colors> availableColors) {
        boolean input = true;
        if(players.containsKey(username)) {
            inputSave = "Waiting for other players to join ... ";
            input = false;
        }
        CliScene.printLobbyScreen(inputSave, players, input);
    }

    @Override
    public void updateChallengerGodSelection(List<String> gods) {
        inputSave = "You are not the challenger, wait while " +challenger+" chooses "+players.size()+" gods";
        CliScene.printChallengerGodsUpdate(inputSave, new ArrayList<>(gods), players.size());
    }

    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String,String> choices, List<String> chosenGods) {
        if(allSelectedGods.isEmpty()) { allSelectedGods = new ArrayList<>(chosenGods); }
        inputSave = turnOwner+" is choosing his god, wait . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),false);
    }

    @Override
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

    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String,String> choices){
        if(allSelectedGods.isEmpty()) { allSelectedGods = new ArrayList<>(chosenGods); }
        inputSave = "Choose one of the available gods:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        while(!validatePlayerGodChoice(requestInput().toUpperCase())) {
            inputSave = "This god isn't available, please choose a different one: ";
            CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        }
    }

    @Override
    public void requestStarterPlayer(Map<String,String> choices){
        inputSave = "As the challenger type the name of the starter player:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        while(!validatePlayer(requestInput())) {
            inputSave = "This player doesn't exist, choose again:";
            CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),true);
        }
    }

    @Override
    public void updateStarterPlayerSelection(Map<String,String> choices) {
        inputSave = "The challenger "+challenger+" is choosing the starter player . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(),false);
    }

    @Override
    public void showBoard(DtoSession session, Map<String,Colors> colors, Map<String,String> gods) {
        CliScene.printBoardScreen(session, new HashMap<>(colors), new HashMap<>(gods));
    }

    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String,Colors> colors, Map<String,String> gods, boolean isSpecialPowerActive) {
        CliScene.printBoardScreen(session, new HashMap<>(colors), new HashMap<>(gods), possibleActions);
        // TEST DA QUI SOTTO ---
        if(possibleActions.containsKey(Action.SPECIAL_POWER)) { System.out.println("Special power: "+((isSpecialPowerActive) ? "ON" : "OFF")); }
        Action action = requestAction(possibleActions.keySet());
        List<DtoPosition> positions = possibleActions.get(action);
        DtoPosition position = null;
        if(action != Action.END_TURN && action != Action.SPECIAL_POWER) {
            possibleActions = new HashMap<>();
            possibleActions.put(action,positions);
            CliScene.printBoardScreen(session, new HashMap<>(colors), new HashMap<>(gods), possibleActions);
            position = requestPosition(possibleActions.get(action));
        }
       validateAction(action, position, possibleActions);

    }

    private Action requestAction(Set<Action> possibleActions) {
        String x;
        System.out.println(possibleActions);
        do {
            System.out.println("\nChoose an action (0, 1, 2, ...) on previous list:\n");
            x = requestInput();
        }
        while(!x.matches("-?\\d+") || !validateRange(possibleActions.size()-1,Integer.parseInt(x)));
        return new ArrayList<>(possibleActions).get(Integer.parseInt(x));
    }

    private DtoPosition requestPosition(List<DtoPosition> possiblePositions) {
        int x, y;
        do {
            System.out.println("\nChoose x:");
            while(true) {
                try {
                    x = Integer.parseInt(requestInput());
                    break;
                } catch(Exception e) { /**/ }
            }
            System.out.println("\nChoose y:");
            while(true) {
                try {
                    y = Integer.parseInt(requestInput());
                    break;
                } catch(Exception e) { /**/ }
            }
        }
        while(!validatePosition(possiblePositions, x, y));
        return new DtoPosition(new Position(x,y));
    }

    @Override
    public void showReconnection(boolean isReconnecting) {
        System.out.println(isReconnecting ? "-> FINESTRA RICONNESSIONE IN CORSO" : "-> RICARICAMENTO FINESTRA PRECEDENTE");
    }

    @Override
    public void showDisconnected(String info) {
        CliScene.printStartScreen(info+".\nPress start to go back to the title screen.",true);
        requestInput();
        waitConnectionRequest(ip, port);
    }

    @Override
    public void showWin(String playerName) {
        System.out.println(playerName+" has won");
        initCli();
        waitConnectionRequest(ip,port);
    }

    @Override
    public void showLose(String playerName) {
        System.out.println(playerName+" has lost");
    }

}
