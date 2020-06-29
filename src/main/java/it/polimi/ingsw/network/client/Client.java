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
     * Output thread
     *
     * @param request the thread
     */
    private void viewRequest(Runnable request) {
        requests.add(request);
    }

    /**
     * Input thread
     *
     * @param request the thread
     */
    private void inputRequest(Runnable request) {
        inputRequests.add(request);
    }

    public void flushRequests() {
        requests.forEach(Runnable::run);
        requests.clear();
        inputRequests.forEach(r -> new Thread(r).start());
        inputRequests.clear();
    }

    /**
     * Creates a connection for the client
     *
     * @param ip server ip
     * @param port connection port
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
     * Calls a specific method based on the message type
     *
     * @param message received message
     */
    public void update(Message message) {
        if ((message.getType() == MessageType.CONNECTION_TOKEN && state == GameState.CONNECTION) ||
                message.getRecipient().equals(username) ||
                message.getRecipient().equals("ALL")) {
            log.info(() -> "[CLIENT] Received message " + message);
            switch (message.getType()) {
                case CONNECTION_TOKEN: // CONNECTION
                    if (state == GameState.CONNECTION) parseConnectionMessage(message);
                    break;
                case SLOTS_UPDATE: // PRE-LOBBY
                    if (state == GameState.PRE_LOBBY) parseSlotMessage();
                    break;
                case INFO_UPDATE: // PRE-LOBBY
                    parseInfoMessage(message);
                    break;
                case DISCONNECTION_UPDATE: // PRE-LOBBY / LOBBY (tutte)
                    parseDisconnectMessage(message);
                    break;
                case REGISTRATION_UPDATE: // LOBBY (pendente)
                    parseRegistrationReply((FlagMessage) message);
                    break;
                case LOBBY_UPDATE: // LOBBY (tutte)
                    parseLobbyUpdate((LobbyUpdate) message);
                    break;
                case STATE_UPDATE: // TUTTE
                    parseStateUpdate((StateUpdateMessage) message);
                    break;
                case SELECTION_UPDATE:
                    parseChallengerSelection(message);
                    break;
                case GODS_UPDATE: // GOD_SELECTION
                    parseManagementMessage((FlagMessage) message);
                    break;
                case GODS_SELECTION_UPDATE: // GOD_SELECTION
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
                    if (connection.getReconnect()) {
                        parseReconnectionUpdate();
                    }
                default: //
            }
            flushRequests();
        }
    }

    
    /**
     * Handles a winning or loosing message
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
                connection.setReconnect(false);
            }
            viewRequest(() -> showLose(message.getInfo()));
        }

    }

    /**
     * Handles a connection message
     *
     * @param message received message
     */
    private void parseConnectionMessage(Message message) {
        username = message.getInfo();
        state = GameState.PRE_LOBBY;
    }

    /**
     * Manages the reconnection of a client
     */
    private void parseReconnectionUpdate() {
        reconnecting = true;
        viewRequest(() -> showReconnection(true));
    }

    /**
     * Handles a reconnection message
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
     * Allows a client to choose between 2 or 3 players
     */
    private void parseSlotMessage() { // TEST CLI
        viewRequest(this::requestNumberOfPlayers);
    }

    /**
     * Checks if the given number of player is valid
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
     * Checks if a given name is allowed
     *
     * @param requestedUsername name of the player
     * @return {@code true} if it's valid
     */
    public boolean validateUsername(String requestedUsername) {
        return (connection != null) && (players.containsKey(requestedUsername) || requestedUsername.length() <= 0 || requestedUsername.length() >= 12 || isNumeric(requestedUsername));
    }

    /**
     * Checks if a given string can be turn into a number
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
     * Checks if a given colors is valid
     *
     * @param requestedColor chosen color
     *@return {@code true} if it's valid
     */
    public boolean validateColor(String requestedColor) {
        return !Colors.isValid(requestedColor) || players.containsValue(Colors.valueOf(requestedColor));
    }


    /**
     * Interacts with the server to pass on the player's choices
     *
     * @param requestedUsername selected username
     * @param color chosen color
     */
    public void requestLogin(String requestedUsername, Colors color) {
        sendMessage(new RegistrationMessage(username, requestedUsername, color, RECIPIENT));
    }


    /**
     * Handles a message from the server allowing a player to choose its username and its color
     *
     * @param message received message
     */
    private void parseLobbyUpdate(LobbyUpdate message) {
        players = message.getPlayers();
        if (state == GameState.PRE_LOBBY) {
            state = GameState.LOGIN;
            inputRequest(this::requestLogin);
        }
        if (state == GameState.LOGIN || state == GameState.LOBBY) {
            viewRequest(() -> showLobby(message.getColors()));
        }
    }

    /**
     * Handles a disconnection message from the server
     *
     * @param message message to display
     */
    private void parseDisconnectMessage(Message message) {
        if (state != GameState.PRE_LOBBY) closeGame();
        if (connection != null) connection.setDisconnected();
        showDisconnected(message.getInfo());
    }


    /**
     * Handles a message regarding the queue of players
     *
     * @param message message to display
     */
    private void parseInfoMessage(Message message) {
        viewRequest(() -> showQueueInfo(message.getInfo()));
    }


    /**
     * Handles a registration message from the server
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
     * Sets the challenger and allows it to choose a god
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
     * Checks if a given god is valid, if so it notifies the server of the player's choice
     *
     * @param requestedGod targeted god
     * @return {@code true} if the god is valid
     */
    public boolean validateGods(String requestedGod) {
        if (!getStringAvailableGods().contains(requestedGod)) return false;
        sendMessage(new FlagMessage(MessageType.GODS_UPDATE, username, requestedGod, true, RECIPIENT));
        return true;
    }

    /**
     * Calls a method in regard of the received message
     *
     * @param message message to forward
     */
    private void parseManagementMessage(FlagMessage message) {
        if (message.getFlag()) addGod(message);
        else removeGod(message);
    }

    /**
     * Adds a god to the list of chosen ones and could allow the challenger to choose a new god
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
     * Allows players to chose their gods and also creates a map containing the name of the players and their choices regarding the gods
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
     * Checks if a given god is valid, if so it notifies the server of the player's choice
     *
     * @param requestedGod targeted god
     * @return {@code true} if the given god is among the ones which were chosen by the challenger
     */
    public boolean validatePlayerGodChoice(String requestedGod) {
        if (!chosenGods.contains(requestedGod)) return false;
        sendMessage(new Message(MessageType.GODS_SELECTION_UPDATE, username, requestedGod, RECIPIENT));
        return true;
    }

    /**
     * Removes a god from a list and allows the challenger to choose the starting player
     *
     * @param message message with the name of a god
     */
    private void removeGod(Message message) {
        chosenGods.remove(message.getInfo());
        for (Map.Entry<String, String> e : gods.entrySet()) {
            if (e.getValue().equals("")) {
                gods.replace(e.getKey(), message.getInfo());
            }
        }
        if (chosenGods.isEmpty()) {
            if (challenger.equals(username))
                inputRequest(() -> requestStarterPlayer(new HashMap<>(gods)));
            else
                viewRequest(() -> updateStarterPlayerSelection(new HashMap<>(gods)));
        }
    }

    /**
     * Notifies the server of the starting player if the given name is valid
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
     * Updates a client on the changes occurred in the previous turn and also allows the turn owner to play
     *
     * @param message message containing the current player and the possbile action it can carry out
     */
    private void parseTurnUpdate(ActionUpdateMessage message) {
        if (username.equals(message.getInfo()))
            inputRequest(() -> requestTurnAction(message.getPossibleActions(), message.getGameUpdate(), players, gods, message.getFlag()));
        else
            viewRequest(() -> showBoard(message.getGameUpdate(), players, gods, message.getInfo()));
    }

    /**
     * Checks if a parameter is between 0 and a given number
     *
     * @param range the upper range
     * @param value the value to check
     * @return {@code true} if the value is positive and minor of and other parameter
     */
    public boolean validateRange(int range, int value) {
        return value >= 0 && value <= range;
    }


    /**
     * Check if an action is allowed, if so notifies the server
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
     * Checks if it's possible to carry out an action in a position defined by two given parameters
     *
     * @param possiblePositions list of possible positions
     * @param x int that indicates the x coordinate for a wanted action
     * @param y int that indicates the y coordinate for a wanted action
     * @return {@code true} if the action is allowed in the that position
     */
    public boolean validatePosition(List<DtoPosition> possiblePositions, int x, int y) {
        return possiblePositions.stream().anyMatch(p -> p.getX() == x && p.getY() == y);
    }

    /**
     * Changes the client game state accordingly to the message
     *
     * @param message message containing the next game state
     */
    private void parseStateUpdate(StateUpdateMessage message) {
        state = message.getState();
        if (state == GameState.GOD_SELECTION) connection.setReconnect(true);
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
     * Handles the disconnection of a client
     */
    protected void disconnectClient() {
        if (connection != null) {
            connection.setReconnect(false);
            connection.disconnect();
        }
    }

    /**
     * Clears all the variable of the game
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

    protected boolean isConnectionLost() {
        System.out.println("isConnectionLost = " + ((connection == null) || (connection.isDisconnected())));
        return !reconnecting && (connection == null) || (connection.isDisconnected());
    }

}