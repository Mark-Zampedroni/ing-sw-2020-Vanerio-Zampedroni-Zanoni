package it.polimi.ingsw.model;
import it.polimi.ingsw.rules.GodRules;

public class Player {

    private String username;
    private GodRules rules;

    public Player(String username) {
        this.username = username;
        this.rules = null;
    }

    public void buildAction(int x, int y) {
        Board.buildTowerOn(this,x,y);
    }

    public void setRules(GodRules rules) {
        this.rules = rules;
    }

    public GodRules getRules() {
        return rules;
    }

    @Override
    public String toString() {
        return username;
    }

}
