package carleton.sysc4907.communications;

public class MessageInterpreter {

    public void interpret(Message message) {
        System.out.println(message.type() + " - " + message.payload());
    }
}
