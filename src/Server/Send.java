package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Send extends Thread {//@fromUser@content
    private String toUser;
    private String fromUser;
    private String content;
    private Socket socket;
    public Send(String fromUser, String toUser, String content, Socket socket){
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
            if (Server.online.get(toUser)==null){
                System.out.println(toUser+"未上线");
                out.writeUTF("1");
                out.flush();
            }
            else {
                Socket send=Server.online.get(toUser);
                DataOutputStream sout=new DataOutputStream(send.getOutputStream());
                String msg="@"+fromUser+"@"+content;
                sout.writeUTF(msg);
                System.out.println("发送至"+toUser+":"+msg);
                sout.flush();
                out.writeUTF("0");
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
