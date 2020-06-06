package Client;


import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class Client {
    private Flag runFlag = Flag.getInstance();
    private String username;
//    private String password;
    BufferedReader input;
    private String IP;
    private int port;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private static Client client;

    static {
        try {
            client = new Client("localhost",1111);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Client getInstance() {
        return client;
    }

    private Client(String IP, int port) throws IOException {
        this.IP = IP;
        this.port = port;
        socket = new Socket(IP,port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    public boolean Login(String username_tmp, String password_tmp) throws IOException, InterruptedException {
        dos.writeUTF("!!" + username_tmp + "##" + password_tmp + "##");
        dos.flush();
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.login == 0) {
                System.out.println("登录成功");
                username = username_tmp;
//            password = password_tmp;
                runFlag.modify = false;
                return true;
            }
            else{
                System.out.println("登录失败，请重试");
                runFlag.modify = false;
                return false;
            }
        }
    }

    public boolean register(String username_tmp, String password_tmp) throws IOException, InterruptedException {
        dos.writeUTF("**" + username_tmp + "##" + password_tmp + "##");
        dos.flush();
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.register == 0) {
                System.out.println("注册成功");
                runFlag.modify = false;
                return true;
            }
            else{
                System.out.println("注册失败，请重试");
                runFlag.modify = false;
                return false;
            }
        }
    }

    public void exit() throws IOException {
        dos.writeUTF("--" + username);
        input.close();
        dos.close();
        dis.close();
        socket.close();
    }

    public Flag getRunFlag() {
        return runFlag;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getUsername() {
        return username;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public boolean send(String ToUsername, String s) throws IOException, InterruptedException {
        dos.writeUTF("@" + username + "@" + ToUsername + "@" + s);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendPrivateMessage == 0) {
                return true;
            }
            else {
                return false;
            }
        }
    }

    public String receive() throws IOException {
//        String ret = dis.readUTF();
//        if (Pattern.matches("@*@*@", ret))
//        return "0" + ret;   //返回消息
//        else if ()
//        return "";
        return dis.readUTF();
    }
}
