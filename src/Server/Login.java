package Server;
import java.io.*;
import java.net.*;

public class Login extends Thread {
    public String username;
    private String passwd;
    private Socket socket;
    public Login(String username,String passwd,Socket socket) {
        this.username=username;
        this.passwd=passwd;
        this.socket=socket;
    }
    @Override
    public void run() {

        try {
                DataOutputStream out=new DataOutputStream(socket.getOutputStream());
                DataInputStream in=new DataInputStream(socket.getInputStream());
                if(Server.account.get(username)!=null&&Server.account.get(username).equals(passwd)){
                    out.writeUTF("@name@0@0");
                    InetAddress inetAddress=socket.getInetAddress();
                    System.out.println(username+":"+inetAddress.getHostAddress()+":online!");

                    Server.online.put(username,socket);
                }
                else {
                    out.writeUTF("@name@0@1");
                    out.flush();
                }
            }
            catch (IOException e) {
            e.printStackTrace();
        }
    }
}
