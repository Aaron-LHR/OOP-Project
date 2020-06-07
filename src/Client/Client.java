package Client;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Flag runFlag = Flag.getInstance();
    private static String username;
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

    public static void saveRecord(String localUsername, String toUsername, String content, String font) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(localUsername + "##" + toUsername + "##Record", true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        outputStreamWriter.write(content + "@" + font + "\n", 0, content.length());
    }

    public static List<String> readRecord(String localUsername, String toUsername) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(localUsername + "##" + toUsername + "##Record");
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        int c, i = 0;
        char[] line = new char[1024];
        List<String> list= new ArrayList<>();
        while ((c = inputStreamReader.read())!=-1) {
            if ((char) c == '\n') {
                list.add(new String(line));
                i = 0;
            }
            else {
                line[i++] = (char) c;
            }
        }
        return list;
    }

    public void exit() throws IOException, InterruptedException {
        dos.writeUTF("--" + username);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.logout == 0) {
                System.out.println("注销成功");
                runFlag.modify = false;
            }
            else{
                System.out.println("注销失败");
                runFlag.modify = false;
            }
        }
        input.close();
        dos.close();
        dis.close();
        socket.close();
    }

    public Flag getRunFlag() {
        return runFlag;
    }

    public static void setUsername(String username) {
        Client.username = username;
    }

//    public void setPassword(String password) {
//        this.password = password;
//    }

    public static String getUsername() {
        return username;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public boolean send(String ToUsername, String s, String font) throws IOException, InterruptedException {
        dos.writeUTF("@" + username + "@" + ToUsername + "@" + s + "@" + font);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendPrivateMessage == 0) {
                saveRecord(username, ToUsername, s, font);
                return true;
            }
            else {
                return false;
            }
        }
    }

    public String[] getOnlineList() throws IOException, InterruptedException {
        dos.writeUTF("##LIST");
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            return runFlag.getOnlineList();
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
