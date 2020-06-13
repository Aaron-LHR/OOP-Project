package src.Server;

import java.io.*;
import java.net.Socket;

public class Register {
    public String username;
    private String passwd;
    private Socket socket;
    public Register(String username,String passwd,Socket socket) {
        this.username=username;
        this.passwd=passwd;
        this.socket=socket;
    }
    public void act() {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            FileWriter writer = new FileWriter(new File("Account"), true);
            if (Server.account.get(username) != null) {
                //表中有重复用户名
                Server.account.put(username, passwd);
                out.writeUTF("@name@1@1");
            } else {
                writer.write("\n" + username + " " + passwd);
                writer.close();
                out.writeUTF("@name@1@0");
                out.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
