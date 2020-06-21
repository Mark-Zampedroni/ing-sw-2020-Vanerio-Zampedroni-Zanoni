package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 423192168387508181L;
    private final MessageType type;
    private final String sender;
    private final String info;
    private final String recipient;

    public Message(MessageType type, String sender, String info, String recipient) {
        this.type = type;
        this.sender = sender;
        this.info = info;
        this.recipient = recipient;
    }

    public MessageType getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "From: " + sender + "\n" +
                "To: " + recipient + "\n" +
                "Type: " + type + "\n" +
                "Info: " + info + "\n";
    }

}
