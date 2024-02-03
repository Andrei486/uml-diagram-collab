package carleton.sysc4907.communications;

import java.io.Serializable;

/**
 * Wraps a message so the TCPSender can send it
 * @param receiverIds the ids to send to
 * @param allowUnauthorizedUsers if it allows users that are invalid
 * @param closeUserOnSend closes the user connection when send
 * @param message the message being wrapped
 */
public record TargetedMessage(
        long[] receiverIds, // an empty array could mean broadcast
        boolean allowUnauthorizedUsers, // if false, filter out users that haven't properly joined. Set true for join request/response.
        boolean closeUserOnSend, // if true, close the user(s) that receives the message
        Message message) implements Serializable {
}