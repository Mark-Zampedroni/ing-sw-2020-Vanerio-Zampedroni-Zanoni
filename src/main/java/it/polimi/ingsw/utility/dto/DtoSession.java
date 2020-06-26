package it.polimi.ingsw.utility.dto;


import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dto copy of {@link Session Session}.
 * The Dto classes are created and stored in some messages exchanged on the network,
 * their methods consist of only getters and all their variables are final
 */
public class DtoSession implements Serializable {

    private static final long serialVersionUID = 553656941565758029L;
    private final DtoBoard board;
    private final ArrayList<DtoWorker> workers;

    /**
     * Initializes a new DtoSession
     *
     * @param session the session to copy as a dto
     */
    public DtoSession(Session session) {
        workers = new ArrayList<>();
        for (Player p : session.getPlayers()) {
            p.getWorkers().forEach(w -> workers.add(new DtoWorker(w)));
        }

        board = new DtoBoard(session.getBoard());
    }

    /**
     * Getter for the list of {@link DtoWorker workers} on the board
     *
     * @return a shallow copy of the {@link DtoWorker workers} list
     */
    public List<DtoWorker> getWorkers() {
        return new ArrayList<>(workers);
    }

    /**
     * Getter for the {@link DtoBoard DtoBoard}
     *
     * @return the {@link DtoBoard board} saved within the DtoSession
     */
    public DtoBoard getBoard() {
        return board;
    }

    /**
     * Override of the toString method
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (board != null) b.append(board.toString());
        workers.forEach(w -> b.append(w).append("\n"));
        if (workers.isEmpty()) b.append("No workers on board\n");
        return b.toString();
    }

    /**
     * Gets the name of the master of a worker at the coordinates given
     *
     * @param y coordinate on y-axis
     * @param x coordinate on x-axis
     * @return the name of the master
     */
    public String getWorkerMasterOn(int x, int y) {
        for (DtoWorker worker : workers) {
            if (worker.isOn(x, y)) {
                return worker.getMasterUsername();
            }
        }
        return null;
    }
}
