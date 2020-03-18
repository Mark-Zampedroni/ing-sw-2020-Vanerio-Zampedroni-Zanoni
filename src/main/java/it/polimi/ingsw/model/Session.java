package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;

public class Session {
    private static List<Player> players = new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    // Solo per test, non dovremmo far uscire i puntatori dei player (privati) da Session
    public static List<Player> getPlayers() {
        return players;
    }

}
