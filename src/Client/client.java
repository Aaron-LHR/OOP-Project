package Client;

public class client {
    public static void main(String[] args) {
        new Thread(new SendThread(6666, "localhost", 9999)).start();
        new Thread(new ReceiveThread(7777)).start();
    }
}
