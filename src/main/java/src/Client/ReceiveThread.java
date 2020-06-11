package src.Client;

import src.UI.chatRoom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ReceiveThread implements Runnable {
    private Client client = Client.getInstance();
    private DataInputStream dis = client.getDis();
    private DataOutputStream dos = client.getDos();
    src.UI.chatRoom chatRoom;
    Flag flag = Flag.getInstance();


    public ReceiveThread(src.UI.chatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }



    private void showMessage() throws IOException {
        String[] onlineList = dis.readUTF().split("@");
        String[] font = onlineList[5].split("#");
        chatRoom.infoTransfer(onlineList[4], onlineList[3], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
    }

    @Override
    public void run() {
        while (true) {
            try {
                String string = dis.readUTF();
                String[] output = string.split("@");
                synchronized (flag) {
                    flag.modify = true;
                    switch (output[2]) {
                        case "0":
                            switch (output[3]) {
                                case "0":
                                    flag.login=0;
                                    break;
                                case "1":
                                    flag.login=1;
                                    break;
                                case "2":
                                    flag.login=2;
                                    break;
                            }
                            break;
                        case "1":
                            switch (output[3]) {
                                case "0":
                                    flag.register=0;
                                    break;
                                case "1":
                                    flag.register=1;
                                    break;
                            }
                            break;
                        case "2":
                            switch (output[3]) {
                                case "0":
                                    flag.logout=0;
                                    break;
                                case "1":
                                    flag.logout=1;
                                    break;
                            }
                            break;
                        case "100":
                            switch (output[3]) {
                                case "0":
                                    flag.checkOnline=0;
                                    break;
                                case "1":
                                    flag.checkOnline=1;
                                    break;
                            }
                            break;
                        case "101":
                            switch (output[3]) {
                                case "0":
                                    flag.sendPrivateMessage=0;
                                    break;
                                case "1":
                                    flag.sendPrivateMessage=1;
                                    break;
                            }
                            break;
                        case "102":
                            switch (output[3]) {
                                case "0":
                                    flag.createGroup=0;
                                    break;
                                case "1":
                                    flag.createGroup=1;
                                    break;
                            }
                            break;
                        case "103":
                            switch (output[3]) {
                                case "0":
                                    flag.activateGroup=0;
                                    int n = Integer.parseInt(output[1]);
                                    for (int i = 0; i < n; i++) {
                                        showMessage();
                                    }
                                    break;
                            }
                            break;
                        case "104":
                            switch (output[3]) {
                                case "0":
                                    flag.sendGroupMessage=0;
                                    break;
                                case "1":
                                    flag.sendGroupMessage=1;
                                    break;
                                case "2":
                                    flag.sendGroupMessage=2;
                                    break;
                            }
                            break;
                        case "105":
                            switch (output[3]) {
                                case "0":
                                    flag.onlineListFlag=0;
                                    int n = Integer.parseInt(output[1]);
                                    String[] onlineList = new String[n];
                                    for (int i = 0; i < n; i++) {
                                        onlineList[i] = dis.readUTF();
                                    }
                                    flag.onlineList = onlineList;
                                    break;
                            }
                            break;
                        case "106":
                            switch (output[3]) {
                                case "0":
                                    flag.exitGroup=0;
                                    break;
                                case "1":
                                    flag.exitGroup=1;
                                    break;
                            }
                            break;
                        case "107":
                            switch (output[3]) {
                                case "0":
                                    flag.groupMember = output[4].split("#");
                                    break;
                            }
                            break;
                        case "200":
                            if (output[1].equals(flag.curToUsername)) {
                                String[] font = output[4].split("#");
                                chatRoom.infoTransfer(output[3], output[1], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], false);
                            new Thread(new SendThread(chatRoom)).start();
                            break;
                        case "201":
                            if (output[1].equals(flag.curToUsername)) {
                                String[] font = output[5].split("#");
                                chatRoom.infoTransfer(output[4], output[3], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            new Thread(new SendThread(chatRoom)).start();
//                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], false);
                            break;
                    }
                    flag.notify();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
