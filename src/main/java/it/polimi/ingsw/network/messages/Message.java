package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.utility.enumerations.MessageType;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 423192168387508181L;
    private final MessageType type;
    private final String sender;
    private final String info;
    private final String recipient;

    /**
     * Constructor
     *
     * @param sender sender of the message
     * @param info information
     * @param type type of the message
     * @param recipient the recipient of the message
     */
    public Message(MessageType type, String sender, String info, String recipient) {
        this.type = type;
        this.sender = sender;
        this.info = info;
        this.recipient = recipient;
    }

    /**
     * Getter for the type
     *
     * @return the type
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Getter for the sender
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter for the recipient
     *
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Getter for the information
     *
     * @return the information
     */
    public String getInfo() {
        return info;
    }

    /**
     * Override of ToString
     *
     * @return a different string
     */
    @Override
    public String toString() {
        return "From: " + sender + "\n" +
                "To: " + recipient + "\n" +
                "Type: " + type + "\n" +
                "Info: " + info + "\n";
    }

}
