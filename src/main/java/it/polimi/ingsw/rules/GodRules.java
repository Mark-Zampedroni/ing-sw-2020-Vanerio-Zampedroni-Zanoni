package it.polimi.ingsw.rules;
import it.polimi.ingsw.exceptions.actions.*;
import it.polimi.ingsw.exceptions.actions.building.AlreadyCompleteException;
import it.polimi.ingsw.exceptions.actions.building.AlreadyOccupiedException;
import it.polimi.ingsw.exceptions.actions.building.OutOfReachException;
import it.polimi.ingsw.exceptions.actions.movement.*;
import it.polimi.ingsw.model.map.Board;
import it.polimi.ingsw.model.player.Position;
import it.polimi.ingsw.model.player.Worker;
import static it.polimi.ingsw.constants.Height.*;

public abstract class GodRules {

    private boolean event=false;
    private Position pos;

    public void consentBuild(Worker worker, Position position) throws CantBuildException {
        if(worker.getPosition().getDistanceFrom(position)!=1 )
        {
            throw new OutOfReachException("Maximum building limit exceeded");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new AlreadyCompleteException("This tile is no longer available for building");
        }
        else if(position.getWorker()!=null)
        {
            throw new AlreadyOccupiedException("This tile is already occupied");
        }

        }


    public void consentMovement(Worker worker, Position position) throws CantMoveException {
        if(worker.getPosition().getDistanceFrom(position)!=1)
        {
            throw new aException("Maximum movement limit exceeded");
        }
        else if(position.getWorker()!=null)
        {
            throw new bException("This tile is already occupied");
        }
        else if(Board.getTile(position).hasDome())
        {
            throw new cException("This tile is no longer available for movement");
        }
        else if(Board.getTile(position).getHeight() > Board.getTile(worker.getPosition()).getHeight() +1)
        {
            throw new dException("Unreachable tile from this position");
        }
        else if(blockedByEnemy(worker, position))
        {
            throw new AthenaException("Not allowed due to Athena's power");
        }



    }

    public boolean isWinner(Worker worker, Position position) {
        return(Board.getTile(worker.getPosition()).getHeight()==MID && Board.getTile(position).getHeight()==TOP);
    }

    public boolean blockedByEnemy(Worker worker, Position position)
    {
        return false;
    }


    public boolean getEvent() {return event;}

    public void setEvent(boolean event) {this.event = event;}

    public Position getPos() {return pos;}

    public void setPos(Position pos) {this.pos = pos;}

    public void clear(){
        this.event=false;
    }

}
