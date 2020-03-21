package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;



public class Session {
    private static ArrayList<Player> players = new ArrayList<>();
    private static Board board;
    private static ArrayList<Gods> godsList;

    public static void addPlayer(Player player) {
        players.add(player);
    }

    // Returns shallow copy of players
    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) players.clone();
    }

    public static void removePlayer(Player player) {
        for(Player localPlayer : players) {
            if(localPlayer.equals(player)) {
                players.remove(localPlayer);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<Player> getOtherPlayers(Player player) {
        ArrayList<Player> list = (ArrayList<Player>) players.clone();
        list.remove(player);
        return list;
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
        for(Gods localGod : godsList ) {
            if(localGod.equals(god)) {
                godsList.remove(localGod);
            }
        }
    }

    public static void startLobby() {
        //da riempire quando avremo controller
    }

    public static boolean hasWinner(){
            for(Player player : players) {
                    if(player.isWinner()) {
                        return true;
                    }
                }
            return false;
        }



}



