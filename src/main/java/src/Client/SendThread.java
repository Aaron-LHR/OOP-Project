package src.Client;

import src.UI.chatRoom;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class SendThread implements Runnable {
    private DataOutputStream dos = Client.getInstance().getDos();
    private src.UI.chatRoom chatRoom;

    public SendThread(src.UI.chatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public void run() {
        chatRoom.refresh();
    }
}