package Server;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    public static int port=1111;
    public static Map<String,String> account=new HashMap<>(){};//name,password
    public static Map<String,String> online=new HashMap<>();//online user
    public static void init(){
        try {
            String line;
            FileInputStream fis=new FileInputStream(new File("Account"));
            InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            while ((line=br.readLine())!=null) {
                String[] tmp=line.split(" ");
                account.put(tmp[0],tmp[1]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        init();
        try {
            ServerSocket serverSocket=new ServerSocket(Server.port);


            while (true){
                System.out.println("等待中。。。");
                Socket socket=serverSocket.accept();
                System.out.println("成功！");
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream=socket.getInputStream();
                System.out.println(1);
                byte[] bytes = new byte[1024];
                int len;
                StringBuffer buff = new StringBuffer();
                while ((len = inputStream.read(bytes)) != -1) {
                    buff.append(new String(bytes, 0, len, "UTF-8"));
                }
                System.out.println(buff);
                if (buff.charAt(0)=='!'&&buff.charAt(1)=='!'){
                    String[] tmp=buff.toString().split("##");
                    if(Server.account.get(tmp[0]).equals(tmp[1])){
                        outputStream.write("1".getBytes("UTF-8"));
                        InetAddress inetAddress=socket.getInetAddress();
                        System.out.println(tmp[0]+":"+inetAddress.getHostAddress()+"online!");
                        Server.online.put(tmp[0],inetAddress.getHostAddress());
                    }
                    else outputStream.write("0".getBytes("UTF-8"));
                }
                System.out.println();
                inputStream.close();
                outputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
