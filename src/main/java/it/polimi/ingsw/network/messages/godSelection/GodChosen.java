package it.polimi.ingsw.network.messages.godSelection;

import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GodChosen extends Message {

    private List<String> gods;

    public GodChosen(String sender, String info, List<String> gods) {
        super(MessageType.GOD_CHOSEN, sender, info);
        this.gods = gods;
    }

    public List<String> getGods() { return gods; }
}
