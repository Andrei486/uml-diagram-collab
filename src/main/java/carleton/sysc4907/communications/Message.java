package carleton.sysc4907.communications;

import java.io.Serializable;

public record Message(MessageType type, Object payload) implements Serializable {
}
