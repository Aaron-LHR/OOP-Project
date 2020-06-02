package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveThread implements Runnable {
    private int port;

    public ReceiveThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] bytes = new byte[1024*60];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        while (true) {
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = packet.getData();
            String string = new String(data, 0, packet.getLength());
            System.out.println("对方：" + string);
            if (string.equals("bye")) {
                break;
            }
        }
        socket.close();
    }
}
