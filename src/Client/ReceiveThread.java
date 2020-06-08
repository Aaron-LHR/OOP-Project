package Client;

import UI.chatRoom;

import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveThread implements Runnable {
    private DataInputStream dis;
    chatRoom chatRoom;
    Flag flag = Flag.getInstance();

    public ReceiveThread(DataInputStream dis, chatRoom chatRoom) {
        this.dis = dis;
        this.chatRoom = chatRoom;
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
                                    String[] onlineList = new String[n];
                                    for (int i = 0; i < n; i++) {
                                        onlineList[i] = dis.readUTF();
                                        String[] font = output[4].split("#");
                                        chatRoom.infoTransfer(output[3], output[1], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
                                    }
                                    flag.onlineList = onlineList;
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
                        case "200":
                            if (output[1].equals(flag.curToUsername)) {
                                String[] font = output[4].split("#");
                                chatRoom.infoTransfer(output[3], output[1], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], false);
                            break;
                        case "201":
                            if (output[1].equals(flag.curToUsername)) {
                                String[] font = output[4].split("#");
                                chatRoom.infoTransfer(output[3], output[1], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4]);
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
//                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], false);
                            break;
                    }
                    flag.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
