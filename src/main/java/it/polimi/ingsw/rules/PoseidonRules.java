package it.polimi.ingsw.rules;

import it.polimi.ingsw.enumerations.ActionType;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

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

    @Override
    public ArrayList<ActionType> executeBuild (Position position, Worker worker) {
        ArrayList<ActionType> nextAction= super.executeBuild(position, worker);
        Worker notMovedWorker;
        PoseidonRules rules = (PoseidonRules) worker.getMaster().getRules();
        if (worker.getMaster().getWorkers().get(0) != worker){
            notMovedWorker= worker.getMaster().getWorkers().get(0);
        } else {
            notMovedWorker= worker.getMaster().getWorkers().get(1);
        }
        Position positionNotMoved = notMovedWorker.getPosition();

        if (Board.getTile(positionNotMoved).getHeight() == 0) {
            rules.clearCounter();
            nextAction.add(ActionType.BUILD_OTHER);
        }
        return nextAction;
    }
}
