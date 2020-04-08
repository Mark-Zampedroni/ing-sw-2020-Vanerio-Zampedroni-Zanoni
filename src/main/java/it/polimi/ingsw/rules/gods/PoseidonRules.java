package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.map.Tile;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.Check;
import it.polimi.ingsw.rules.CommonRules;
import it.polimi.ingsw.rules.EventRules;

import java.util.ArrayList;
import java.util.List;

public class PoseidonRules extends EventRules {
    //Il controllo sull'altezza della tile del worker che non si è mosso viene effettuato come ritorno
    private int counter;
    private Worker movedWorker;
    private Worker unmovedWorker;

    private void setMovedWorker(Worker worker) {
        this.movedWorker = worker;
    }

    public Worker getMovedWorker() {
        return movedWorker;
    }

    private void increaseCounter() {
        counter++;
    }

    private void clearCounter() {
        counter = 0;
    }

    private int getCounter(){ return counter; }

    private void setUnmovedWorker() {
        if (movedWorker.getMaster().getWorkers().get(0) != getMovedWorker()) {
            unmovedWorker = movedWorker.getMaster().getWorkers().get(0);
        } else {
            unmovedWorker = movedWorker.getMaster().getWorkers().get(1);
        }
    }

    @Override
    public void executeMove(Worker worker, Position position) {
        super.executeMove(worker, position);
        setMovedWorker(worker);
        setUnmovedWorker();
        clearCounter();
    }

    @Override
    public void consentSelect(Worker worker) throws CantActException{
        if (getEvent()) {
        if (! worker.equals(unmovedWorker)) {
            throw new CantActException("This worker can't do any action");
        }}
        super.consentSelect(worker);
    }

    @Override
    public List<Action> afterBuild() {
        List<Action> list = super.afterBuild();
        if (!getEvent()) {
            if (Session.getBoard().getTile(unmovedWorker.getPosition()).getHeight() == 0) {
                setEvent(true);
                list.add(Action.SELECT_WORKER);}
                return list;
        }
        else if (getCounter() < 2) {
            increaseCounter();
            list.add(Action.BUILD);
            return list;
        }
        else {
            clearCounter();
            return list;
        }
    }
}
