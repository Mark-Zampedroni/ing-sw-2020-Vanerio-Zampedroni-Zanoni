package it.polimi.ingsw.model;
import java.util.ArrayList;

// STEFANO

public class Session {
    private static ArrayList<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    // Returns shallow copy of players
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }



}
