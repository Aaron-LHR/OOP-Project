package Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GroupShow extends Thread{
    Socket socket;
    String name;
    public GroupShow(String name,Socket socket){//##GROUPSHOW##群名+群主
        this.socket=socket;
        this.name=name;
    }
    @Override
    public void run() {
        super.run();
        try {
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            File group=new File("Group/"+name);

            Server.groupLock.get(name).readLock().lock();
            FileInputStream fi=new FileInputStream(group);
            InputStreamReader isr=new InputStreamReader(fi,"UTF_8");
            BufferedReader br=new BufferedReader(isr);
            List<String > s=new ArrayList<>();
            String tmp=null;
            while ((tmp=br.readLine())!=null){
                s.add(tmp);
            }

            Server.groupLock.get(name).readLock().unlock();
            out.writeUTF("@"+s.size()+"@103@0");
            for (String i:s){
                out.writeUTF(i);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
