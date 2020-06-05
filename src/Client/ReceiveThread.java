package Client;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ReceiveThread implements Runnable {
    private DataInputStream dis;
    private JTextArea txtRcd;

    public ReceiveThread(DataInputStream dis, JTextArea txtRcd) {
        this.dis = dis;
        this.txtRcd = txtRcd;
    }

    @Override
    public void run() {
        while (true) {
            try {

                String string = dis.readUTF();
                String[] output = string.split("@");
                synchronized (txtRcd) {
                    txtRcd.setEditable(true);
                    txtRcd.append(output[0] + ":\n    " + output[1] + "\n\n");
                    txtRcd.setEditable(false);
                }
//                System.out.println(output[1] + ":" + output[2]);
//                if (output.equals("bye")) {
//                    break;
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
