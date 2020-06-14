package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 负责处理发起群聊的类，创建群聊后，在Group目录下生成一个包含群成员用户名的文件
 * 以"群：(群聊名)+(群主名)"存储，改文件名用于标识唯一的群聊，当一个人试图创建两个同名群时就会失败
 * @author Yzx
 */
public class AddGroup{
    public String[] member;//groupname,(name1),name2,name3
    public Socket socket;

    /**
     * 构造方法
     * @param member 群成员的用户名，其中member[0]为群主名
     * @param socket 发起群聊端的套接字
     */
    public AddGroup(String[] member,Socket socket){
        this.member=member;
        this.socket=socket;
    }

    /**
     * 启动函数
     */
    public void act() {
        DataOutputStream out= null;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        StringBuffer sum=new StringBuffer();

        for (int i=1;i<member.length;i++){
            sum.append(member[i]);
        }
        File groupfile=new File("Group/"+member[0]+member[1]);
        if (!groupfile.exists()){
            try {
                groupfile.createNewFile();
                Server.groupLock.put(member[0]+member[1],new ReentrantReadWriteLock());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                out.writeUTF("@"+member[0]+member[1]+"@102@1");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            FileOutputStream fos=new FileOutputStream(groupfile);
            OutputStreamWriter osw=new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            String tmp=member[1].replace("(","");
            tmp=tmp.replace(")","");//群主去括号存储
            osw.write(tmp);
            for(int i=2;i<member.length;i++){
                osw.write(" "+member[i]);
            }
            osw.close();
            out.writeUTF("@"+member[0]+member[1]+"@102@0");
            System.out.println("群聊初始化完成");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
