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
    public static Map<String,Socket> online=new HashMap<>();//online user
    private Server(){}
    public static void main(String[] args) {
        init();
        try {
            ServerSocket serverSocket=new ServerSocket(Server.port);
            while (true){
                System.out.println("等待中。。。");
                Socket socket=serverSocket.accept();
                System.out.println("成功！");
                InputStream inputStream=socket.getInputStream();
                OutputStream outputStream=socket.getOutputStream();
                DataOutputStream out = new DataOutputStream(outputStream);
                DataInputStream in = new DataInputStream(inputStream);

                String buff = in.readUTF();
                System.out.println(buff);

                if (buff.charAt(0)=='*'&&buff.charAt(1)=='*'){//注册:**name##passwd##
                    String[] tmp=buff.split("##");
                    tmp[0]=tmp[0].substring(2);
                    new Register(tmp[0],tmp[1],socket).start();
                }

                if (buff.charAt(0)=='!'&&buff.charAt(1)=='!'){//登录:!!name##passwd##
                    String[] tmp=buff.split("##");
                    tmp[0]=tmp[0].substring(2);
                    new Login(tmp[0],tmp[1],socket).start();
                }

                if (buff.charAt(0)=='?'&&buff.charAt(1)=='?'){//检查对方登录状态:??name
                    String tmp=buff;
                    tmp=tmp.substring(2);
                    if (online.get(tmp)!=null){
                        //用户在线，返回0
                        out.writeUTF("0");
                        out.flush();
                    }
                    else {
                        //不在线，返回1
                        out.writeUTF("1");
                        out.flush();
                    }
                }

                if (buff.charAt(0)=='@'){//发送信息：@fromUser@ToUser@content
                    String[] tmp=buff.split("@");
                    int t=tmp[1].length()+tmp[2].length()+3;
                    String content= buff.substring(t);
                    new Send(tmp[1],tmp[2],content,socket).start();
                }

                if (buff.charAt(0)=='-'&&buff.charAt(1)=='-'){//注销：--username
                    String tmp=buff;
                    tmp=tmp.substring(2);
                    if (online.get(tmp)==null){
                        //用户已下线，注销错误
                        out.writeUTF("1");
                        out.flush();
                    }
                    else {
                        //完成注销
                        out.writeUTF("0");
                        out.flush();
                    }
                }

                if (buff.indexOf("##ADDGROUP##")==0){//创建群聊：##ADDGROUP##groupname##name1##name2##name3
                    buff=buff.substring(12);
                    String[] tmp=buff.split("##");
                    new AddGroup(tmp,socket).start();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
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
    public static String getUsername(Socket socket){
        for (Map.Entry<String,Socket> entry : online.entrySet()) {
            if (entry.getValue().equals(socket.getInetAddress().getHostAddress())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
