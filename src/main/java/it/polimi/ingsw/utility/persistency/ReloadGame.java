package it.polimi.ingsw.utility.persistency;

import it.polimi.ingsw.ServerApp;
import it.polimi.ingsw.mvc.controller.SessionController;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.view.RemoteView;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.server.ServerConnection;
import it.polimi.ingsw.utility.dto.DtoSession;
import it.polimi.ingsw.utility.enumerations.GameState;
import it.polimi.ingsw.utility.exceptions.utility.NotInstantiableClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Reloads the game from a file after a failure of the server
 */
public class ReloadGame {

    private static SaveData saveData;
    private static boolean isAlreadyLoaded;

    /**
     * This class is not instantiable
     *
     * @throws NotInstantiableClass when instantiated
     */
    private ReloadGame() throws NotInstantiableClass {
        throw new NotInstantiableClass();
    }

    /**
     * Reads from the saved date the names of the players
     *
     * @return a list containing the names of the players
     */
    public static List<String> getInGamePlayersNames() {
        return saveData.getSession().getPlayers().stream()
                .filter(p -> !p.isLoser())
                .map(Player::getUsername).collect(Collectors.toList());
    }

    /**
     * Deserialize the file and loads the information in a specific class
     *
     * @return {@code true} if there aren't problems during the deserialization
     */
    private static boolean deserializeFile() {
        if (ServerApp.isFeature()) {
            String filename = "santorini.game.ser";
            try (
                    FileInputStream file = new FileInputStream(filename);
                    ObjectInputStream input = new ObjectInputStream(file)
            ) {
                ReloadGame.saveData = (SaveData) input.readObject();
                isAlreadyLoaded = true;
            } catch (IOException | ClassNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Getter for the saved data
     *
     * @return the class containing all the information of the previous game
     */
    public static SaveData load() {
        return saveData;
    }

    /**
     * Sets to false the flag that indicates if the game is already loaded or not
     */
    public static void setFinishedLoad() {
        isAlreadyLoaded = false;
    }

    /**
     * Creates new views for the players of the saved game
     *
     * @param state the state of the game
     * @param map contains the connections of the players
     * @param views the list containing the views of the players
     * @param controller the controller of the session
     */
    public static void reloadViews(SessionController controller, Map<String, ServerConnection> map, List<RemoteView> views, GameState state) {
        for (Map.Entry<String, ServerConnection> e : map.entrySet()) {
            e.getValue().putInLobby();
            RemoteView view = new RemoteView(e.getValue());
            view.register(e.getKey());
            views.add(view);
            if (state == GameState.GAME) {
                view.getFirstDTOSession(new DtoSession(saveData.getSession()));
            }
            view.addObserver(controller);
            saveData.getSession().getPlayers().stream()
                    .filter(p -> p.getRules() != null)
                    .forEach(p -> p.getRules().addObserver(view));
        }
    }

    /**
     * Accepts the client that play in the previous game and denies connection to the others
     *
     * @param log logger of the server
     * @param reconnecting contains the connections and the associated key
     * @param message the message sent by client
     * @param connection Mark will fill it
     * @param sessionController the controller of the session
     */
    public static SessionController reloadConnection(SessionController sessionController, Map<String, ServerConnection> reconnecting, ServerConnection connection, Logger log, Message message) {
        SessionController newController = null;
        if (!sessionController.isGameStarted() && ReloadGame.isRestartable()) {
            List<String> previousPlayers = ReloadGame.getInGamePlayersNames();
            if (previousPlayers.contains(message.getSender()) && !reconnecting.containsKey(message.getSender())) {
                newController = openIfFull(reconnecting, log, previousPlayers, connection, message);
            } else {
                connection.denyReconnection(message.getSender(), "You are not a player of the game being loaded");
                log.info(() -> "Unknown player " + message.getInfo() + " tried to reconnect to previous game");
            }
        } else {
            connection.denyReconnection(message.getSender(), "No game files that can be loaded exist");
            log.info(() -> "Player " + message.getInfo() + " tried to reconnect but no saved game exists");
        }
        return newController;
    }

    /**
     * Opens the game if all the player in previous game reconnect
     *
     * @param log logger of the server
     * @param reconnecting contains the connections and the associated key
     * @param message the message sent by client
     * @param connection Mark will fill it
     * @param previousPlayers list of the players previously in game
     *
     * @return the session controller of the new game
     */
    private static SessionController openIfFull(Map<String, ServerConnection> reconnecting, Logger log, List<String> previousPlayers, ServerConnection connection, Message message) {
        SessionController newController = null;
        log.info(() -> "Player " + message.getSender() + " asked to reconnect to the previous game");
        reconnecting.put(message.getSender(), connection);
        if (reconnecting.size() == previousPlayers.size()) {
            newController = new SessionController(log, ReloadGame.load(), reconnecting);
            ReloadGame.setFinishedLoad();
        }
        return newController;
    }

    /**
     * Checks if the game is restartable
     *
     * @return {@code true} if is restartable
     */
    public static boolean isRestartable() {
        return isAlreadyLoaded || deserializeFile();
    }

    /**
     * Deletes the old backup file from the memory
     */
    public static void clearSavedFile() throws IOException {
        Files.deleteIfExists(Paths.get("santorini.game.ser"));
    }

}
