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

public class Server {
    public static int port=1111;
    public static char chgl='\n';//部署至服务器时更改
    public static Map<String,String> account=new HashMap<>(){};//name,password
    public static Map<String,Socket> online=new HashMap<>();//online user
    public static Map<String, ReentrantReadWriteLock> groupLock=new HashMap<>();//每个群聊有一个锁
    private final ReentrantReadWriteLock readWriteLock =new ReentrantReadWriteLock();
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
                account.put(tmp[0],tmp[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getUsername(Socket socket){
        for (Map.Entry<String,Socket> entry : online.entrySet()) {
            if (entry.getValue().equals(socket.getInetAddress().getHostAddress())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
