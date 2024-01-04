package carleton.sysc4907.communication;

public enum MessageType {
    JOIN_REQUEST,
    JOIN_RESPONSE, // boolean payload, true for accepted
    UPDATE, // command args payload, potentially wrapped with other info
    LEAVING_ROOM,
    CLOSING_ROOM // might also be used for kick, so the end user can't tell?
}
