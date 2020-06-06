package Client;

import java.io.DataInputStream;
import java.io.IOException;

public class ReceiveThread implements Runnable {
    private DataInputStream dis;
    Flag flag;

    public ReceiveThread(DataInputStream dis, Flag flag) {
        this.dis = dis;
        this.flag = flag;
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

                        case "104":

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

                    }
                    flag.notify();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
