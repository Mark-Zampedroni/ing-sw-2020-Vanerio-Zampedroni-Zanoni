package it.polimi.ingsw.utility.serialization.DTO;

import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;
import it.polimi.ingsw.MVC.model.player.Player;
import it.polimi.ingsw.MVC.model.player.Worker;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO copy of the class {@link Player player}
 */

public class DTOplayer {
    private String username;
    //private GodRules rules;
    private ArrayList<DTOworker> workers;
    private Colors color;
    private Gods god;
    private boolean challenger;
    private boolean winner;

    /**
     * Initializes the DTOplayer
     *
     * @param player indicates his equivalent in server storage
     */
    public DTOplayer(Player player) {
        this.username = player.getUsername();
        workers = new ArrayList<>();
        for(Worker p : player.getWorkers()) {
            DTOworker dtOworker = new DTOworker(p);
            workers.add(dtOworker);
        }
        this.color = player.getColor();
        this.challenger=player.isChallenger();
        this.winner=player.isWinner();
        this.god=player.getGod();
    }

    /**
     * Getter for color
     *
     * @return username the {@link Colors color} which represents the player
     */
    public Colors getColor() { return color; }

    /**
     * Getter for username
     *
     * @return username the {@link String string} which represents the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Getter for god
     *
     * @return the {@link Gods god} of the DTOplayer
     */
    public Gods getGod() {
        return god;
    }

    /**
     * Getter for DTOworkers
     *
     * @return a shallow copy of the list which contains the DTOworkers {@link DTOworker DTOworkers}
     */
    public List<DTOworker> getWorkers() {
        return new ArrayList<>(workers);
    }

    /**
     * Getter for DTOworkers list size
     *
     * @return the number of {@link DTOworker DTOworkers} the player has
     */
    public int getWorkersSize() { return workers.size(); }

    /**
     * Evaluates if the DTOplayer is the challenger
     *
     * @return {@code true} if the DTOplayer is the current challenger
     */
    public boolean isChallenger(){
        return challenger;
    }

    /**
     * Evaluates if the DTOplayer is the winner
     *
     * @return {@code true} if the DTOplayer is the winner of the {@link it.polimi.ingsw.MVC.model.Session session}
     */
    public boolean isWinner() {
        return winner;
    }

    /**
     * Creates a String that contains the DTOplayer most important values
     *
     * @return a String with the player most important attributes
     */
    @Override
    public String toString() {
        return "{Username: "+this.username
                +", Color: "+ color
                +", God: " + god
                +"}";
    }

    //Non dovrebbero servire le godrules a lato client
    /*public GodRules getRules() {
       return rules;
    }*/

}
