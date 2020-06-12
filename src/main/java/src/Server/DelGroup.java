package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DelGroup{
    String username;
    String groupname;
    public DelGroup(String username,String groupname){
        this.username=username;
        this.groupname=groupname;
    }

    public void act(){
        try {
            List<String> list=Control.searchGroup(username);
            Socket socket=Server.online.get(username);
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            File f=new File("Group/"+groupname);
            List<String> tmp=new ArrayList<>();//用于暂时保存群文件
            String t;
            if (!list.contains(groupname)) {
                out.writeUTF("@" + groupname + "@106@1");//用户没有未加入该群
                return;
            }
            Server.groupLock.get(groupname).writeLock().lock();
            FileInputStream fi=new FileInputStream(f);
            InputStreamReader isr=new InputStreamReader(fi, StandardCharsets.UTF_8);
            BufferedReader br=new BufferedReader(isr);
            while ((t=br.readLine())!=null){//读入所有文件
                tmp.add(t);
            }
            String[] ss=tmp.get(0).split("\\s+");
            String k="";
            for (int i=0;i<ss.length;i++){
                if (!ss[i].equals(username)) k=k+ss[i]+" ";
            }

            tmp.add(0,k);
            tmp.remove(1);

            FileOutputStream fos=new FileOutputStream(f,false);//重新写一遍文件
            OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            for (String s:tmp){
                bw.write(s);
                bw.newLine();
            }
            bw.close();
            Server.groupLock.get(groupname).writeLock().unlock();
            out.writeUTF("@" + groupname + "@106@0");
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
