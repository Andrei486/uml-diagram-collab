package carleton.sysc4907.communications;

import java.io.Serializable;

public class JoinMessage implements Serializable {
    private static final long serialVersionUID = 12375L;
    private String username;

    public JoinMessage(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
