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

import java.util.ArrayList;
import java.util.List;

public class PoseidonRules extends CommonRules {
    //Il controllo sull'altezza della tile del worker che non si è mosso viene effettuato come ritorno
    private int counter;
    private Worker movedWorker;
    private Player master;
    private Worker unmovedWorker;

    private boolean multiBuild=false;

    public void setMultiBuild (boolean state) {multiBuild=state;}
    public boolean getMultiBuild () { return multiBuild;}

    public void setMaster(Player master) {this.master=master;}
    public Player getMaster() {return master;}

    public void setMovedWorker(Worker worker) {this.movedWorker=worker;}
    public Worker getMovedWorker() {return movedWorker;}

    public int getCounter() {
        return counter;
    }

    public void increaseCounter(){
        counter++;
    }

    public void clearCounter(){
        counter=0;
    }

    public void setUnmovedWorker(){
        if (master.getWorkers().get(0) != getMovedWorker()){
            unmovedWorker = getMaster().getWorkers().get(0);
        } else {
            unmovedWorker= getMaster().getWorkers().get(1);
        }
    }

    public Worker getUnmovedWorker() {return unmovedWorker;}

    @Override
    public void executeMove(Worker worker, Position position) {
    super.executeMove(worker, position);
    setMaster(worker.getMaster());
    setMovedWorker(worker);
    setUnmovedWorker();
    }

    @Override
    public void executeBuild(Position position) {
        if (!multiBuild) {
        super.executeBuild(position);
        clearCounter();
        if (Session.getBoard().getTile(getUnmovedWorker().getPosition()).getHeight() == 0) {
            setMultiBuild(true);}
        } else {
            if (getCounter()<3) {
                increaseCounter();
                super.executeBuild(position);
            }
        }
    }

    @Override
    public void consentBuild(Worker worker, Position position) throws CantActException {
        if (multiBuild) {
            if (worker.equals(unmovedWorker)) {
                super.consentBuild(unmovedWorker, position);
                return;
            } else { throw new CantActException("Wrong Player for final build");}
        } else {super.consentBuild(worker,position);}
    }

    @Override
    public List<Action> afterBuild(){
        List<Action> list = super.afterBuild();
        if (!multiBuild) {
        return list; } else {
            if (getCounter() < 3) {
                list.add(Action.BUILD);
                return list;
            } else { return list; } } }
}
