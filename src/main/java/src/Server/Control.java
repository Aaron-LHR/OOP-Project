package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Control extends Thread{
    Socket socket;
    String username;
    public Control(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        super.run();
        try {
            InputStream inputStream=socket.getInputStream();
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            DataInputStream in = new DataInputStream(inputStream);
            while (socket.isConnected()){
                String buff=null;
                System.out.println("等待输入数据");
                buff= in.readUTF();
                synchronized (socket) {
                    System.out.println("Control receive:" + buff);

                    if (buff.charAt(0) == '*' && buff.charAt(1) == '*') {//注册:**name##passwd##
                        String[] tmp = buff.split("##");
                        tmp[0] = tmp[0].substring(2);
                        new Register(tmp[0], tmp[1], socket).act();
                    }

                    if (buff.charAt(0) == '!' && buff.charAt(1) == '!') {//登录:!!name##passwd##
                        String[] tmp = buff.split("##");
                        tmp[0] = tmp[0].substring(2);
                        username = tmp[0];
                        new Login(tmp[0], tmp[1], socket).act();
                    }

                    if (buff.charAt(0) == '?' && buff.charAt(1) == '?') {//检查对方登录状态:??name
                        String tmp = buff;
                        tmp = tmp.substring(2);
                        if (Server.online.get(tmp) != null) {
                            //用户在线，返回0
                            out.writeUTF("@" + tmp + "@100@0");
                            out.flush();
                        } else {
                            //不在线，返回1
                            out.writeUTF("@" + tmp + "@100@1");
                            out.flush();
                        }

                    }

                    if (buff.charAt(0) == '@') {//发送信息：@fromUser@ToUser@content
                        String[] tmp = buff.split("@");
                        new Send(tmp[1], tmp[2], tmp[3], tmp[4], socket).act();
                    }

                    if (buff.charAt(0) == '-' && buff.charAt(1) == '-') {//注销：--username
                        String tmp = buff;
                        tmp = tmp.substring(2);
                        if (Server.online.get(tmp) == null) {
                            //用户已下线，注销错误
                            System.out.println("用户已下线，注销错误");
                            out.writeUTF("@name@2@1");
                            out.flush();
                        } else {
                            //完成注销
                            out.writeUTF("@name@2@0");
                            out.flush();
                        }
                    }

                    if (buff.indexOf("##ADDGROUP##") == 0) {//创建群聊：##ADDGROUP##groupname##(name1)##name2##name3
                        buff = buff.substring(12);
                        String[] tmp = buff.split("##");
                        new AddGroup(tmp, socket).act();
                    }

                    if (buff.indexOf("##GROUPON##") == 0) {//激活群聊：##GROUPON##群名+群主
                        String name = buff.substring(11);
                        GroupShow gs = new GroupShow(name, socket);//只负责展示群信息
                        gs.act();
                    }
                    if (buff.indexOf("##GROUPCHAT##") == 0) {
                        new GroupChat(buff, socket).act();
                    }

                    if (buff.indexOf("##LIST") == 0) {//在线列表：##LIST
                        List<String> list = searchGroup(username);
                        int t = Server.online.size() + list.size();//在线人数和群的个数之和
                        out.writeUTF("@" + t + "@105@0");
                        for (String i : Server.online.keySet()) {
                            out.writeUTF(i);
                        }

                        for (String i : list) {
                            out.writeUTF(i);
                        }
                    }
                    if (buff.indexOf("##QUITGROUP##") == 0) {
                        new DelGroup(username, buff.substring(13)).act();
                    }
                    if (buff.indexOf("##GList##") == 0) {
                        String tmp = buff.substring(9);
                        File f = new File("Group/" + tmp);
                        FileInputStream fi = new FileInputStream(f);
                        InputStreamReader isr = new InputStreamReader(fi, StandardCharsets.UTF_8);
                        BufferedReader br = new BufferedReader(isr);
                        Server.groupLock.get(f.getName()).readLock().lock();
                        String[] s = br.readLine().split(" ");//群成员列表
                        Server.groupLock.get(f.getName()).readLock().unlock();
                        String tmp1 = "@" + s.length + "@107@0@" + s[0];
                        int cnt = s.length;
                        for (int i = 1; i < cnt; i++) {
                            tmp1 = tmp1 + "#" + s[i];
                        }
                        out.writeUTF(tmp1);
                    }
                    if (buff.indexOf("##FILE##")==0){//传输文件##FILE##toUser##filename##n(按byte[1024]划分的长度)
                        new SendFile(username,buff.substring(8).split("##")[0],buff.substring(8).split("##")[1],Long.parseLong(buff.substring(8).split("##")[2]),socket).act();
                    }
                }
            }


        }catch (Exception e){
            System.out.println(username+"用户断开链接，已自动注销");
            Server.online.remove(username);
        }
    }
    public static List<String> searchGroup(String name){//返回该成员所在的所有群名
        List<String> list=new ArrayList<>();
        File file = new File("Group/");
        File[] fs = file.listFiles();
        for(File f:fs){
            if(!f.isDirectory()) {
                try {
                    FileInputStream fi=new FileInputStream(f);
                    InputStreamReader isr=new InputStreamReader(fi, StandardCharsets.UTF_8);
                    BufferedReader br=new BufferedReader(isr);
                    Server.groupLock.get(f.getName()).readLock().lock();
                    String[] s=br.readLine().split(" ");
                    for (String i:s){
                        if (i.equals(name))
                            list.add(f.getName());
                    }
                    Server.groupLock.get(f.getName()).readLock().unlock();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return list;
    }
}
