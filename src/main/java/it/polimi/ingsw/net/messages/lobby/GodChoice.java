package it.polimi.ingsw.net.messages.lobby;

import it.polimi.ingsw.enumerations.MessageType;
import it.polimi.ingsw.net.messages.Message;

import java.util.ArrayList;

public class GodChoice extends Message {

    private ArrayList<String> gods;

    public GodChoice(String sender, String info, ArrayList<String> gods) {
        super(MessageType.GOD_CHOICE, sender, info);
        this.gods = gods;
    }

    public ArrayList<String> getGods(){return this.gods;}
}

