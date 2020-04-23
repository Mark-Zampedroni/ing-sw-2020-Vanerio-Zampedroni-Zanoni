package it.polimi.ingsw.network.messages.game;


import it.polimi.ingsw.utility.serialization.DTO.DTOsession;
import it.polimi.ingsw.utility.enumerations.MessageType;
import it.polimi.ingsw.network.messages.Message;

public class ChangeMessage extends Message {

    private final DTOsession session;

    public ChangeMessage (String sender, String content, DTOsession session) {
        super(MessageType.ACTION, sender, content);
        this.session= session;
    }

    public DTOsession getSession() {return session;}
}
