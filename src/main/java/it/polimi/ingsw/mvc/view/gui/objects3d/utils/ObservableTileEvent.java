package it.polimi.ingsw.mvc.view.gui.objects3d.utils;

import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.observer.Observable;

/**
 * Observable position, notifies when a Tile/Worker/Animation is clicked on the board coordinates (x,y)
 */
public class ObservableTileEvent extends Observable<DtoPosition> {

    private int x;
    private int y;

    /**
     * Notifies the Observer
     */
    public void run() {
        notify(new DtoPosition(x, y));
    }

    /**
     * Sets the position notifying the Observer
     *
     * @param x coordinate X of the position on the board
     * @param y coordinate Y of the position on the board
     */
    public void setCallerCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
