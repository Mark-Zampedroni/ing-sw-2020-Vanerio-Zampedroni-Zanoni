package it.polimi.ingsw.rules;

public class PoseidonRules extends GodRules{
    //Il controllo sull'altezza della tile del worker che non si è mosso viene effettuato come ritorno
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
