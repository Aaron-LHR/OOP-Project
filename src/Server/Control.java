package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Control extends Thread{
    Socket socket;
    public Control(Socket socket){
        this.socket=socket;
    }
    @Override
    public void run() {
        super.run();
        while (true){
            try {
                InputStream inputStream=socket.getInputStream();
                OutputStream outputStream=socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
                DataInputStream in = new DataInputStream(inputStream);

                String buff = in.readUTF();
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
                        out.writeUTF("0");
                        out.flush();
                    }
                    else {
                        //不在线，返回1
                        out.writeUTF("1");
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
                        out.writeUTF("1");
                        out.flush();
                    }
                    else {
                        //完成注销
                        out.writeUTF("0");
                        out.flush();
                    }
                }

                if (buff.indexOf("##ADDGROUP##")==0){//创建群聊：##ADDGROUP##groupname##name1##name2##name3
                    buff=buff.substring(12);
                    String[] tmp=buff.split("##");
                    new AddGroup(tmp,socket).start();
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}