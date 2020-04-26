package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.Map;


public class GodChosen extends Message {

    private ArrayList<String> gods;

    public GodChosen(String sender, String info, ArrayList<String> gods) {
        super(MessageType.GOD_CHOSEN, sender, info);
        this.gods = gods;
    }

    public ArrayList<String> getGods(){return this.gods;}
}
