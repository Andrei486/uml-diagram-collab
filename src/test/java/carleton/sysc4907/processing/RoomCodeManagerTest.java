package carleton.sysc4907.processing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoomCodeManagerTest {
    private RoomCodeManager roomCodeManager;

    @BeforeEach
    public void setup() {
        roomCodeManager = new RoomCodeManager();
    }

    @Test
    public void validateRoomCodeTest() {
        assertTrue(roomCodeManager.validateRoomCode("ABCD12341234"));
        assertTrue(roomCodeManager.validateRoomCode("123412349876"));
        assertTrue(roomCodeManager.validateRoomCode("ALLLETTERSAA"));
        assertFalse(roomCodeManager.validateRoomCode(""));
        assertFalse(roomCodeManager.validateRoomCode("TOOSHORT"));
        assertFalse(roomCodeManager.validateRoomCode("8567-ZXY1122"));
        assertFalse(roomCodeManager.validateRoomCode("THISROOMCODEISTOOLONG"));
        assertFalse(roomCodeManager.validateRoomCode("lowercase123"));
    }

    @Test
    public void getNewRoomCodeTest() {
        assertTrue(roomCodeManager.validateRoomCode(roomCodeManager.getNewRoomCode()));
    }
}
