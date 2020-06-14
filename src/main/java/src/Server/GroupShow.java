package src.Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 激活群聊的类，当用户点击群聊发起会话时，该类负责处理
 * 主要功能是将Group目录下对应的群聊历史文件读取出来，依据协议向客户端发送消息
 * @author Yzx
 */
public class GroupShow{
    Socket socket;
    String name;

    /**
     * 构造方法
     * @param name 想要激活的群聊名
     * @param socket 对应套接字
     */
    public GroupShow(String name,Socket socket){//##GROUPSHOW##群名+群主
        this.socket=socket;
        this.name=name;
    }

    /**
     * 启动函数
     */
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
