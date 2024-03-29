package it.polimi.ingsw.utility.enumerations;

import it.polimi.ingsw.network.messages.Message;

/**
 * Different types of {@link Message messages} used to exchange information between
 * {@link it.polimi.ingsw.network.client.Client Client} and {@link it.polimi.ingsw.network.server.Server Server}
 */
public enum MessageType {
    CONNECTION_TOKEN,
    SLOTS_UPDATE,
    REGISTRATION_UPDATE,
    DISCONNECTION_UPDATE,
    STATE_UPDATE,
    LOBBY_UPDATE,
    SELECTION_UPDATE,
    GODS_SELECTION_UPDATE,
    GODS_UPDATE,
    STARTER_PLAYER,
    TURN_UPDATE,
    ACTION,
    RECONNECTION_REPLY,
    RECONNECTION_UPDATE,
    WIN_LOSE_UPDATE,
    INFO_UPDATE
}
