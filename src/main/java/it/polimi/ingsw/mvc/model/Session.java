package it.polimi.ingsw.mvc.model;

import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.EnemyRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Session of the game containing the {@link java.util.List list} of the {@link Player players},
 * the {@link Board board} and the {@link java.util.List list} of the {@link Gods gods}
 */
public class Session implements Serializable {

    private static final long serialVersionUID = 3969372125515778369L;
    private static Session instance;
    private final ArrayList<Player> players = new ArrayList<>();
    private final Board board = new Board();
    private final List<EnemyRules> enemyModifiers = new ArrayList<>();
    private boolean isStarted;

    /**
     * Can't be instantiated, singleton pattern implemented
     */
    private Session() {
        super();
    }

    /**
     * Method to set the singleton at the reload
     * @param session the session passed
     */
    public static synchronized void loadInstance(Session session) {
        instance = session;
    }

    /**
     * Method that creates a unique instance of Session (Singleton)
     *
     * @return the {@link Session session} with all the game information
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Evaluates if the game started
     *
     * @return {@code true} if is started
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Setter for started flag
     *
     * @param isStarted specific if the game is started
     */
    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    /**
     * Adds a {@link Player player} to the list of {@link Player players}
     *
     * @param player identifies the {@link Player player}
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Adds a {@link Player player} to the list of {@link Player players}
     *
     * @param username is the {@link Player player}'s name
     * @param color    the color associated to the player
     */
    public void addPlayer(String username, Colors color) {
        players.add(new Player(username, color));
    }

    /**
     * Removes the {@link Player player} and his {@link Worker workers} from the game
     *
     * @param player identifies the {@link Player player} to remove
     */
    public void removePlayer(Player player) {
        removeWorkers(player);
        players.removeIf(p -> p == player);
    }

    /**
     * Removes the {@link Player player} and his {@link Worker workers} from the game
     *
     * @param username name of the {@link Player player} to remove
     */
    public void removePlayer(String username) {
        removePlayer(getPlayerByName(username));
    }

    /**
     * Gets a {@link Player player} by username
     *
     * @param username is the {@link Player player}'s name
     * @return the player, or null if not found
     */
    public Player getPlayerByName(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Getter for the color of a specific player in the game
     *
     * @param username the name of the player
     * @return the {@link Colors color} of that player
     */
    public Colors getPlayerColor(String username) {
        return getPlayerByName(username).getColor();
    }

    /**
     * Getter for the list of {@link Player players}
     *
     * @return a shallow copy of the {@link Player players} list
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * Removes the {@link Worker workers} of a {@link Player player} from the game
     *
     * @param player identifies the {@link Worker workers} master
     */
    private void removeWorkers(Player player) {
        while (!player.getWorkers().isEmpty()) {
            player.removeWorker(player.getWorkers().size() - 1);
        }
    }

    /**
     * Getter for the list of the {@link Player players} without the passed player
     *
     * @param player the {@link Player player} that you don't need to have in the list
     * @return a shallow copy of the {@link Player players}'s list and removes the {@link Player player} in the argument
     */
    public List<Player> getOtherPlayers(Player player) {
        List<Player> list = getPlayers();
        list.remove(player);
        return list;
    }

    /**
     * Getter for the size of the list of the {@link Player players}
     *
     * @return the {@link int number} of the players in the game
     */
    public int playersNumber() {
        return players.size();
    }

    /**
     * Getter for the {@link Board board}
     *
     * @return the {@link Board board} used in the session
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Evaluates if there is a winner in the {@link Player players}'s list
     *
     * @return {@code true} if there is a winner
     */
    public boolean hasWinner() {
        for (Player player : players) {
            if (player.isStarter()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Randomly chooses the challenger from the {@link Player players}
     *
     * @return the name of a player
     */
    public String getChallenger() {
        for (Player player : players) {
            if (player.isChallenger()) {
                return player.getUsername();
            }
        }
        int challengerNumber = (new Random().nextInt(playersNumber()));
        Player p = players.get(challengerNumber);
        p.setChallenger();
        return p.getUsername();
    }

    /**
     * Getter for the list of {@link EnemyRules EnemyRules} in game
     *
     * @return the list of rules
     */
    public List<EnemyRules> getEnemyModifiers() {
        return enemyModifiers;
    }

    /**
     * Clear method to refresh the Session
     */
    public void clear() {
        players.clear();
        enemyModifiers.clear();
        board.clear();
        isStarted = false;
    }

}


