package carleton.sysc4907.communication;

public class MessageInterpreter {

    public void interpret(Message message) {
        System.out.println(message.type() + " - " + message.payload());
    }
}
