package src.Client;

import src.UI.chatRoom;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

public class ReceiveThread implements Runnable {
    private Client client = Client.getInstance();
    private DataInputStream dis = client.getDis();
    private DataOutputStream dos = client.getDos();
    src.UI.chatRoom chatRoom;
    Flag flag = Flag.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式

    public ReceiveThread(src.UI.chatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }



    private void showMessage() throws IOException {
        String[] onlineList = dis.readUTF().split("@");
        String[] font = onlineList[5].split("#");
        chatRoom.infoTransfer(onlineList[4], onlineList[3], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4], df.format(new Date()));
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
                        case "0":   //登录成功：@name@0@0 登录失败：@name@0@1 重复登录：@name@0@2
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
                        case "1":   //注册成功：@name@1@0 注册失败：@name@1@1
                            switch (output[3]) {
                                case "0":
                                    flag.register=0;
                                    break;
                                case "1":
                                    flag.register=1;
                                    break;
                            }
                            break;
                        case "2":   //注销成功：@name@2@0注销失败：@name@2@1
                            switch (output[3]) {
                                case "0":
                                    flag.logout=0;
                                    break;
                                case "1":
                                    flag.logout=1;
                                    break;
                            }
                            break;
                        case "100": //检查对方在线状态  在线：@Toname@100@0 不在线：@Toname@100@1
                            switch (output[3]) {
                                case "0":
                                    flag.checkOnline=0;
                                    break;
                                case "1":
                                    flag.checkOnline=1;
                                    break;
                            }
                            break;
                        case "101": //私聊发消息 成功送达：@Toname@101@0 不在线：@Toname@101@1
                            switch (output[3]) {
                                case "0":
                                    flag.sendPrivateMessage=0;
                                    break;
                                case "1":
                                    flag.sendPrivateMessage=1;
                                    break;
                            }
                            break;
                        case "102": //创建群聊  成功：@群聊名字+群主用户名@102@0 群聊已存在：@群聊名字+群主用户名@102@1
                            switch (output[3]) {
                                case "0":
                                    flag.createGroup=0;
                                    break;
                                case "1":
                                    flag.createGroup=1;
                                    break;
                            }
                            break;
                        case "103": //激活群聊  首先返回历史记录总数@n@103@0（String类型），然后发送n个字符串，每条字符串为：@群聊名字+群主用户名@201@name(发送方用户名)@content@字体
                            switch (output[3]) {
                                case "0":
                                    flag.activateGroup=0;
                                    int n = Integer.parseInt(output[1]);
                                    for (int i = 0; i < n; i++) {
                                        output = dis.readUTF().split("@");
                                        if (Pattern.matches("!!\\((.*?)\\)!!", output[4]) && output[5].trim().contains("emoji")) {
                                            chatRoom.imgTransfer(output[3], output[4].substring(3, output[4].length()-3), output[6]);
                                        }
                                        else {
                                            String[] font = output[5].split("#");
                                            chatRoom.infoTransfer(output[4], output[3], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4], output[6]);
                                        }
//                                        showMessage();
                                    }
                                    break;
                            }
                            break;
                        case "104": //发送群聊消息    成功：@name@104@0，群聊不存在：@name@104@1，传输格式有误：@name@104@2
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
                        case "105": //在线列表  首先返回在线人数@n@105@0（String），然后返回n个字符串，每个字符串为用户名（前后没有#）
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
                        case "106": //退出群聊  退出成功 @groupname@106@0   退出失败 @groupname@106@1（用户没有未加入该群）
                            switch (output[3]) {
                                case "0":
                                    flag.exitGroup=0;
                                    break;
                                case "1":
                                    flag.exitGroup=1;
                                    break;
                            }
                            break;
                        case "107": //群聊成员列表    @n@107@0@name1#name2
                            switch (output[3]) {
                                case "0":
                                    flag.groupMember = output[4].split("#");
                                    break;
                            }
                            break;
                        case "108": //发送文件  成功：@filename@108@0失败：@filename@108@1
                            switch (output[3]) {
                                case "0":
                                    flag.sendFile=0;
                                    break;
                                case "1":
                                    flag.sendFile=1;
                                    break;
                            }
                            break;
                        case "200": //接收私聊消息    @fromUser@200@content@字体
                            flag.modify = false;
                            if (output[1].equals(flag.curToUsername)) {
                                if (Pattern.matches("!!\\((.*?)\\)!!", output[3]) && output[4].trim().equals("emoji")) {
                                    chatRoom.imgTransfer(output[1], output[3].substring(3, output[3].length()-3), df.format(new Date()));
                                }
                                else {
                                    String[] font = output[4].split("#");
                                    chatRoom.infoTransfer(output[3], output[1], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4], df.format(new Date()));
                                }
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], df.format(new Date()), false);
                            new Thread(new SendThread(chatRoom)).start();
                            break;
                        case "201": //接收群聊消息    @群聊名字+群主用户名@201@name(发送方用户名)@content@字体
                            flag.modify = false;
                            if (output[1].equals(flag.curToUsername)) {
                                if (Pattern.matches("!!\\((.*?)\\)!!", output[4]) && output[5].trim().equals("emoji")) {
                                    chatRoom.imgTransfer(output[3], output[4].substring(3, output[4].length()-3), df.format(new Date()));
                                }
                                else {
                                    String[] font = output[5].split("#");
                                    chatRoom.infoTransfer(output[4], output[3], font[0], Integer.parseInt(font[1]), Integer.parseInt(font[2]), font[3], font[4], df.format(new Date()));
                                }
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            new Thread(new SendThread(chatRoom)).start();
//                            Client.saveRecord(Client.getUsername(), output[1], output[3], output[4], false);
                            break;
                        case "202":
                            flag.modify = false;
                            File file = new File("Files" + File.separator + output[3]);
                            if (file.exists()) {
                                file.delete();
                            }
                            FileOutputStream fos = new FileOutputStream("Files" + File.separator + output[3], true);
                            int n = Integer.parseInt(output[4]);
                            byte[] bytes = new byte[1024];
                            int length = 0;
                            while (n > 0) {
                                length = dis.read(bytes, 0, bytes.length);
                                fos.write(bytes, 0, length);
                                System.out.println(Arrays.toString(bytes));
                                fos.flush();
                                n--;
                            }
                            flag.modify = false;
                            if (output[1].equals(flag.curToUsername)) {
                                chatRoom.infoTransfer("接收文件:" + output[3], output[1], "宋体", 0, 12, "黑色", "无色", df.format(new Date()));
                            }
                            else {
                                chatRoom.infoReminder(output[1], true);
                            }
                            Client.saveRecord(Client.getUsername(), output[1], "接收文件:" + output[3], "宋体#0#12#黑色#无色", df.format(new Date()), false);
                            new Thread(new SendThread(chatRoom)).start();
                            fos.close();
                            break;
                    }
                    flag.notify();
                }
            } catch (IOException | InterruptedException | BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}
