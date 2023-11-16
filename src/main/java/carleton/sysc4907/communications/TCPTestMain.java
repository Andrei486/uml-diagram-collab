package carleton.sysc4907.communications;

public class TCPTestMain {
    public static void main(String[] args) {
        new Host().start();
        new Client("c1").start();
    }
}
