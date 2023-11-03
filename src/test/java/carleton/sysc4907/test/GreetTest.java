package carleton.sysc4907.test;

import carleton.sysc4907.model.GreetClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GreetTest {

    @Test
    public void givenGreetingClient_whenServerRespondsWhenStarted_thenCorrect() {
        GreetClient client = new GreetClient();
        client.startConnection("127.0.0.1", 6666);
        String response = client.sendMessage("hello server");
        assertEquals("hello client", response);
    }
}
