package it.polimi.ingsw.mvc.view.cli;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.GameState;

import java.util.*;
import java.util.function.Predicate;

public class Cli extends Client {

    private final Scanner cliInput;

    private String inputSave;
    private int godPage;
    private List<String> allSelectedGods;
    private String ip;
    private int port;
    private static volatile boolean waitingInput;

    public Cli(String ip, int port, boolean log) {
        super(log);
        cliInput = new Scanner(System.in);
        initCli();
        this.ip = ip;
        this.port = port;
        waitConnectionRequest(ip, port);
    }

    private void initCli() {
        inputSave = "";
        allSelectedGods = new ArrayList<>();
        godPage = 0;
    }

    @Override
    protected void init() {
        super.init();
        initCli();
    }

    private static synchronized void setWaitingInput(boolean value) {
        waitingInput = value;
    }

    private String requestInput() {
        while (true) {
            try {
                while (waitingInput) Thread.onSpinWait();
                setWaitingInput(true);
                while (waitingInput) {
                    String in = cliInput.nextLine();
                    if (!reconnecting) {
                        setWaitingInput(false);
                        return (in != null) ? in : "";
                    } else log.info("[CLI] Input refused because client lost connection to the server");
                }
            } catch (IndexOutOfBoundsException e) { /* nothing */ }
        }
    }

    private void waitConnectionRequest(String ip, int port) {
        CliScene.printStartScreen("Press [ENTER] when you are ready", true);
        requestInput(); // waiting for [ENTER]
        while (!createConnection(ip, port)) {
            CliScene.printStartScreen("Connection failed! The server is unreachable, press [ENTER] to try again", true);
            requestInput(); // waiting for [ENTER]
        }
    }

    @Override
    public void requestNumberOfPlayers() {
        CliScene.printStartScreen("You are the first player to connect!\nchoose if you want to play as 2 or 3 people (Type 2 or 3) ", true);
        while (!validateNumberOfPlayers(requestInput().toUpperCase())) {
            if (isConnectionLost()) return;
            CliScene.printStartScreen("The number you typed is not valid, please choose 2 or 3:", true);
        }
    }

    @Override
    public void showQueueInfo(String text) {
        if (state == GameState.PRE_LOBBY) {
            CliScene.printStartScreen(text, false);
        }
    }

    @Override
    public void requestLogin() {
        String requestedUsername = requestLobbyInput("Input username: ",
                "This username is invalid, choose a different one: ",
                this::validateUsername);
        if (isConnectionLost()) return;
        String requestedColor = requestLobbyInput("Selected name: " + requestedUsername + ", choose one of the available colors: "
                        + (requestedUsername.length() % 2 == 0 ? " " : ""),
                "This color does not exist or was already chosen, select a different one:",
                color -> validateColor(color.toUpperCase())).toUpperCase();
        if (isConnectionLost()) return;
        requestLogin(requestedUsername, Colors.valueOf(requestedColor));
    }

    private String requestLobbyInput(String request, String error, Predicate<String> check) {
        inputSave = request;
        CliScene.printLobbyScreen(inputSave, players, true);
        String someInput = requestInput();
        while (!isConnectionLost() && Boolean.TRUE.equals(check.test(someInput))) {
            someInput = showWrongInput(error);
        }
        return someInput;
    }

    private String showWrongInput(String text) {
        inputSave = text;
        CliScene.printLobbyScreen(inputSave, players, true);
        return requestInput();
    }

    @Override
    public void showLobby(List<Colors> availableColors) {
        boolean input = true;
        if (players.containsKey(username)) {
            inputSave = "Waiting for other players to join ... ";
            input = false;
        }
        CliScene.printLobbyScreen(inputSave, players, input);
    }

    @Override
    public void updateChallengerGodSelection(List<String> gods) {
        inputSave = "You are not the challenger, wait while " + challenger + " chooses " + players.size() + " gods";
        CliScene.printChallengerGodsUpdate(inputSave, new ArrayList<>(gods), players.size());
    }

    @Override
    public void updatePlayerGodSelection(String turnOwner, Map<String, String> choices, List<String> chosenGods) {
        if (allSelectedGods.isEmpty()) allSelectedGods = new ArrayList<>(chosenGods);
        inputSave = turnOwner + " is choosing his god, wait . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), false);
    }

    @Override
    public void requestChallengerGod(List<String> gods) {
        inputSave = "You are the challenger! Type the name of one god to select it ";
        while (true) {
            CliScene.printChallengerSelection(inputSave, new ArrayList<>(gods), godPage, players.keySet().size(), true);
            String choice = requestInput().toUpperCase();
            if (isConnectionLost()) return;
            if (choice.equals("1") || choice.equals("2")) changeGodPage(Integer.parseInt(choice));
            else {
                if (validateGods(choice)) break;
                else inputSave = "God not available, choose a different one:";
            }
        }
    }

    private void changeGodPage(int choice) {
        if (choice == 1) godPage = (godPage == -3) ? 0 : godPage - 1;
        else godPage = (godPage == 3) ? 0 : godPage + 1;
    }

