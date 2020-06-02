package Client;

import java.io.IOException;

public class Test {
    public static void main(String[] args) {
        new Thread(new runnableTest()).start();
//        new Thread(new runnableTest()).start();
    }
}

class runnableTest implements Runnable {

    @Override
    public void run() {
        try {
            Client client = new Client("localhost",1111);
            client.Login();
            client.register();
            client.exit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}