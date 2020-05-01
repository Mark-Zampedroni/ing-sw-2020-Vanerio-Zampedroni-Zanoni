package it.polimi.ingsw.utility.serialization.DTO;


import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * DTO copy of the class {@link Session Session}
 */
public class DTOsession implements Serializable {

    private DTOboard board;
    private ArrayList<DTOworker> workers;

    /**
     * Method that create a instance of DTOsession
     *
     * @param session indicates his equivalent in server storage
     */
    public DTOsession (Session session) {
        workers = new ArrayList<>();
        for(Player p : session.getPlayers()) {
            p.getWorkers().forEach(w -> workers.add(new DTOworker(w)));
        }

        board = new DTOboard(session.getBoard());
        }

    /**
     * Getter for the list of the {@link DTOworker workers}
     *
     * @return a shallow copy of the {@link DTOworker workers} list
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DTOworker> getWorkers() {
        return (ArrayList<DTOworker>) workers.clone();
    }

    /**
     * Getter for the {@link DTOboard DTOboard}
     *
     * @return the {@link DTOboard board} used in the session
     */
    public DTOboard getBoard() {
        return board;
    }

    /**
     * Implementation of toString method for the {@link DTOsession DTObsession}
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
