package it.polimi.ingsw.network.client;

import it.polimi.ingsw.mvc.view.View;
import it.polimi.ingsw.network.messages.FlagMessage;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.StateUpdateMessage;
import it.polimi.ingsw.network.messages.game.ActionMessage;
import it.polimi.ingsw.network.messages.game.ActionUpdateMessage;
import it.polimi.ingsw.network.messages.lobby.LobbyUpdate;
import it.polimi.ingsw.network.messages.lobby.RegistrationMessage;
import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.enumerations.*;
import it.polimi.ingsw.utility.observer.Observer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

/**
 * Client, it's implemented as {@link it.polimi.ingsw.mvc.view.cli.Cli cli} and {@link it.polimi.ingsw.mvc.view.gui.GuiController gui}
 */
public abstract class Client implements Observer<Message>, View {

    private static final String RECIPIENT = "SERVER";
    /**
     * Logger of the client
     */
    public final Logger log;
    protected GameState state;
    protected String username;
    protected boolean reconnecting = false;
    protected ClientConnection connection;
    protected String challenger;
    protected List<String> chosenGods;
    protected Map<String, Colors> players;
    protected Map<String, String> gods;
    private List<Runnable> requests;
    private List<Runnable> inputRequests;
    private boolean isDiscParsed;

    /**
     * Constructor
     *
     * @param log if {@code true} creates a log file
     */
    public Client(boolean log) {
        init();
        this.log = Logger.getLogger("client");
        if (log) startLogging();
        this.log.setUseParentHandlers(false); // <- Set True for debugging
    }

    /**
     * Initializes a client
     */
    protected void init() {
        chosenGods = new ArrayList<>();
        requests = new ArrayList<>();
        inputRequests = new ArrayList<>();
        reconnecting = false;
        clearSessionVars();
    }

    /**
     * Initializes the logging
     */
    private void startLogging() {
        DateFormat dateFormat = new SimpleDateFormat("MM_dd_HH-mm-ss");
        Date date = new Date();
        try {
            FileHandler fileHandler = new FileHandler(dateFormat.format(date) + ".log");
            fileHandler.setFormatter(new SimpleFormatter());
            log.addHandler(fileHandler);
        } catch (IOException e) {
            log.severe(e.getMessage() + " couldn't be opened\n");
        }
    }


    /**
     * Adds the specified output request to the queue
     *
     * @param request request as runnable
     */
    private void viewRequest(Runnable request) {
        requests.add(request);
    }

    /**
     * Adds the specified input request to the queue
     *
     * @param request request as runnable
     */
    private void inputRequest(Runnable request) {
        inputRequests.add(request);
    }

    /**
     * Executes and consumes all the requests in queue
     */
    public void flushRequests() {
        requests.forEach(Runnable::run);
        requests.clear();
        inputRequests.forEach(r -> new Thread(r).start());
        inputRequests.clear();
    }

