package it.polimi.ingsw.mvc.model;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.mvc.model.map.Board;
import it.polimi.ingsw.mvc.model.player.Player;
import it.polimi.ingsw.mvc.model.player.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Session of the game containing the {@link java.util.List list} of the {@link Player players},
 * the {@link Board board} and the {@link java.util.List list} of the {@link Gods gods}
 */
public class Session implements Serializable {
    private static final ArrayList<Player> players = new ArrayList<>();
    private static final Board board = new Board();
    private static final long serialVersionUID = 3969372125515778369L;
    private static boolean started;
    private static Session instance;

    /**
     * Singleton instance
     */
    private Session() {
        super();
    }

    /**
     * Method that create a unique instance of Session
     *
     * @return the {@link Session session} with all the game informations
     */
    public static Session getInstance() {
        if(instance == null) {
            instance = new Session();
        }
        return instance;
    }

    /**
     * Evaluates if the match is started
     *
     * @return {@code true} if is started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * Setter for started flag
     *
     * @param state specific if the game is started
     */
    public void setStarted(boolean state) {
        started=state;
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
     * Get {@link Player player} by username
     *
     * @param username is the {@link Player player}'s name
     * @return the player, or null if not found
     */
    public Player getPlayerByName(String username) {
        for(Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public Colors getPlayerColor(String username) {
        return getPlayerByName(username).getColor();
    }

    /**
     * Getter for the list of the {@link Player players}
     *
     * @return a shallow copy of the {@link Player players} list
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }

    /**
     * Removes the {@link Worker workers} of a {@link Player player} from the game
     *
     * @param player identifies the {@link Worker workers} master
     */
    private void removeWorkers(Player player) {
        while(player.getWorkers().size() != 0) {
            player.removeWorker(player.getWorkers().size()-1);
        }
    }

    /**
     * Getter for the list of the {@link Player players} without the passed player
     *
     * @param player the {@link Player player} that you don't need to have in the list
     * @return a shallow copy of the {@link Player players}'s list and removes the {@link Player player} in the argument
     */
    public ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> list = getPlayers();
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
            if (player.isStarter()) { return true; }
        }
        return false;
    }

    /**
     * Randomly chooses the challenger from the {@link Player players}
     */
    public String getChallenger() {
        for (Player player : players) {
            if(player.isChallenger()){
                return player.getUsername();
            }
        }
        int challengerNumber = (new Random().nextInt(playersNumber()));
        Player p = players.get(challengerNumber);
        p.setChallenger();
        return p.getUsername();
    }

}

