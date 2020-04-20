package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.Message;

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
