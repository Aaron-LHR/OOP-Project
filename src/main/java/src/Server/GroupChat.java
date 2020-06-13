package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChat {
    Socket socket;
    String buff;
    public GroupChat(String buff, Socket socket){//群聊：##GROUPCHAT##群聊名字+群主用户名##name(发送方用户名)##content##font
        this.socket=socket;
        this.buff=buff;
    }

    public void act() {
        try {
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            String[] tmp;
            tmp=buff.split("##");
            File group=new File("Group/"+tmp[2]);

            if (tmp.length!=6){
                out.writeUTF("@"+tmp[3]+"@104@2");//传输格式出现错误
                out.flush();
                System.out.println("传输格式有误");
                return;
            }

            if (!group.exists()){
                out.writeUTF("@"+tmp[3]+"@104@1");//群聊不存在，可能已被删除
                out.flush();
                System.out.println("群聊不存在");
                return;
            }

            Server.groupLock.get(tmp[2]).writeLock().lock();
            FileOutputStream fos=new FileOutputStream(group,true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            bw.write(Server.chgl);
            bw.write("@"+tmp[2]+"@201@" + tmp[3] + "@" + tmp[4]+ "@"+tmp[5]+"@"+df.format(new Date()));
                //在文件中：以@群聊名字+群主用户名@201@name(发送方用户名)@content@字体@时间 存储
            bw.close();
            Server.groupLock.get(tmp[2]).writeLock().unlock();

            Server.groupLock.get(tmp[2]).readLock().lock();
            FileInputStream fi=new FileInputStream(group);
            InputStreamReader isr=new InputStreamReader(fi, StandardCharsets.UTF_8);
            BufferedReader br=new BufferedReader(isr);
            String[] s=br.readLine().split(" ");

            for (String i:s){
                Socket s1=Server.online.get(i);
                if (s1!=null){
                    synchronized (s1){
                        DataOutputStream out1 = new DataOutputStream(s1.getOutputStream());
                        out1.writeUTF("@"+tmp[2]+"@201@"+tmp[3]+"@"+tmp[4]+"@"+tmp[5]+"@");
                    }

                }
            }

            Server.groupLock.get(tmp[2]).readLock().unlock();

            out.writeUTF("@"+tmp[3]+"@104@0");//发送成功

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
