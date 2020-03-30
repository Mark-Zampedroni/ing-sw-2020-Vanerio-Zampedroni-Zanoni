package it.polimi.ingsw.model.player;
import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.rules.GodRules;

import java.util.ArrayList;

// STEFANO

public class Player {

    private String username;
    private GodRules rules;
    private ArrayList<Worker> workers;
    private Colors color;
    private Gods god;
    private boolean challenger;
    private boolean winner;

    // ovviamente challenger e color sono temporanei
    public Player(String username) {
        this.username = username;
        this.workers= new ArrayList<>();
        this.workers.add(new Worker(this));
        this.workers.add(new Worker(this));
        this.color=Colors.BLUE;
    }

    public void setColor(Colors color){ this.color = color;}

    public String getUsername(){
        return username;
    }

    public void setGod(Gods god) {
        this.god = god;
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

    public boolean isChallenger(){
        return challenger;
    }

    public void setChallenger() {
        challenger = true;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner() {
        winner=true;
    }

    @Override
    public String toString() {
        return "{Username: "+this.username
                +", Color: "+ String.valueOf(color)
                +", God: " + String.valueOf(god)
                +"}";
    }

    public Gods getGod() {
        return god;
    }
}
