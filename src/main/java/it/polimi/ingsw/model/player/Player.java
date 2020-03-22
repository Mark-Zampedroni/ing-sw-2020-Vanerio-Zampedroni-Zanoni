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
    private boolean challenger;
    private Colors color;
    private boolean winner;
    private Gods god;

    // ovviamente challenger e color sono temporanei
    public Player(String username) {
        this.username = username;
        this.rules = null;
        this.workers= new ArrayList<>();
        this.workers.add(new Worker(this));
        this.workers.add(new Worker(this));
        this.challenger=false;
        this.color=Colors.BLUE;
        this.winner=false;
        this.god=null;
    }

    public String getUsername(){
        return this.username;
    }

    public void setGod(Gods god) {
        this.god=god;
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
        this.challenger=true;
    }

    public boolean isWinner() {
        return this.winner;
    }

    public void setWinner() {
        this.winner=true;
    }

    @Override
    public String toString() {
        return "{Username: "+this.username
                +", Color: "+ String.valueOf(color)
                +", God: " + String.valueOf(rules)
                +"}";
    }

    public Gods getGod() {
        return this.god;
    }
}
