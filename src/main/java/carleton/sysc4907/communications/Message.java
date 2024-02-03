package carleton.sysc4907.communications;

import java.io.Serializable;

/**
 * Used to wrap objects to be sent and received over TCP
 * @param type the type of message being sent
 * @param payload the object being sent
 */
public record Message(MessageType type, Object payload) implements Serializable {
}
