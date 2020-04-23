package it.polimi.ingsw.net.messages.game;


import it.polimi.ingsw.DTO.DTOsession;
import it.polimi.ingsw.enumerations.MessageType;
;
import it.polimi.ingsw.net.messages.Message;

public class ChangeMessage extends Message {

    private final DTOsession session;

    public ChangeMessage (String sender, String content, DTOsession session) {
        super(MessageType.ACTION, sender, content);
        this.session= session;
    }

    public DTOsession getSession() {return session;}
}
