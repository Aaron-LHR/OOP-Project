package src.Server;
import java.io.*;
import java.net.*;

/**
 * 负责处理登录请求的类，当输入符合要求的账号密码时，将账号以及对应的socket加入Server
 * 中的online图
 * @author Yzx
 */
public class Login{
    public String username;
    private String passwd;
    private Socket socket;

    /**
     * 构造方法
     * @param username 接收的用户名
     * @param passwd 接收的密码
     * @param socket 对应客户端的套接字
     */
    public Login(String username,String passwd,Socket socket) {
        this.username=username;
        this.passwd=passwd;
        this.socket=socket;
    }

    /**
     * 启动函数
     */
    public void act() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            if (Server.account.get(username) != null && Server.account.get(username).equals(passwd)) {
                if (Server.online.get(username) != null) {
                    out.writeUTF("@name@0@2");//用户已登录
                    return;
                }
                out.writeUTF("@name@0@0");
                InetAddress inetAddress = socket.getInetAddress();
                System.out.println(username + ":" + inetAddress.getHostAddress() + ":online!");

                Server.online.put(username, socket);
            } else {
                out.writeUTF("@name@0@1");
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
