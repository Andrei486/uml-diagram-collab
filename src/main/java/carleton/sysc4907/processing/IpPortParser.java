package carleton.sysc4907.processing;

public class IpPortParser {
    public IpPortParser() {

    }

    public boolean validateIpPortFormatting(String ipPort) {
        String[] splitString = ipPort.split(":");
        if (ipPort.split(":").length != 2) {
            return false;
        }
        try {
            Integer.parseInt(splitString[1]);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Gets the ip address from an ip and port string separated by ':'
     * @param ipPort the string to parse
     * @return the ip address
     */
    public String getIp(String ipPort) {
        if (!validateIpPortFormatting(ipPort)) {
            throw new IllegalArgumentException("The provided string is incorrectly formatted. The string must be an ip" +
                    " address followed by a port number, separated by a colon");
        }
        return (ipPort.split(":"))[0];
    }

    /**
     * Gets the port address from an ip and port string separated by ':'
     * @param ipPort the string to parse
     * @return the port
     */
    public String getPort(String ipPort) {
        if (!validateIpPortFormatting(ipPort)) {
            throw new IllegalArgumentException("The provided string is incorrectly formatted. The string must be an ip" +
                    " address followed by a port number, separated by a colon");
        }
        return (ipPort.split(":"))[1];
    }
}
