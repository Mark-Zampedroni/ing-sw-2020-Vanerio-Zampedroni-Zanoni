package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;
import java.util.Random;

/**
 * Session of the game containing the {@link java.util.List list} of the {@link Player players},
 * the {@link Board board} and the {@link java.util.List list} of the {@link Gods gods}
 */
public class Session {
    private static ArrayList<Player> players = new ArrayList<>();
    private static Board board = new Board();
    private static ArrayList<Gods> godsList= new ArrayList<>();

    /**
     * Adds a {@link Player player} in the list of the {@link Player players}
     *
     * @param player identifies the {@link Player player}
     */
    public static void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Getter for the list of the {@link Player players}
     *
     * @return a shallow copy of the {@link Player players}'s list
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }

    /**
     * Removes the {@link Player player} and his {@link Worker workers} from the game
     *
     * @param player identifies the {@link Player player} you have to remove
     */
    public static void removePlayer(Player player) {
        int i=0;
        while (!players.get(i).equals(player)) {
            i++;
        }
        if (players.get(i).getWorkers().size() == 2) {
            players.get(i).removeWorker(1);
            players.get(i).removeWorker(0);} else {
            if (players.get(i).getWorkers().size() == 1) {
                players.get(i).removeWorker(0);
            }
        }
        players.removeIf(p -> p == player);
    }

    /**
     * Getter for the list of the {@link Player players} without the passed player
     *
     * @param player the {@link Player player} that you don't need to have in the list
     * @return a shallow copy of the {@link Player players}'s list and removes the {@link Player player} in the argument
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> list = (ArrayList<Player>) players.clone();
        list.remove(player);
        return list;
    }

    /**
     * Getter for the size of the list of the {@link Player players}
     *
     * @return the {@link int number} of the players in the game
     */
    public static int playersNumber() {
        return players.size();
    }

    /**
     * Getter for the {@link Board board}
     *
     * @return the {@link Board board} used in the session
     */
    public static Board getBoard() {
        return board;
    }

    /**
     * Adds a {@link Gods god} in the list of the {@link Gods gods}
     *
     * @param god identifies the {@link Gods god} you have to add
     */
    public static void addGod(Gods god) {
        godsList.add(god);
    }

    /**
     * Getter for the list of the {@link Gods gods}
     *
     * @return a shallow copy of the {@link Gods gods}'s list
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Gods> getGods() {
        return (ArrayList<Gods>) godsList.clone();
    }

    /**
     * Removes the {@link Gods god} from the god's game list
     *
     * @param god identifies the {@link Gods god} you have to remove
     */
    public static void removeGod(Gods god) {
        godsList.removeIf(elem -> elem.equals(god));
    }

    /**
     * Evaluates if there is a winner in the {@link Player players}'s list
     *
     * @return {@code true} if there is a winner
     */
    public static boolean hasWinner() {
        for (Player player : players) {
            if (player.isWinner()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Randomly chooses the challenger from the {@link Player players}
     */
    public static void pickChallenger() {
        int firstTwoPlayers = (new Random().nextInt(1));
        int firstThreePlayers = (new Random().nextInt(2));
        if (playersNumber() == 2) players.get(firstTwoPlayers).setChallenger();
        if (playersNumber() == 3) players.get(firstThreePlayers).setChallenger();
    }

}


