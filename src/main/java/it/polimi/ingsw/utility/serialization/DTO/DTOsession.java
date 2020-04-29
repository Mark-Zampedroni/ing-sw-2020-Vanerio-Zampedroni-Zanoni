package it.polimi.ingsw.utility.serialization.DTO;


import it.polimi.ingsw.MVC.model.Session;
import it.polimi.ingsw.MVC.model.player.Player;

import java.util.ArrayList;

/**
 * DTO copy of the class {@link Session Session}
 */
public class DTOsession {

    private  DTOboard board;
    private ArrayList<DTOworker> workers;

    /**
     * Method that create a instance of DTOsession
     *
     * @param session indicates his equivalent in server storage
     */
    public DTOsession (Session session) {
        workers = new ArrayList<>();
        for(Player p : session.getPlayers()) {
            workers.add(new DTOworker(p.getWorkers().get(0)));
            workers.add(new DTOworker(p.getWorkers().get(1)));
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

}
