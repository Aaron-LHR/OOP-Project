package src.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 负责传输私聊信息的类
 * 在获取接收端的锁后向接收端发送信息，
 * 向发送端返回信息，具体传输信息见协议
 * @author Yzx
 */
public class Send{//@fromUser@content
    private String toUser;
    private String fromUser;
    private String content;
    private String font;
    private Socket socket;

    /**
     * 构造方法
     * @param fromUser 发送方用户名
     * @param toUser 接收方用户名
     * @param content 传输内容
     * @param font 聊天字体
     * @param socket 对应套接字
     */
    public Send(String fromUser, String toUser, String content, String font,Socket socket){
        this.toUser=toUser;
        this.fromUser=fromUser;
        this.content=content;
        this.socket=socket;
        this.font=font;
    }

    /**
     * 启动函数
     */
    public void act() {
        try {
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            DataInputStream in=new DataInputStream(socket.getInputStream());
            if (Server.online.get(toUser)==null){
                System.out.println(toUser+"未上线");
                out.writeUTF("@"+toUser+"@101@1");
                out.flush();
            }
            else {
                Socket send=Server.online.get(toUser);
                DataOutputStream sout=new DataOutputStream(send.getOutputStream());
                out.writeUTF("@"+toUser+"@101@0");
                String msg="@"+fromUser+"@200@"+content+"@"+font+"@";
                synchronized (send){
                    sout.writeUTF(msg);
                }
                System.out.println("发送至"+toUser+":"+msg);
                sout.flush();
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
