package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class AddGroup extends Thread{
    public String[] member;//groupname,(name1),name2,name3
    public Socket socket;
    public AddGroup(String[] member,Socket socket){
        this.member=member;
        this.socket=socket;
    }

    @Override
    public void run() {
        super.run();
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
