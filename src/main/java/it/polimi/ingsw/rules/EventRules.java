package it.polimi.ingsw.rules;

import it.polimi.ingsw.model.map.Position;

public class EventRules extends CommonRules {

    private boolean event=false;
    private Position pos;

    public boolean getEvent() {return event;}

    public void setEvent(boolean event) {this.event = event;}

    public Position getPos() {return pos;}

    public void setPos(Position pos) {this.pos = pos;}

    public void clear(){
        this.event=false;
    }
}