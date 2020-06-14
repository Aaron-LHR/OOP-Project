package src.Client;


import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * 客户端的总类，负责与服务端
 * 和UI界面进行交互
 * @author 廉皓然
 */
public class Client {
    /**
     * 线程之间交互信息
     */
    private Flag runFlag = Flag.getInstance();
    /**
     * 当前登录用户名
     */
    private static String username;
//    private String password;
    BufferedReader input;
    private String IP;
    private int port;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
    private static Client client;

    static {
        try {
//            client = new Client("39.97.126.242",1111);
            client = new Client("localhost",1111);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Client getInstance() {
        return client;
    }
    /**
     * 构造方法
     */
    private Client(String IP, int port) throws IOException {
        this.IP = IP;
        this.port = port;
        socket = new Socket(IP,port);
        dos = new DataOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    /**
     * 向服务器发送登录请求
     */
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

    /**
     * 向服务器发送注册请求
     */
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

    /**
     * 在本地保存聊天记录
     */
    public static void saveRecord(String localUsername, String toUsername, String content, String font, String time, boolean direction) throws IOException { //direction==true:send   direction==false:receive
        FileOutputStream fileOutputStream = new FileOutputStream("localRecord" + File.separator + localUsername + "##" + toUsername + "##Record", true);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        String s;
        if (direction) {
            s = localUsername + "@" + content + "@" + font + "@" + time + "@" + "\0";
        }
        else {
            s = toUsername + "@" + content + "@" + font + "@" + time + "@" + "\0";
        }
        outputStreamWriter.write(s, 0, s.length());
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    /**
     * 读取本地聊天记录
     */
    public static List<String> readRecord(String localUsername, String toUsername) throws IOException {
        try {
            FileInputStream fileInputStream = new FileInputStream("localRecord" + File.separator + localUsername + "##" + toUsername + "##Record");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            int c, i = 0;
            char[] line = new char[1024];
            List<String> list= new ArrayList<>();
            while ((c = inputStreamReader.read())!=-1) {
                line[i++] = (char) c;
                if ((char) c == '\0') {
                    list.add(new String(line));
                    i = 0;
                    line = new char[1024];
                }
            }
            inputStreamReader.close();
            return list;
        }catch (FileNotFoundException e) {
            File file = new File("localRecord" + File.separator + localUsername + "##" + toUsername + "##Record");
            return new ArrayList<>();
        }

    }

    /**
     * 关闭客户端
     */
    public void exit() throws IOException, InterruptedException {
//        dos.writeUTF("--" + username);
//        synchronized (runFlag) {
//            while (!runFlag.modify) {
//                runFlag.wait();
//            }
//            if (runFlag.logout == 0) {
//                System.out.println("注销成功");
//                runFlag.modify = false;
//            }
//            else{
//                System.out.println("注销失败");
//                runFlag.modify = false;
//            }
//        }
        if (input!=null)
        input.close();
        if (dos!=null)
        dos.close();
        if (dis!=null)
        dis.close();
        if (socket!=null)
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

    /**
     * 提供用户名
     */
    public static String getUsername() {
        return username;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }

    /**
     * 发送私聊消息
     */
    public boolean sendPrivateMessage(String ToUsername, String s, String font) throws IOException, InterruptedException {
        dos.writeUTF("@" + username + "@" + ToUsername + "@" + s + "@" + font);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendPrivateMessage == 0) {
                saveRecord(username, ToUsername, s, font, df.format(new Date()), true);
                runFlag.modify = false;
                return true;
            }
            else {
                runFlag.modify = false;
                return false;
            }
        }
    }

    /**
     * 发送群聊消息
     */
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

    /**
     * 获取群聊聊天记录
     */
    public void activateGroup(String name) throws IOException, InterruptedException {
        dos.writeUTF("##GROUPON##" + name);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            runFlag.modify = false;
        }
    }

    /**
     * 获取当前在线用户列表
     */
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

    /**
     * 创建群聊
     */
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

    /**
     * 退出群聊
     */
    public boolean exitGroup(String groupName) throws IOException, InterruptedException {
        dos.writeUTF("##QUITGROUP##" + groupName);
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

    /**
     * 获取群聊成员列表
     */
    public String[] getGroupMembers(String groupName) throws IOException, InterruptedException {
        dos.writeUTF("##GList##" + groupName);
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            runFlag.modify = false;
            return runFlag.getGroupMember();
        }
    }

    /**
     * 发送文件
     */
    public boolean sendFile(String ToUsername, File file) throws IOException, InterruptedException {
        long n = (long) Math.ceil(file.length() / 1024.0);
        dos.writeUTF("##FILE##" + ToUsername + "##" + file.getName() + "##" + n);
        FileInputStream fis = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int length = 0;
        while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
            dos.write(bytes, 0, length);
            System.out.println(Arrays.toString(bytes));
            dos.flush();
//            Thread.sleep(200);
        }
        fis.close();
        synchronized (runFlag) {
            while (!runFlag.modify) {
                runFlag.wait();
            }
            if (runFlag.sendFile == 0) {
                System.out.println("发送文件成功");
                saveRecord(username, ToUsername, "发送文件:" + file.getName(), "宋体#0#12#黑色#无色", df.format(new Date()),  true);
                runFlag.modify = false;
                return true;
            }
            else{
                System.out.println("发送文件失败");
                runFlag.modify = false;
                return false;
            }
        }
    }
}
