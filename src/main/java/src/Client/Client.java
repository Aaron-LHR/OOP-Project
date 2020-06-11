package src.Client;


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

    public int Login(String username_tmp, String password_tmp) throws IOException, InterruptedException {
        dos.writeUTF("!!" + username_tmp + "##" + password_tmp + "##");
        dos.flush();
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            switch (runFlag.login) {
                case 0:
                    System.out.println("登录成功");
                    username = username_tmp;
//            password = password_tmp;
                    runFlag.modify = false;
                    File f = new File(username + "##" + username + "##Record");
                    f.createNewFile();
                    return 0;
                case 1:
                    System.out.println("用户名或密码错误，请重试");
                    runFlag.modify = false;
                    return 1;
                case 2:
                    System.out.println("该用户已登录");
                    runFlag.modify = false;
                    return 2;
                default:
                    return 1;
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

    public static void saveRecord(String localUsername, String toUsername, String content, String font, boolean direction) throws IOException { //direction==true:send   direction==false:receive
        FileOutputStream fileOutputStream = new FileOutputStream(localUsername + "##" + toUsername + "##Record", true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        String s;
        if (direction) {
            s = localUsername + "@" + content + "@" + font + "\n";
        }
        else {
            s = toUsername + "@" + content + "@" + font + "\n";
        }
        outputStreamWriter.write(s, 0, s.length());
        outputStreamWriter.flush();
        outputStreamWriter.close();
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
        inputStreamReader.close();
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

    public boolean sendPrivateMessage(String ToUsername, String s, String font) throws IOException, InterruptedException {
        dos.writeUTF("@" + username + "@" + ToUsername + "@" + s + "@" + font);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendPrivateMessage == 0) {
                saveRecord(username, ToUsername, s, font, true);
                runFlag.modify = false;
                return true;
            }
            else {
                runFlag.modify = false;
                return false;
            }
        }
    }

    public boolean sendGroupMessage(String ToUsername, String s, String font) throws IOException, InterruptedException {
        dos.writeUTF("##GROUPCHAT##" + ToUsername + "##" + username + "##" + s + "##" + font); //##GROUPCHAT##name(发送方用户名)##groupname ##content##字体
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendGroupMessage == 0) {
//                saveRecord(username, ToUsername, s, font, true);
                runFlag.modify = false;
                return true;
            }
            else {
                runFlag.modify = false;
                return false;
            }
        }
    }

    public void activateGroup(String name) throws IOException, InterruptedException {
        dos.writeUTF("##GROUPON##" + name);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            runFlag.modify = false;
        }
    }

    public String[] getOnlineList() throws IOException, InterruptedException {
        dos.writeUTF("##LIST");
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            runFlag.modify = false;
            return runFlag.getOnlineList();
        }
    }

    public boolean createGroup(String groupName, String host, String[] usernames) throws IOException, InterruptedException {
        StringBuilder s = new StringBuilder("");
        for (String t : usernames) {
            if (!t.equals(""))
            s.append("##");
            s.append(t);
        }
        dos.writeUTF("##ADDGROUP" + "##群：" + groupName + "##(" + host + ")" + s.toString());
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.createGroup == 0) {
                System.out.println("创建群聊成功");
                runFlag.modify = false;
                return true;
            }
            else{
                System.out.println("群聊已存在");
                runFlag.modify = false;
                return false;
            }
        }
    }

    public boolean exitGroup(String groupName) throws IOException, InterruptedException {
        dos.writeUTF("");
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.exitGroup == 0) {
                System.out.println("退出群聊成功");
                runFlag.modify = false;
                return true;
            }
            else{
                System.out.println("退出群聊失败");
                runFlag.modify = false;
                return false;
            }
        }
    }

    public String[] getGroupMembers(String groupName) throws IOException, InterruptedException {
        dos.writeUTF("");
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            runFlag.modify = false;
            return runFlag.getGroupMember();
        }
    }
}
