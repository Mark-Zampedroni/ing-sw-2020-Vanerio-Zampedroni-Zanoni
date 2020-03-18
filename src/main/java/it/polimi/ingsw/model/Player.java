package it.polimi.ingsw.model;
import it.polimi.ingsw.rules.GodRules;
import java.util.ArrayList;

// STEFANO

public class Player {

    private String username;
    private GodRules rules;
    private ArrayList<Worker> workers;

    public Player(String username) {
        this.username = username;
        this.rules = null;
    }

    public void setRules(GodRules rules) {
        this.rules = rules;
    }
    public GodRules getRules() {
        return rules;
    }

    // Returns Shallow copy of workers
    @SuppressWarnings("unchecked")
    public ArrayList<Worker> getWorkers() {
        return (ArrayList<Worker>) workers.clone();
    }

    // PLACEHOLDER, DA IMPLEMENTARE
    public Worker getWorker(Position p) { return null; }

}
