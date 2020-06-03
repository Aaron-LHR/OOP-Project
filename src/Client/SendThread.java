package Client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SendThread implements Runnable {
    private DataOutputStream dos;
    private BufferedReader input;
    private String username;

    public SendThread(DataOutputStream dos, String username) {
        this.dos = dos;
        this.username = username;
        input = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String string = input.readLine();
                dos.writeUTF("@" + username + "@abc" + "@" + string);
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