    @Override
    public void requestPlayerGod(List<String> chosenGods, Map<String, String> choices) {
        if (allSelectedGods.isEmpty()) allSelectedGods = new ArrayList<>(chosenGods);
        inputSave = "Choose one of the available gods:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), true);
        while (!validatePlayerGodChoice(requestInput().toUpperCase())) {
            if (isConnectionLost()) return;
            inputSave = "This god isn't available, please choose a different one: ";
            try {
                CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), true);
            } catch (NullPointerException e) {
                break;
            }
        }
    }

    @Override
    public void requestStarterPlayer(Map<String, String> choices) {
        inputSave = "As the challenger type the name of the starter player:";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), true);
        while (!validatePlayer(requestInput())) {
            inputSave = "This player doesn't exist, choose again:";
            try {
                CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), true);
            } catch (NullPointerException e) {
                break;
            }
        }
    }

    @Override
    public void updateStarterPlayerSelection(Map<String, String> choices) {
        inputSave = "The challenger " + challenger + " is choosing the starter player . . .";
        CliScene.printPlayerGodSelection(inputSave, choices, allSelectedGods, players.size(), false);
    }

    @Override
    public void showBoard(DtoSession session, Map<String, Colors> colors, Map<String, String> gods, String turnOwner) {
        CliScene.printBoardScreen(session, new HashMap<>(colors), new HashMap<>(gods), turnOwner, false);
    }

    @Override
    public void requestTurnAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, boolean isSpecialPowerActive) {
        Action action = requestAction(possibleActions, session, colors, gods);
        if (action == null) return;
        List<DtoPosition> positions = possibleActions.get(action);
        if (positions == null) return;
        DtoPosition position = null;
        if (action != Action.END_TURN && action != Action.SPECIAL_POWER) {
            possibleActions = new EnumMap<>(Action.class);
            possibleActions.put(action, positions);
            position = requestPosition(possibleActions, session, colors, gods, action);
        }
        validateAction(action, position, possibleActions);
    }

    private Action requestAction(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods) {
        String x;
        inputSave = "Type the chosen action number";
        do {
            CliScene.printBoardScreen(inputSave, session, new HashMap<>(colors), new HashMap<>(gods), possibleActions, username, false);
            x = requestInput();
            if (isConnectionLost()) return null;
            inputSave = "Invalid number, retype it";
        }
        while (!x.matches("-?\\d+") || !validateRange(possibleActions.size() - 1, Integer.parseInt(x)));
        return new ArrayList<>(possibleActions.keySet()).get(Integer.parseInt(x));
    }

    private DtoPosition requestPosition(Map<Action, List<DtoPosition>> possibleActions, DtoSession session, Map<String, Colors> colors, Map<String, String> gods, Action a) {
        List<Integer> c;
        do {
            inputSave = "Insert the pos. (es: A1 or 1A)";
            while (true) {
                CliScene.printBoardScreen(inputSave, session, new HashMap<>(colors), new HashMap<>(gods), possibleActions, username, true);
                String[] input = requestInput().split("");
                if (isConnectionLost()) return null;
                if (input.length == 2 && !decodeCoordinatesValue(input).isEmpty()) {
                    c = decodeCoordinatesValue(input);
                    break;
                } else inputSave = "Unexpected pos. retype (es: 1A)";
            }
        } while (!validatePosition(possibleActions.get(a), c.get(0), c.get(1)));
        return new DtoPosition(new Position(c.get(0), c.get(1)));
    }

    private List<Integer> decodeCoordinatesValue(String[] input) {
        List<Integer> c = new ArrayList<>();
        if (isInt(input[0]) && isValidCoordinate(input[1])) {
            c.add(convertXBoardCoordinate(input[1]));
            c.add(Integer.parseInt(input[0]) - 1);
        } else if (isInt(input[1]) && isValidCoordinate(input[0])) {
            c.add(convertXBoardCoordinate(input[0]));
            c.add(Integer.parseInt(input[1]) - 1);
        }
        return c;
    }

    private int convertXBoardCoordinate(String value) {
        return ((int) value.toUpperCase().charAt(0)) - 'A';
    }

    private boolean isValidCoordinate(String value) {
        int c = convertXBoardCoordinate(value);
        return (c >= 0 && c <= 5);
    }

    private boolean isInt(String value) {
        if (value == null) return false;
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void showReconnection(boolean isReconnecting) {
        /* There is no reconnection window notification on CLI */
    }

    @Override
    public void showDisconnected(String info) {
        while (waitingInput) Thread.onSpinWait();
        CliScene.printStartScreen(info + "\nPress start to go back to the title screen.", true);
        requestInput();
        waitConnectionRequest(ip, port);

    }

    @Override
    public void showWin(String playerName) {
        init();
        showDisconnected(playerName + " has won!");
    }

    @Override
    public void showLose(String playerName) {
        /* There is no loss window notification on CLI */
    }

}
