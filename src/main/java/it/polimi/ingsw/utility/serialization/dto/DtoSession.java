package it.polimi.ingsw.utility.serialization.dto;


import it.polimi.ingsw.mvc.model.Session;
import it.polimi.ingsw.mvc.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DTO copy of the class {@link Session Session}
 */
public class DtoSession implements Serializable {

    private static final long serialVersionUID = 553656941565758029L;
    private final DtoBoard board;
    private final ArrayList<DtoWorker> workers;

    /**
     * Method that create a instance of DTOsession
     *
     * @param session indicates his equivalent in server storage
     */
    public DtoSession(Session session) {
        workers = new ArrayList<>();
        for(Player p : session.getPlayers()) {
            p.getWorkers().forEach(w -> workers.add(new DtoWorker(w)));
        }

        board = new DtoBoard(session.getBoard());
        }

    /**
     * Getter for the list of the {@link DtoWorker workers}
     *
     * @return a shallow copy of the {@link DtoWorker workers} list
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DtoWorker> getWorkers() {
        return (ArrayList<DtoWorker>) workers.clone();
    }

    /**
     * Getter for the {@link DtoBoard DTOboard}
     *
     * @return the {@link DtoBoard board} used in the session
     */
    public DtoBoard getBoard() {
        return board;
    }

    /**
     * Implementation of toString method for the {@link DtoSession DTObsession}
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if(board != null) {
            b.append(board.toString());
        }
        workers.forEach(w -> b.append(w).append("\n"));
        if(workers.isEmpty()) { b.append("No workers on board\n"); }
        return b.toString();
    }

}
