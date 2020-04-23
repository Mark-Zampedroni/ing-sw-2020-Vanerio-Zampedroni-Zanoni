package it.polimi.ingsw.network.messages.lobby;

import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.Map;


public class GodUpdate extends Message {

    private Map<String, ArrayList<String>> gods;

    public GodUpdate(String sender, String info, Map<String, ArrayList<String>> gods) {
        super(MessageType.GOD_UPDATE, sender, info);
        this.gods = gods;
    }

    public Map<String, ArrayList<String>> getGods(){return this.gods;}
}
