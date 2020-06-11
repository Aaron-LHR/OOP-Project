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
        while (socket.isConnected()){
            try {
                InputStream inputStream=socket.getInputStream();
                OutputStream outputStream=socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
                DataInputStream in = new DataInputStream(inputStream);
                String buff=null;
                try {
                    buff= in.readUTF();
                }catch (Exception e){
                    System.out.println(username+"用户断开链接，已自动注销");
                    Server.online.remove(username);
                    return;
                }

                System.out.println("Control receive:"+buff);

                if (buff.charAt(0)=='*'&&buff.charAt(1)=='*'){//注册:**name##passwd##
                    String[] tmp=buff.split("##");
                    tmp[0]=tmp[0].substring(2);
                    new Register(tmp[0],tmp[1],socket).start();
                }

                if (buff.charAt(0)=='!'&&buff.charAt(1)=='!'){//登录:!!name##passwd##
                    String[] tmp=buff.split("##");
                    tmp[0]=tmp[0].substring(2);
                    username=tmp[0];
                    new Login(tmp[0],tmp[1],socket).start();
                }

                if (buff.charAt(0)=='?'&&buff.charAt(1)=='?'){//检查对方登录状态:??name
                    String tmp=buff;
                    tmp=tmp.substring(2);
                    if (Server.online.get(tmp)!=null){
                        //用户在线，返回0
                        out.writeUTF("@"+tmp+"@100@0");
                        out.flush();
                    }
                    else {
                        //不在线，返回1
                        out.writeUTF("@"+tmp+"@100@1");
                        out.flush();
                    }
                }

                if (buff.charAt(0)=='@'){//发送信息：@fromUser@ToUser@content
                    String[] tmp=buff.split("@");

                    new Send(tmp[1],tmp[2],tmp[3],tmp[4],socket).start();
                }

                if (buff.charAt(0)=='-'&&buff.charAt(1)=='-'){//注销：--username
                    String tmp=buff;
                    tmp=tmp.substring(2);
                    if (Server.online.get(tmp)==null){
                        //用户已下线，注销错误
                        System.out.println("用户已下线，注销错误");
                        out.writeUTF("@name@2@1");
                        out.flush();
                    }
                    else {
                        //完成注销
                        out.writeUTF("@name@2@0");
                        out.flush();
                    }
                }

                if (buff.indexOf("##ADDGROUP##")==0){//创建群聊：##ADDGROUP##groupname##(name1)##name2##name3
                    buff=buff.substring(12);
                    String[] tmp=buff.split("##");
                    new AddGroup(tmp,socket).start();
                }

                if (buff.indexOf("##GROUPON##")==0){//激活群聊：##GROUPON##群名+群主
                    String name=buff.substring(11);
                    GroupShow gs=new GroupShow(name,socket);//只负责展示群信息
                    gs.start();
                }
                if (buff.indexOf("##GROUPCHAT##")==0){
                    new GroupChat(buff,socket).start();
                }

                if (buff.indexOf("##LIST")==0){//在线列表：##LIST
                    List<String> list = searchGroup(username);
                    int t=Server.online.size()+list.size();//在线人数和群的个数之和
                    out.writeUTF("@"+t+"@105@0");
                    for (String i:Server.online.keySet()){
                        out.writeUTF(i);
                    }

                    for (String i:list){
                        out.writeUTF(i);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }


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
                    String[] s=br.readLine().split(" ");
                    for (String i:s){
                        if (i.equals(name))
                            list.add(f.getName());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        return list;
    }
}
