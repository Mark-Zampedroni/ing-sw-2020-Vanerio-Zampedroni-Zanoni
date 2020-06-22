package it.polimi.ingsw.utility.enumerations;

import it.polimi.ingsw.mvc.model.player.Worker;

/**
 * Possible relationship between any two {@link it.polimi.ingsw.mvc.model.player.Worker workers}
 */
public enum Target {
    ALLY {
        /**
         * Controls if the workers have the same master
         *
         * @param worker first worker
         * @param that second worker
         * @return {@code true} if the workers have the same master
         */
        public boolean compareWorkers(Worker worker, Worker that) {
            return worker.getMaster() == that.getMaster();
        }
    },
    ENEMY {
        /**
         * Controls if the workers have different master
         *
         * @param worker first worker
         * @param that second worker
         * @return {@code true} if the workers have different master
         */
        public boolean compareWorkers(Worker worker, Worker that) {
            return !ALLY.compareWorkers(worker, that);
        }
    },
    ANY {
        /**
         * Mark will fill it
         *
         * @param worker first worker
         * @param that second worker
         * @return {@code true}
         */
        public boolean compareWorkers(Worker worker, Worker that) {
            return true;
        }
    },
    SELF {
        /**
         * Compare if the workers are the same worker
         *
         * @param worker first worker
         * @param that second worker
         * @return {@code true} if the workers are the same worker
         */
        public boolean compareWorkers(Worker worker, Worker that) {
            return worker == that;
        }
    };

    /**
     * Checks if two workers have the relationship defined by the instance
     * where the method is called
     *
     * @param worker one of the two workers
     * @param that   the other worker
     * @return {@code true} if the workers have that relationship
     */
    public abstract boolean compareWorkers(Worker worker, Worker that);

}