    /**
     * Opens a connection
     *
     * @param ip server ip
     * @param port connection port
     * @return {@code true} if the connection is created
     */
    public boolean createConnection(String ip, int port) {
        state = GameState.CONNECTION;
        try {
            connection = new ClientConnection(ip, port, this);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Parses a message, then consumes the queue of requests
     *
     * @param message received message
     */
    @Override
    public void update(Message message) {
        if ((message.getType() == MessageType.CONNECTION_TOKEN && state == GameState.CONNECTION) ||
                message.getRecipient().equals(username) ||
                message.getRecipient().equals("ALL")) {
            log.info(() -> "[CLIENT] Received message " + message);
            switch (message.getType()) {
                case CONNECTION_TOKEN:
                    if (state == GameState.CONNECTION) parseConnectionMessage(message);
                    break;
                case SLOTS_UPDATE:
                    if (state == GameState.PRE_LOBBY) parseSlotMessage();
                    break;
                case INFO_UPDATE:
                    parseInfoMessage(message);
                    break;
                case DISCONNECTION_UPDATE:
                    parseDisconnectMessage(message);
                    break;
                case REGISTRATION_UPDATE:
                    parseRegistrationReply((FlagMessage) message);
                    break;
                case LOBBY_UPDATE:
                    parseLobbyUpdate((LobbyUpdate) message);
                    break;
                case STATE_UPDATE:
                    parseStateUpdate((StateUpdateMessage) message);
                    break;
                case SELECTION_UPDATE:
                    parseChallengerSelection(message);
                    break;
                case GODS_UPDATE:
                    parseManagementMessage((FlagMessage) message);
                    break;
                case GODS_SELECTION_UPDATE:
                    parseGodPlayerChoice(message);
                    break;
                case TURN_UPDATE:
                    parseTurnUpdate((ActionUpdateMessage) message);
                    break;
                case RECONNECTION_REPLY:
                    parseReconnectionReply((FlagMessage) message);
                    break;
                case WIN_LOSE_UPDATE:
                    parseWinLoseUpdate((FlagMessage) message);
                    break;
                case RECONNECTION_UPDATE:
                    parseReconnectionUpdate();
                default: //
            }
            flushRequests();
        }
    }


    /**
     * Parses a winning or loosing message
     *
     * @param message received message
     */
    private void parseWinLoseUpdate(FlagMessage message) {
        if (message.getFlag()) {
            reconnecting = false;
            connection.setDisconnected();
            closeGame();
            viewRequest(() -> showWin(message.getInfo()));
        } else {
            players.remove(message.getInfo());
            gods.remove(message.getInfo());
            if (message.getInfo().equals(username)) {
                connection.setHasToReconnect(false);
            }
            viewRequest(() -> showLose(message.getInfo()));
        }

    }

    /**
     * Parses a connection message
     *
     * @param message received message
     */
    private void parseConnectionMessage(Message message) {
        username = message.getInfo();
        state = GameState.PRE_LOBBY;
    }

    /**
     * Shows on the View the reconnection reply
     */
    private void parseReconnectionUpdate() {
        if (connection.getHasToReconnect()) {
            reconnecting = true;
            viewRequest(() -> showReconnection(true));
        }
    }

    /**
     * Parses a reconnection message
     *
     * @param message received message
     */
    private void parseReconnectionReply(FlagMessage message) {
        reconnecting = false;
        if (!message.getFlag()) {
            closeGame();
            log.info("[CLIENT] Couldn't reconnect because: " + message.getInfo()); // Quits game
        }
        viewRequest((!message.getFlag()) ? () -> showDisconnected(message.getInfo()) : () -> showReconnection(false));
    }

    /**
     * Shows an input request; asks the player to choose between 2 or 3 slots for the game
     */
    private void parseSlotMessage() { // TEST CLI
        viewRequest(this::requestNumberOfPlayers);
    }

    /**
     * Checks if the given number of players is valid
     *
     * @param number number of player as string
     * @return {@code true} if it's valid
     */
    public boolean validateNumberOfPlayers(String number) {
        if (number.equals("2") || number.equals("3")) { // Controllo su client - implementato anti-cheat su server
            sendMessage(new Message(MessageType.SLOTS_UPDATE, username, number, RECIPIENT));
            return true;
        }
        return false;
    }

    /**
     * Checks if a given player name is allowed
     *
     * @param requestedUsername name of the player
     * @return {@code true} if it's valid
     */
    public boolean validateUsername(String requestedUsername) {
        return (connection != null) && (players.containsKey(requestedUsername) || requestedUsername.length() <= 0 || requestedUsername.length() >= 12 || isNumeric(requestedUsername));
    }

    /**
     * Checks if a given string can be cast into a number
     *
     * @param string number as string
     * @return {@code true} if the given string can be turn into a number
     */
    private boolean isNumeric(String string) {
        try {
            Integer.valueOf(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a given color is valid
     *
     * @param requestedColor chosen color
     *@return {@code true} if it's valid
     */
    public boolean validateColor(String requestedColor) {
        return !Colors.isValid(requestedColor) || players.containsValue(Colors.valueOf(requestedColor));
    }


    /**
     * Sends to the server a registration request
     *
     * @param requestedUsername selected username
     * @param color chosen color
     */
    public void requestLogin(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(username, requestedUsername, color, RECIPIENT));
    }


    /**
     * Parses a lobby update message from the server; if the client is not registered
     * shows to the player the request to choose an username and a color
     *
     * @param message received message
     */
    private void parseLobbyUpdate(LobbyUpdate message) {
        players = message.getPlayers();
        if (state == GameState.PRE_LOBBY) {
            state = GameState.LOGIN;
            inputRequest(this::requestLogin);
        }
        if (state == GameState.LOGIN || state == GameState.LOBBY) viewRequest(() -> showLobby(message.getColors()));
    }

    /**
     * Parses a disconnection message
     *
     * @param message message to display on the disconnection tab (if shown)
     */
    private void parseDisconnectMessage(Message message) {
        if (!isDiscParsed) {
            if (state != GameState.PRE_LOBBY) closeGame();
            if (connection != null) connection.setDisconnected();
            showDisconnected(message.getInfo());
            if (message.getSender().equals(RECIPIENT)) isDiscParsed = true;
        } else isDiscParsed = false;
    }


    /**
     * Parses a message regarding the queue of players waiting to join the lobby
     *
     * @param message message to display
     */
    private void parseInfoMessage(Message message) {
        viewRequest(() -> showQueueInfo(message.getInfo()));
    }


    /**
     * Parses a registration reply message from the server; if failed
     * shows the reason and asks again to choose an username and a color
     *
     * @param message message containing the information
     */
    private void parseRegistrationReply(FlagMessage message) {
        if (state == GameState.LOGIN) {
            if (message.getFlag()) {
                username = message.getInfo();
                connection.setConnectionName(username);
                state = GameState.LOBBY;
            } else inputRequest(this::requestLogin);
        }
    }


    /**
     * Parses a selection update message; if the challenger has to select
     * more gods queues a View update
     *
     * @param message message with information regarding the challenger
     */
    private void parseChallengerSelection(Message message) {
        gods = new HashMap<>();
        challenger = message.getInfo();
        if (message.getInfo().equals(username) && chosenGods.size() != players.size())
            inputRequest(() -> requestChallengerGod(new ArrayList<>(chosenGods)));
        else
            viewRequest(() -> updateChallengerGodSelection(new ArrayList<>(chosenGods)));
    }

    /**
     * Checks if a given god is valid, if so notifies the server of the player's choice
     *
     * @param requestedGod specified god
     * @return {@code true} if the god is valid
     */
    public boolean validateGods(String requestedGod) {
        if (!getStringAvailableGods().contains(requestedGod)) return false;
        sendMessage(new FlagMessage(MessageType.GODS_UPDATE, username, requestedGod, true, RECIPIENT));
        return true;
    }

    /**
     * Parses a gods update message; updates the View on the selected gods
     *
     * @param message message received
     */
    private void parseManagementMessage(FlagMessage message) {
        if (message.getFlag()) addGod(message);
        else removeGod(message);
    }

    /**
     * Adds the god contained in the message given to the list of chosen ones; then
     * queues a View update
     *
     * @param message message containing the chosen god
     */
    private void addGod(Message message) {
        chosenGods.add(message.getInfo());
        if (chosenGods.size() != players.size()) {
            if (challenger.equals(username))
                inputRequest(() -> requestChallengerGod(new ArrayList<>(chosenGods)));
            else
                viewRequest(() -> updateChallengerGodSelection(new ArrayList<>(chosenGods)));
        }
    }

    /**
     * Parses a god selection update message, adds the selected god
     * to the map of players; queues a View update
     *
     * @param message message to display
     */
    private void parseGodPlayerChoice(Message message) {
        gods.put(message.getInfo(), "");
        if (username.equals(message.getInfo()))
            inputRequest(() -> requestPlayerGod(new ArrayList<>(chosenGods), new HashMap<>(gods)));
        else
            viewRequest(() -> updatePlayerGodSelection(message.getInfo(), new HashMap<>(gods), new ArrayList<>(chosenGods)));
    }

    /**
     * Checks if a given god is valid, if so sends to the server a god selection update containing the god
     *
     * @param requestedGod selected god
     * @return {@code true} if the given god is among the ones which were chosen by the challenger
     */
    public boolean validatePlayerGodChoice(String requestedGod) {
        if (!chosenGods.contains(requestedGod) || isConnectionLost()) return false;
        sendMessage(new Message(MessageType.GODS_SELECTION_UPDATE, username, requestedGod, RECIPIENT));
        return true;
    }

    /**
     * Removes a god from the chosen gods list; if the list becomes empty
     * queues a View request update to the challenger client, otherwise
     * updates any client
     *
     * @param message message containing the name of a god
     */
    private void removeGod(Message message) {
        chosenGods.remove(message.getInfo());
        for (Map.Entry<String, String> e : gods.entrySet()) {
            if (e.getValue().equals(""))
                gods.replace(e.getKey(), message.getInfo());
        }
        if (chosenGods.isEmpty()) {
            if (challenger.equals(username))
                inputRequest(() -> requestStarterPlayer(new HashMap<>(gods)));
            else
                viewRequest(() -> updateStarterPlayerSelection(new HashMap<>(gods)));
        }
    }

    /**
     * Checks if the given name is the name of a player; if so sends a starter player
     * message to the server
     *
     * @param string could be the name of a player
     * @return {@code true} if the given string matches with one of the players' names
     */
    public boolean validatePlayer(String string) {
        if (players == null || !players.containsKey(string)) return false;
        sendMessage(new Message(MessageType.STARTER_PLAYER, username, string, RECIPIENT));
        return true;
    }

    /**
     * Parses a turn update message. Queues an update to the View, if the current player
     * is the client owner requests to execute an action
     *
     * @param message message containing the current player and the possbile action it can carry out
     */
    private void parseTurnUpdate(ActionUpdateMessage message) {
        List<String> toRemove = players.keySet().stream().filter(s -> !gods.containsKey(s)).collect(Collectors.toList());
        toRemove.forEach(s -> players.remove(s));
        if (username.equals(message.getInfo()))
            inputRequest(() -> requestTurnAction(message.getPossibleActions(), message.getGameUpdate(), players, gods, message.getFlag()));
        else
            viewRequest(() -> showBoard(message.getGameUpdate(), players, gods, message.getInfo()));
    }

    /**
     * Checks if a given value is between 0 and a second given value
     *
     * @param range the upper range
     * @param value the value to check
     * @return {@code true} if the value is positive and minor than the other parameter
     */
    public boolean validateRange(int range, int value) {
        return value >= 0 && value <= range;
    }


    /**
     * Check if an action is allowed, if so sends an action request message to the server
     *
     * @param action chosen action
     * @param possibleActions map containing the possible actions that can be carried out by the player and their positions
     * @param position chosen position
     * @return {@code true} if the action is allowed
     */
    public boolean validateAction(Action action, DtoPosition position, Map<Action, List<DtoPosition>> possibleActions) {
        if (connection != null && possibleActions.containsKey(action) && (Action.getNullPosActions().contains(action) || possibleActions.get(action).stream().anyMatch(p -> p.isSameAs(position)))) {
            sendMessage(new ActionMessage(username, "Action request", action, position, RECIPIENT));
            return true;
        }
        return false;
    }

    /**
     * Checks if the given position identified by its coordinates is contained in the given list of positions
     *
     * @param possiblePositions list of positions
     * @param x int that indicates the x coordinate
     * @param y int that indicates the y coordinate
     * @return {@code true} if the action is allowed in the that position
     */
    public boolean validatePosition(List<DtoPosition> possiblePositions, int x, int y) {
        return possiblePositions.stream().anyMatch(p -> p.getX() == x && p.getY() == y);
    }

    /**
     * Parses a game state update message, changes the client game state accordingly
     *
     * @param message message containing the game state
     */
    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        if (state == GameState.GOD_SELECTION) connection.setHasToReconnect(true);
    }

    /**
     * Sends a message to the server
     *
     * @param message the message to send
     */
    public void sendMessage(Message message) {
        if (connection != null) {
            connection.sendMessage(message);
            log.info(() -> "[CLIENT] Sent message " + message);
        }
    }

    /**
     * Getter fot the list of available gods as string
     *
     * @return a list of gods as string
     */
    private List<String> getStringAvailableGods() {
        return getAvailableGods().stream().map(Enum::toString).collect(Collectors.toList());
    }

    /**
     * Getter fot the list of available gods
     *
     * @return a list of gods
     */
    private List<Gods> getAvailableGods() {
        return Arrays.stream(Gods.values()).filter(god -> !chosenGods.contains(god.toString())).collect(Collectors.toList());
    }

    /**
     * Handles the disconnection of the client
     */
    protected void disconnectClient() {
        if (connection != null) {
            connection.setHasToReconnect(false);
            connection.disconnect();
        }
    }

    /**
     * Clears all the variable of the client
     */
    private void clearSessionVars() {
        connection = null;
        challenger = null;
        players = null;
        gods = null;
        username = null;
        state = null;
    }

    /**
     * Closes the game
     */
    protected void closeGame() {
        disconnectClient();
        init();
    }

    /**
     * Checks if the client is connected to the server
     *
     * @return {@code true} if the client is connected
     */
    protected boolean isConnectionLost() {
        return !reconnecting && (connection == null) || (connection.isDisconnected());
    }

}