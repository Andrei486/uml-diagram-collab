package carleton.sysc4907.communications.messages;

import carleton.sysc4907.communications.MessageType;

import java.io.Serializable;

public record JoinMessage(MessageType messageType, String username) implements Serializable {

}
