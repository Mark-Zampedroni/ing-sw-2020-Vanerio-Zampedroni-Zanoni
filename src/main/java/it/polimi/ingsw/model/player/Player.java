package it.polimi.ingsw.model.player;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.rules.GodRules;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Used to represent a person (identified by his username) playing the game in a {@link it.polimi.ingsw.model.Session session}
 * with a unique {@link it.polimi.ingsw.enumerations.Colors color}, a chosen {@link it.polimi.ingsw.enumerations.Gods god}
 * and a list of {@link it.polimi.ingsw.model.player.Worker workers}
 */

public class Player implements Serializable {

    private String username;
    private GodRules rules;
    private ArrayList<Worker> workers;
    private Colors color;
    private Gods god;
    private boolean challenger;
    private boolean winner;

    /**
     * Initializes a player with his username, creating two {@link Worker workers}
     *
     * @param username unique string used to identify the player
     */
    public Player(String username) {
        this.username = username;
        this.workers= new ArrayList<>();
        this.workers.add(new Worker());
        this.workers.add(new Worker());
        this.color=Colors.BLUE;
    }

    /**
     * Setter for color
     *
     * @param color specific {@link Colors color} for the player assigned during the creation, never change during the match
     */
    public void setColor(Colors color) { this.color = color;}

    /**
     * Getter for username
     *
     * @return username the {@link String string} which represents the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * Setter for god
     *
     * @param god the {@link Gods god} chosen by the player during the creation, never change during the match
     */
    public void setGod(Gods god) {
        this.god = god;
    }

    /**
     * Getter for god
     *
     * @return the {@link Gods god} of the player
     */
    public Gods getGod() {
        return god;
    }

    /**
     * Setter for rules
     *
     * @param rules specific {@link GodRules rules} that are linked to the chosen {@link Gods god}
     */
    public void setRules(GodRules rules) {
        this.rules = rules;
    }

    /**
     * Getter for rules
     *
     * @return the {@link GodRules rules} of the player
     */
    public GodRules getRules() {
        return rules;
    }

    /**
     * Getter for workers
     *
     * @return a shallow copy of the list which contains the workers {@link Worker workers}
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Worker> getWorkers() {
        return (ArrayList<Worker>) workers.clone();
    }

    /**
     * Getter for workers list size
     *
     * @return the number of {@link Worker workers} the player has
     */
    public int getWorkersSize() { return workers.size(); }

    /**
     * Removes the {@link Worker worker} of a player in a specific {@link Position position}
     *
     * @param position specific {@link GodRules rules} that are linked to the chosen {@link Gods god}
     */
    public void removeWorker(Position position) {
        if (workers.get(0).getPosition().equals(position)) {
            workers.remove(0);
        } else {
            if (workers.get(1).getPosition().equals(position)) {
                workers.remove(1);
            }
        }
    }

    /**
     * Removes the {@link Worker worker} identified by his index
     *
     * @param index position of the {@link Worker worker} in the list in player
     */
    public void removeWorker(int index) {
            workers.remove(index);
    }

    /**
     * Evaluates if the player is the challenger
     *
     * @return {@code true} if the player is the current challenger
     */
    public boolean isChallenger(){
        return challenger;
    }

    /**
     * The current player becomes the challenger (first player)
     */
    public void setChallenger() {
        challenger = true;
    }

    /**
     * Evaluates if the player is the winner
     *
     * @return {@code true} if the player is the winner of the {@link it.polimi.ingsw.model.Session session}
     */
    public boolean isWinner() {
        return winner;
    }

    /**
     * The current player becomes the winner of the match
     */
    public void setWinner() {
        winner=true;
    }

    /**
     * Creates a String that contains the player most important values
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
}
