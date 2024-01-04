package carleton.sysc4907.communication;

import java.io.Serializable;


// Should messages include the sender/receivers? How to specify that a message should not be delivered to
// unauthorized users (i.e. who haven't joined the room properly)?
// We could use fields in the message that we unset later for security, since IDs and enums are serializable.
// Or we could wrap the message with that info before sending.
public record Message(MessageType type, Object payload) implements Serializable {
}
