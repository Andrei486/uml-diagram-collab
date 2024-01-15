package carleton.sysc4907.communications;

import java.io.Serializable;

public record TargetedMessage(
        long[] receiverIds, // an empty array could mean broadcast
        boolean allowUnauthorizedUsers, // if false, filter out users that haven't properly joined. Set true for join request/response.
        Message message) implements Serializable {
}