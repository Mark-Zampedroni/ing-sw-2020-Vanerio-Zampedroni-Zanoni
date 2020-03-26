package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
//import java.util.stream.Collectors;


public class Session {
    private static ArrayList<Player> players = new ArrayList<>();
    private static Board board = new Board();
    private static ArrayList<Gods> godsList= new ArrayList<>();

    public static void addPlayer(Player player) {
        players.add(player);
    }

    // Returns shallow copy of players
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }

    public static void removePlayer(Player player) {
        for(Iterator<Player> playerIterator = players.iterator(); playerIterator.hasNext();){
            Player playerIt = playerIterator.next();
            if (playerIt.equals(player)) {
                playerIterator.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> list = (ArrayList<Player>) players.clone();
        list.remove(player);
        return list;
    }

    public static int playersNumber() {
        return players.size();
    }

    public static void addGod(Gods gods) {
        godsList.add(gods);
    }

    // Returns shallow copy of gods
    @SuppressWarnings("unchecked")
    public static ArrayList<Gods> getGods() {
        return (ArrayList<Gods>) godsList.clone();
    }

    public static void removeGod(Gods god) {
        for (Gods localGod : godsList) {
            if (localGod.equals(god)) {
                godsList.remove(localGod);
            }
        }
    }

    public static void startLobby() {
        //da riempire quando avremo controller
    }

    public static boolean hasWinner() {
        for (Player player : players) {
            if (player.isWinner()) {
                return true;
            }
        }
        return false;
    }

    public static void pickChallenger() {
        int firstTwoPlayers = (new Random().nextInt(1));
        int firstThreePlayers = (new Random().nextInt(2));
        if (playersNumber() == 2) players.get(firstTwoPlayers).setChallenger();
        if (playersNumber() == 3) players.get(firstThreePlayers).setChallenger();
    }
 /*
    @Override
    public String toString() {
        String space = ", ";
        String out = players.stream()
                .map(Object::toString)
                .collect(Collectors.joining(space));

        String out2 = godsList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(space));
        return "Players: " + out
                + "Gods: " + out2 +
                board.toString();
    }*/

    public static Board getBoard() {
        return board;
    }
}


