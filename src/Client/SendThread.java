package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SendThread implements Runnable {
    private int port;
    private String toIP;
    private int toPort;

    public SendThread(int port, String toIP, int toPort) {
        this.port = port;
        this.toIP = toIP;
        this.toPort = toPort;
    }

    @Override
    public void run() {
        DatagramSocket socket = null;
        byte[] bytes;
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                String string = input.readLine();
                bytes = string.getBytes();
                DatagramPacket packet = new DatagramPacket(bytes,0, bytes.length,new InetSocketAddress(toIP, toPort));
                socket.send(packet);
                if (string.equals("bye")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}