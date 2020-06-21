package it.polimi.ingsw.mvc.model.player;

import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.rules.GodRules;
import it.polimi.ingsw.utility.enumerations.Colors;
import it.polimi.ingsw.utility.enumerations.Gods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent a person (identified by his username) playing the game in a {@link it.polimi.ingsw.mvc.model.Session session}
 * with a unique {@link it.polimi.ingsw.utility.enumerations.Colors color}, a chosen {@link it.polimi.ingsw.utility.enumerations.Gods god}
 * and a list of {@link it.polimi.ingsw.mvc.model.player.Worker workers}
 */
public class Player implements Serializable {

    private static final long serialVersionUID = 1606644735117861200L;
    private final String username;
    private final ArrayList<Worker> workers;
    private final Colors color;
    private GodRules rules;
    private Gods god;
    private boolean challenger;
    private boolean starter;
    private boolean loss;

    /**
     * Initializes a player with his username, creating two {@link Worker workers}
     *
     * @param username unique string used to identify the player
     * @param color    the color associated to the player
     */
    public Player(String username, Colors color) {
        this.username = username;
        this.workers = new ArrayList<>();
        this.color = color;
    }

    /**
     * Add a worker in {@link Position position} to the list of the player's workers
     *
     * @param position position of the worker added
     */
    public void addWorker(Position position) {
        workers.add(new Worker(position));
    }

    /**
     * Getter for color
     *
     * @return color the {@link Colors color} which represents the player
     */
    public Colors getColor() {
        return color;
    }

    /**
     * Getter for username
     *
     * @return username the {@link String string} which represents the username
     */
    public String getUsername() {
        return username;
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
     * Setter for god
     *
     * @param god the {@link Gods god} chosen by the player during the creation, never change during the match
     */
    public void setGod(Gods god) {
        this.god = god;
        setRules(GodRules.getInstance(god));
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
     * Setter for rules
     *
     * @param rules specific {@link GodRules rules} that are linked to the chosen {@link Gods god}
     */
    public void setRules(GodRules rules) {
        this.rules = rules;
    }

    /**
     * Getter for workers
     *
     * @return a shallow copy of the list which contains the workers {@link Worker workers}
     */
    public List<Worker> getWorkers() {
        return new ArrayList<>(workers);
    }

    /**
     * Removes the {@link Worker worker} of a player in a specific {@link Position position}
     *
     * @param position specific {@link GodRules rules} that are linked to the chosen {@link Gods god}
     */
    public void removeWorker(Position position) {
        workers.removeIf(w -> w.getPosition().isSameAs(position));
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
    public boolean isChallenger() {
        return challenger;
    }

    /**
     * The current player becomes the challenger (first player)
     */
    public void setChallenger() {
        challenger = true;
    }

    /**
     * Evaluates if the player is the starter
     *
     * @return {@code true} if the player is the starter of the {@link it.polimi.ingsw.mvc.model.Session session}
     */
    public boolean isStarter() {
        return starter;
    }

    /**
     * The current player becomes the starter of the match
     */
    public void setStarter() {
        starter = true;
    }

    /**
     * Evaluates if the player has lost
     *
     * @return {@code true} if the player has lost
     */
    public boolean isLoser() {
        return loss;
    }

    /**
     * Set the player to a "looser" player, removes his workers, removes the effect of his god
     */
    public void loss() {
        loss = true;
        workers.clear();
        if (rules != null) {
            rules.removeEffect();
        }
    }

    /**
     * Creates a String that contains the player most important values
     *
     * @return a String with the player most important attributes
     */
    @Override
    public String toString() {
        return "{Username: " + this.username
                + ", Color: " + color
                + ", God: " + god
                + "}";
    }
}
