package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;

public class Control extends Thread{
    Socket socket;
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
                    e.printStackTrace();
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
                    int t=tmp[1].length()+tmp[2].length()+3;
                    String content= buff.substring(t);
                    new Send(tmp[1],tmp[2],content,socket).start();
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

                if (buff.indexOf("##ADDGROUP##")==0){//创建群聊：##ADDGROUP##groupname##name1##name2##name3
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
                    int t=Server.online.size();
                    out.writeUTF("@"+t+"@105@0");
                    for (String i:Server.online.keySet()){
                        out.writeUTF(i);
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
