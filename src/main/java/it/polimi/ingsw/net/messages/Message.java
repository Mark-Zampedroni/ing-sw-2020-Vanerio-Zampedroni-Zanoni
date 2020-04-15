package it.polimi.ingsw.net.messages;

import it.polimi.ingsw.enumerations.MessageType;

import java.io.Serializable;

public class Message implements Serializable {

    private final MessageType type;
    private final String sender;
    private final String info;

    public Message(MessageType type, String sender, String info) {
        this.type = type;
        this.sender = sender;
        this.info = info;
    }

    public MessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
    return "From: "+sender+"\n"+
           "Type: "+type+"\n"+
           "Info: "+info;
    }

}
