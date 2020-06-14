package src.Server;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 服务端的总类，每接收一个客户端请求都会
 * 新建一个Control线程负责处理
 * @author Yzx
 */
public class Server {
    public static int port=1111;
    public static char chgl='\n';//部署至服务器时更改
    /**
     * 储存所有账号信息
     */
    public static Map<String,String> account=new HashMap<>(){};//name,password
    /**
     * 储存所有在线用户信息
     */
    public static Map<String,Socket> online=new HashMap<>();//online user
    /**
     * 每个群聊对应一个读写锁
     */
    public static Map<String, ReentrantReadWriteLock> groupLock=new HashMap<>();//每个群聊有一个锁
    private Server(){}

    public static void main(String[] args) {
        init();
        try {
            ServerSocket serverSocket=new ServerSocket(Server.port);

            while (true) {
                System.out.println("Server:等待中。。。");
                Socket socket=serverSocket.accept();
                System.out.println("Server:成功！");
                new Control(socket).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化方法
     * 负责读入Account文件的账号信息
     * 并为每个群文件初始化读写锁
     */
    public static void init(){
        File file = new File("Group/");
        File[] fs = file.listFiles();
        for(File f:fs){
            groupLock.put(f.getName(),new ReentrantReadWriteLock());
        }
        try {
            String line;
            FileInputStream fis=new FileInputStream(new File("Account"));
            InputStreamReader isr=new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            while ((line=br.readLine())!=null) {
                String[] tmp=line.split(" ");
                if(tmp.length==2)
                    account.put(tmp[0],tmp[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
