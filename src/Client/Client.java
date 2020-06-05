package Client;


import java.io.*;
import java.net.Socket;

public class Client {
    private String username;
//    private String password;
    BufferedReader input;
    private String IP;
    private int port;
    private Socket client;
    private DataOutputStream dos;
    private DataInputStream dis;

    public Client(String IP, int port) throws IOException {

        this.IP = IP;
        this.port = port;
        client = new Socket(IP,port);
        dos = new DataOutputStream(client.getOutputStream());
        dis = new DataInputStream(client.getInputStream());
    }

    public boolean Login(String username_tmp, String password_tmp) throws IOException {
//        System.out.println("请输入用户名：");
//        username_tmp = input.readLine();
//        System.out.println("请输入密码：");
//        password_tmp = input.readLine();
        dos.writeUTF("!!" + username_tmp + "##" + password_tmp + "##");
        dos.flush();
//            client.shutdownOutput();
        String ret = dis.readUTF();
        if (ret.equals("0")) {
            System.out.println("登录成功");
            username = username_tmp;
//            password = password_tmp;
            return true;
        }
        else{
            System.out.println("登录失败，请重试");
            return false;
        }
    }

    public boolean register(String username_tmp, String password_tmp) throws IOException {
//        System.out.println("请输入用户名：");
//        username_tmp = input.readLine();
//        System.out.println("请输入密码：");
//        password_tmp = input.readLine();
//        System.out.println("请确认密码：");
//        if (input.readLine().equals(password)) {
//            break;
//        }
//        else {
//            System.out.println("两次密码不一致，请重新输入");
//        }
        dos.writeUTF("**" + username_tmp + "##" + password_tmp + "##");
        dos.flush();
//        client.shutdownOutput();
        String ret = dis.readUTF();
        if (ret.equals("0")) {
            System.out.println("注册成功");
            return true;
        }
        else {
            System.out.println("注册失败");
                return false;
        }

    }

//    public void init() {
//        new Thread(new SendThread(dos, this.username)).start();
//        new Thread(new ReceiveThread(dis)).start();
//    }

    public void exit() throws IOException {
        dos.writeUTF("--" + username);
        input.close();
        dos.close();
        dis.close();
        client.close();
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

    public void send(String ToUsername, String s) throws IOException {
        dos.writeUTF("@" + username + "@" + ToUsername + "@" + s);
    }

    public String receive() throws IOException {
        return dis.readUTF();
    }
}
