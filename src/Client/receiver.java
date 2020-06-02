package Client;

public class receiver {
    public static void main(String[] args) {
        new Thread(new SendThread(8888, "localhost", 7777)).start();
        new Thread(new ReceiveThread(9999)).start();
    }
}
