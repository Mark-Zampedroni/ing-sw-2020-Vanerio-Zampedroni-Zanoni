package it.polimi.ingsw.rules.gods;

import it.polimi.ingsw.enumerations.Action;
import it.polimi.ingsw.exceptions.actions.CantActException;
import it.polimi.ingsw.model.Session;
import it.polimi.ingsw.model.map.Position;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.rules.EventRules;

import java.io.Serializable;
import java.util.List;

/**
 * Rules for a player with Poseidon as God
 */
public class PoseidonRules extends EventRules implements Serializable {

    private int counter;
    private Worker movedWorker;
    private Worker unmovedWorker;

    /**
     * Setter for movedWorker
     *
     * @param worker the {@link Worker worker} moved in the last {@link Action moveAction}
     */
    private void setMovedWorker(Worker worker) {
        this.movedWorker = worker;
    }

    /**
     * Getter for movedWorker
     *
     * @return the {@link Worker movedWorker}
     */
    public Worker getMovedWorker() {
        return movedWorker;
    }

    /**
     * Increments the counter variable
     *
     */
    private void increaseCounter() {
        counter++;
    }

    /**
     * Sets the counter variable to 0
     *
     */
    private void clearCounter() {
        counter = 0;
    }

    /**
     * Getter for counter
     *
     * @return the integer value of the counter
     */
    private int getCounter(){ return counter; }

    /**
     * Setter for unmovedWorker, called in {@link #executeMove(Worker, Position) executeMove} method
     *
     */
    private void setUnmovedWorker() {
        if (movedWorker.getMaster().getWorkers().get(0) != getMovedWorker()) {
            unmovedWorker = movedWorker.getMaster().getWorkers().get(0);
        } else {
            unmovedWorker = movedWorker.getMaster().getWorkers().get(1);
        }
    }

    /**
     * Executes a movement {@link Action action}, set the {@link Worker movedWorker} and the {@link Worker unmovedWorker}
     * with the {@link #setUnmovedWorker() setUnmovedWorker} method
     *
     * @param worker selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        super.executeMove(worker, position);
        setMovedWorker(worker);
        setUnmovedWorker();
        clearCounter();
    }

    /**
     * Checks if by the rules it's physically possible to perform a select {@link Action action},
     * for the second {@link Action BUILD} phase the {@link Action BUILD} is possible only with the {@link Worker unmovedWorker}
     *
     * @param worker worker the {@link it.polimi.ingsw.model.player.Player player} wants to select
     * @throws CantActException when the worker can't be selected
     */
    @Override
    public void consentSelect(Worker worker) throws CantActException{
        if (getEvent()) {
        if (! worker.equals(unmovedWorker)) {
            throw new CantActException("This worker can't do any action");
        }}
        super.consentSelect(worker);
    }

    /**
     * Returns a list of possible {@link Action actions} after the
     * {@link it.polimi.ingsw.model.player.Player player}
     * {@link Action built} with a {@link Worker worker},
     * if the unmovedWorker is on {@link it.polimi.ingsw.constants.Height groundLevel} the player can {@link Action BUILD}
     * up to three times
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> list = super.afterBuild();
        if (!getEvent()) {
            if (Session.getInstance().getBoard().getTile(unmovedWorker.getPosition()).getHeight() == 0) {
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
