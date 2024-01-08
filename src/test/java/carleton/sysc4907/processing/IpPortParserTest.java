package carleton.sysc4907.processing;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class IpPortParserTest {
    public IpPortParser ipPortParser;

    @BeforeEach
    public void setup() {
        ipPortParser = new IpPortParser();
    }

    @Test
    public void getIp_ValidString_Test() {
        String testString = "1.2.3.4:1234";
        String ip = ipPortParser.getIp(testString);
        assertEquals("1.2.3.4", ip);
    }

    @Test
    public void getIp_InvalidString_Test() {
        String testString = "this string is not a valid ip port";
        assertThrows(IllegalArgumentException.class, ()-> ipPortParser.getIp(testString));
    }

    @Test
    public void getPort_ValidString_Test() {
        String testString = "1.2.3.4:1234";
        String port = ipPortParser.getPort(testString);
        assertEquals("1234", port);
    }

    @Test
    public void getPort_InvalidString_Test() {
        String testString = "this string is not a valid ip port";
        assertThrows(IllegalArgumentException.class, ()-> ipPortParser.getPort(testString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.2.3.4:1234", "test:1234"})
    public void validateIpPortFormatting_ValidString_Test(String testString) {
        boolean isCorrect = ipPortParser.validateIpPortFormatting(testString);
        assertTrue(isCorrect);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.2.3.4:", "1.2.3.4:eeee", "hello", "test:test"})
    public void validateIpPortFormatting_InvalidString_Test(String testString) {
        boolean isCorrect = ipPortParser.validateIpPortFormatting(testString);
        assertFalse(isCorrect);
    }

}
