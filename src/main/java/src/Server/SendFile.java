package src.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFile {
    Socket socket;
    String fromUser,toUser,filename;
    int len,length;
    byte[] bytes=new byte[1024];
    List<byte[]> list=new ArrayList<>();
    public SendFile(String fromUser, String toUser,String filename,int len, Socket socket){
        this.filename=filename;
        this.len=len;
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.socket=socket;
    }
    public void act(){
        try {
            DataOutputStream out=new DataOutputStream(Server.online.get(toUser).getOutputStream());
            DataInputStream in=new DataInputStream(socket.getInputStream());
            synchronized (out){
                out.writeUTF("@"+fromUser+"@202@"+filename+"@"+len);
                for (int i=0;i<len;i++){
                    length=in.read(bytes,0,bytes.length);
                    out.write(bytes,0,length);
                }

            }
            new DataOutputStream(socket.getOutputStream()).writeUTF("@"+filename+"@108@0");
        }catch (Exception e){
            e.printStackTrace();
            try {
                new DataOutputStream(socket.getOutputStream()).writeUTF("@"+filename+"@108@1");
            }catch (Exception e1){}

        }

    }
}
