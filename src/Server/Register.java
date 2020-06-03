package Server;

import java.io.*;
import java.net.Socket;

public class Register extends Thread{
    public String username;
    private String passwd;
    private Socket socket;
    public Register(String username,String passwd,Socket socket) {
        this.username=username;
        this.passwd=passwd;
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            DataOutputStream out=new DataOutputStream(socket.getOutputStream());
            DataInputStream in=new DataInputStream(socket.getInputStream());
            FileWriter writer = new FileWriter(new File("Account"), true);
            if (Server.account.get(username)!=null){
                //表中没有重复用户名
                Server.account.put(username,passwd);
                out.writeUTF("1");
            }
            writer.write("\n"+username+" "+passwd);
            writer.close();
            out.writeUTF("0");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
