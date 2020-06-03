package Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveThread implements Runnable {
    private DataInputStream dis;

    public ReceiveThread(DataInputStream dis) {
        this.dis = dis;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String string = dis.readUTF();
                System.out.println("对方：" + string);
                if (string.equals("bye")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
