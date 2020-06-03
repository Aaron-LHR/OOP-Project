package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Send extends Thread {
    private String toUser;
    private String fromUser;
    private String content;
    private Socket socket;
    public Send(String toUser, String content, String fromUser,Socket socket){
        this.toUser=toUser;
        this.fromUser=fromUser;
        this.content=content;
        this.socket=socket;
    }
    @Override
    public void run() {
        super.run();

        try {
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            DataInputStream in=new DataInputStream(socket.getInputStream());
            if (Server.online.get(fromUser)==null){
                out.writeUTF("1");
                out.flush();
            }
            else {
                Socket send=new Socket(Server.online.get(toUser),Server.port);
                DataOutputStream sout=new DataOutputStream(send.getOutputStream());
                String msg="@"+fromUser+"@"+content;
                sout.writeUTF(msg);
                System.out.println("发送:"+msg);
                sout.flush();
                out.writeUTF("0");
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
