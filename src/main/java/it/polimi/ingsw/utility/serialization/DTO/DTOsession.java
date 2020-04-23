package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;

import java.util.ArrayList;

/**
 * DTO copy of the class {@link Session Session}
 */
public class DTOsession {

    private static ArrayList<DTOplayer> players = new ArrayList<>();
    private static DTOboard board;
    private static ArrayList<Gods> godsList= new ArrayList<>();
    private static boolean started;
    private static DTOsession instance;

    /**
     * Singleton instance
     */
    private DTOsession() {
        super();
    }

    /**
     * Method that returns the unique instance of Session
     *
     * @return the {@link DTOsession DTOsession} with all the game informations
     */
    public static DTOsession getInstance() {
        return instance;
    }
    /**
     * Method that create a unique instance of DTOsession
     *
     * @return the {@link DTOsession DTOsession} with all the game informations
     * @param session indicates his equivalent in server storage
     */
    public DTOsession (Session session) {
        for(Player p : session.getPlayers()) {
            DTOplayer dtOplayer = new DTOplayer(p);
            players.add(dtOplayer);
            }
        godsList = new ArrayList<>(session.getGods());
        board = new DTOboard(session.getBoard());
        started=session.isStarted();
        instance=this;
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
     * Get {@link DTOplayer DTOplayer} by username
     *
     * @param username is the {@link Player player}'s name
     * @return the {@link DTOplayer DTOplayer}, or null if not found
     */
    public DTOplayer getPlayerByName(String username) {
        for(DTOplayer p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

     /** Getter for the color of a specific {@link DTOplayer player}
     *
      * @param username username of the player
      * @return the {@link Colors color} of the player
     */
    public Colors getPlayerColor(String username) {
        return getPlayerByName(username).getColor();
    }

    /**
     * Getter for the list of the {@link DTOplayer DTOplayers}
     *
     * @return a shallow copy of the {@link DTOplayer players} list
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DTOplayer> getPlayers() {
        return (ArrayList<DTOplayer>) players.clone();
    }


    /**
     * Getter for the list of the {@link DTOplayer DTOplayers} without the passed player
     *
     * @param player the {@link DTOplayer DTOplayer} that you don't need to have in the list
     * @return a shallow copy of the {@link DTOplayer players}'s list and removes the {@link DTOplayer player} in the argument
     */
    public ArrayList<DTOplayer> getOtherPlayers(DTOplayer player) {
        ArrayList<DTOplayer> list = getPlayers();
        list.remove(player);
        return list;
    }

    /**
     * Getter for the size of the list of the {@link DTOplayer DTOplayers}
     *
     * @return the {@link int number} of the players in the game
     */
    public int playersNumber() {
        return players.size();
    }

    /**
     * Getter for the {@link DTOboard DTOboard}
     *
     * @return the {@link DTOboard board} used in the session
     */
    public DTOboard getBoard() {
        return board;
    }

    /**
     * Getter for the list of the {@link Gods gods}
     *
     * @return a shallow copy of the {@link Gods gods}'s list
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Gods> getGods() {
        return (ArrayList<Gods>) godsList.clone();
    }

    /**
     * Evaluates if there is a winner in the {@link DTOplayer DTOplayers}'s list
     *
     * @return {@code true} if there is a winner
     */
    public boolean hasWinner() {
        for (DTOplayer player : players) {
            if (player.isWinner()) { return true; }
        }
        return false;
    }

    /**
     * Getter for the challenger of the session
     *
     * @return the name of the {@link DTOplayer DTOplayer} that is the challenger
     */
    public String getChallenger() {
        for (DTOplayer player : players) {
            if(player.isChallenger()){
                return player.getUsername();
            }
        } return null;
    }
}
