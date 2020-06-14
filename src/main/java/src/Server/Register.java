package src.Server;

import java.io.*;
import java.net.Socket;

/**
 * 负责处理注册请求的类
 * 处理时将符合要求的账号密码写入文件，并加入Server类中的account图
 * @author Yzx
 */
public class Register {
    public String username;
    private String passwd;
    private Socket socket;

    /**
     * 构造函数
     * @param username 注册的用户名
     * @param passwd 密码
     * @param socket 对应套接字
     */
    public Register(String username,String passwd,Socket socket) {
        this.username=username;
        this.passwd=passwd;
        this.socket=socket;
    }

    /**
     * 启动方法
     */
    public void act() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            FileWriter writer = new FileWriter(new File("Account"), true);
            if (Server.account.get(username) != null) {
                //表中有重复用户名
                out.writeUTF("@name@1@1");
            } else {
                Server.account.put(username, passwd);
                writer.write(username + " " + passwd + "\n");
                writer.close();
                out.writeUTF("@name@1@0");
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
