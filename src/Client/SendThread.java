package Client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SendThread implements Runnable {
    private DataOutputStream dos;
    private BufferedReader input;

    public SendThread(DataOutputStream dos, BufferedReader input) {
        this.dos = dos;
        this.input = input;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String string = input.readLine();
                dos.writeUTF(string);
                dos.flush();
                if (string.equals("bye")) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}