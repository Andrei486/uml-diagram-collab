package carleton.sysc4907.communications.records;

import carleton.sysc4907.command.Command;

import java.io.Serializable;
import java.util.ArrayList;

public record JoinResponse(ArrayList commandList) implements Serializable {
}
