package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.Colors;
import it.polimi.ingsw.enumerations.Gods;
import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.net.messages.FlagMessage;

import java.util.ArrayList;
import java.util.List;


public class GodUpdate extends FlagMessage {

    private ArrayList<Gods> chosenGods = new ArrayList<>();

    public GodUpdate(MessageType type, String sender, String info,  boolean flag, ArrayList<Gods> god) {
        super(MessageType.GOD_UPDATE, sender, info, flag);
    }

    public ArrayList<Gods> getChosenGods() {
        return chosenGods;
    }

    public void setChosenGod(Gods god) {
        chosenGods.add(god);
    }
}
