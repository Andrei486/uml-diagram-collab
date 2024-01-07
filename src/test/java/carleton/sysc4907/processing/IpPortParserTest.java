package carleton.sysc4907.processing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IpPortParserTest {
    public IpPortParser ipPortParser;

    @BeforeEach
    public void setup() {
        ipPortParser = new IpPortParser();
    }

    @Test
    public void getIpValidStringTest() {
        String testString = "1.2.3.4:1234";
        String ip = ipPortParser.getIp(testString);
        assertEquals("1.2.3.4", ip);
    }

    @Test
    public void getIpInvalidStringTest() {
        String testString = "1.2.3.4:1234";
        assertThrows(IllegalArgumentException.class, ()-> ipPortParser.getIp(testString));
    }

    @Test
    public void getPortValidStringTest() {
        String testString = "1.2.3.4:1234";
        String port = ipPortParser.getPort(testString);
        assertEquals("1234", port);
    }

    @Test
    public void getPortInvalidStringTest() {
        String testString = "1.2.3.4:1234";
        assertThrows(IllegalArgumentException.class, ()-> ipPortParser.getPort(testString));
    }

    @Test
    public void validateIpPortTest() {

    }

}
