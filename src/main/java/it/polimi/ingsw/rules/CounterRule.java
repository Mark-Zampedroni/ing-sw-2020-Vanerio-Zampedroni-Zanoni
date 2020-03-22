package it.polimi.ingsw.rules;

public abstract class CounterRule extends GodRules {
    //0 if moving worker, 1 for the first time unmoved worker is building, 2 for the 2nd time, 3rd for the last time
    private int counter;

    public int getCounter() {
        return counter=0;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increaseCounter(){
        counter++;
    }
    public void clearCounter(){
        counter=0;
    }
}
