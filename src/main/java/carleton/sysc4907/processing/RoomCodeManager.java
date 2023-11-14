package carleton.sysc4907.processing;

public class RoomCodeManager {

    public RoomCodeManager() {

    }

    /**
     * Checks if a string meets the requirements of a room code and is unique
     * @param roomCode the string to check
     * @return true if the room code is valid
     */
    public boolean validateRoomCode(String roomCode) {
        if (roomCode == null) {
            return false;
        }
        if(isValidString(roomCode)) {
            //TODO
            //if code is valid, check the server for the code
            //check if the code is in the server, if it is, return true
            return true;
        }
        return false;
    }


    /**
     * Checks if the given string meets the requirements of a room code.
     * Note: this method does not check if the room code is unique
     * @param roomCode the string to check
     * @return true if the string meets the requirements of a room code, otherwise false
     */
    private boolean isValidString(String roomCode) {
        if((roomCode.length() != 12)) {
            return false;
        }
        char c;
        for (int i = 0; i < roomCode.length(); i++) {
            c = roomCode.charAt(i);
            if ((!Character.isLetterOrDigit(c)) && (!Character.isUpperCase(c))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets a new room code and adds it as an entry in the server
     * @return
     */
    public String getNewRoomCode() {
        //TODO
        //temporarily returning a set room code
        return "ROOMCODE1234";
    }
}
