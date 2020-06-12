package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GroupShow{
    Socket socket;
    String name;
    public GroupShow(String name,Socket socket){//##GROUPSHOW##群名+群主
        this.socket=socket;
        this.name=name;
    }
    public void act() {
        try {
            OutputStream outputStream=socket.getOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            File group=new File("Group/"+name);

            Server.groupLock.get(name).readLock().lock();
            FileInputStream fi=new FileInputStream(group);
            InputStreamReader isr=new InputStreamReader(fi, StandardCharsets.UTF_8);
            BufferedReader br=new BufferedReader(isr);
            List<String > s=new ArrayList<>();
            String tmp=null;
            while ((tmp=br.readLine())!=null){
                s.add(tmp);
            }

            Server.groupLock.get(name).readLock().unlock();
            out.writeUTF("@"+(s.size()-1)+"@103@0");
            for (int i=1;i<s.size();i++){
                out.writeUTF(s.get(i));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
