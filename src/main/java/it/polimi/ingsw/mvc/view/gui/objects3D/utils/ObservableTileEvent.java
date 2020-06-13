package it.polimi.ingsw.mvc.view.gui.objects3D.utils;

import it.polimi.ingsw.utility.dto.DtoPosition;
import it.polimi.ingsw.utility.observer.Observable;

public class ObservableTileEvent extends Observable<DtoPosition> {

    int x, y;

    public void run() {
        notify(new DtoPosition(x,y));
    }

    public void setCaller(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
