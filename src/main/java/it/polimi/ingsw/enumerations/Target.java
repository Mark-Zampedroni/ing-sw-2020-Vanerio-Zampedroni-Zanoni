package it.polimi.ingsw.enumerations;

import it.polimi.ingsw.model.player.Worker;

/**
 * Possible relationship between any two {@link it.polimi.ingsw.model.player.Worker workers}
 */
public enum Target {
    ALLY { public boolean compareWorkers(Worker worker, Worker that) {
        return worker.getMaster() == that.getMaster();
    } },
    ENEMY { public boolean compareWorkers(Worker worker, Worker that) {
        return !ALLY.compareWorkers(worker,that);
    } },
    ANY { public boolean compareWorkers(Worker worker, Worker that) {
        return true;
    } },
    SELF { public boolean compareWorkers(Worker worker, Worker that) {
        return worker == that;
    } };

    /**
     * Checks if two workers have the relationship defined by the instance
     * where the method is called
     *
     * @param worker one of the two workers
     * @param that the other worker
     * @return {@code true} if the workers have that relationship
     */
    public abstract boolean compareWorkers(Worker worker, Worker that);

}
