package it.polimi.ingsw.mvc.model.rules.gods;

import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.map.Position;
import it.polimi.ingsw.mvc.model.player.Worker;
import it.polimi.ingsw.mvc.model.rules.EventRules;
import it.polimi.ingsw.utility.enumerations.Action;
import it.polimi.ingsw.utility.exceptions.actions.CantActException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Rules for a player with Poseidon as God
 */
public class PoseidonRules extends EventRules implements Serializable {

    private static final long serialVersionUID = 6425590845136672804L;
    private int counter;
    private Worker movedWorker;
    private Worker unmovedWorker;

    /**
     * Getter for movedWorker
     *
     * @return the {@link Worker movedWorker}
     */
    public Worker getMovedWorker() {
        return movedWorker;
    }

    /**
     * Setter for movedWorker
     *
     * @param worker the {@link Worker worker} moved in the last {@link Action moveAction}
     */
    private void setMovedWorker(Worker worker) {
        movedWorker = worker;
    }

    /**
     * Setter for unmovedWorker
     */
    private void setUnmovedWorker() {
        int index = (movedWorker.getMaster().getWorkers().get(0) != getMovedWorker()) ? 0 : 1;
        unmovedWorker = movedWorker.getMaster().getWorkers().get(index);
    }

    /**
     * Executes a {@link Action movement}, saving which worker is moved
     *
     * @param worker   selected {@link Worker worker}
     * @param position {@link Position position} the {@link Worker worker} will move to
     */
    @Override
    public void executeMove(Worker worker, Position position) {
        setMovedWorker(worker);
        setUnmovedWorker();
        counter = 0;
        super.executeMove(worker, position);
    }

    /**
     * Checks if by the rules it's physically possible to perform a select {@link Action action}
     *
     * @param worker worker the {@link it.polimi.ingsw.mvc.model.player.Player player} wants to select
     * @throws CantActException when the worker can't be selected
     */
    @Override
    public void consentSelect(String username, Worker worker) throws CantActException {
        if (getEvent() && !worker.equals(unmovedWorker)) {
            throw new CantActException("This worker can't do any action");
        }
        super.consentSelect(username, worker);
    }


    /**
     * Returns a list of possible actions after a {@link Action selection}
     *
     * @return the list of possible {@link Action actions} after the selection of a worker
     */
    @Override
    public List<Action> afterSelect() {
        return (getEvent()) ? Arrays.asList(Action.BUILD, Action.END_TURN) : super.afterSelect();
    }

    /**
     * Returns a list of possible actions after a {@link Action build}
     *
     * @return list of {@link Action actions} that can be done after {@link Action building}
     */
    @Override
    public List<Action> afterBuild() {
        List<Action> list = super.afterBuild();
        if (!getEvent()) {
            if (Session.getInstance().getBoard().getTile(unmovedWorker.getPosition()).getHeight() == 0) {
                setEvent(true);
                list.add(Action.SELECT_WORKER);
            }
        } else if (counter < 2) {
            counter++;
            list.add(Action.BUILD);
        } else {
            counter = 0;
        }
        return list;
    }
}
