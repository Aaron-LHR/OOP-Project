package src.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 负责文件传输的类
 */
public class SendFile {
    Socket socket;
    String fromUser,toUser,filename;
    Long len;
    byte[] bytes=new byte[1024];
    List<byte[]> list=new ArrayList<>();

    /**
     * 构造方法
     * @param fromUser 发送方用户名
     * @param toUser 接收方用户名
     * @param filename 发送文件的内容
     * @param len 发送文件的长度(以byte[1024]为单位)
     * @param socket 对应套接字
     */
    public SendFile(String fromUser, String toUser,String filename,Long len, Socket socket){
        this.filename=filename;
        this.len=len;
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.socket=socket;
    }

    /**
     * 启动方法
     */
    public void act(){
        try {
            DataOutputStream out=new DataOutputStream(Server.online.get(toUser).getOutputStream());
            DataInputStream in=new DataInputStream(socket.getInputStream());
            List<byte[]> list=new ArrayList<>();
            synchronized (out){
                File file = new File(filename);
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fos = new FileOutputStream(filename, true);
                byte[] bytes = new byte[1024];
                int length = 0;
                long n =len;
                while (n > 0) {
                    length = in.read(bytes, 0, bytes.length);
                    fos.write(bytes, 0, length);
                    fos.flush();
                    n--;
                }
                new DataOutputStream(socket.getOutputStream()).writeUTF("@"+filename+"@108@0");
                FileInputStream fis = new FileInputStream(file);
                out.writeUTF("@"+fromUser+"@202@"+filename+"@"+len);
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    out.write(bytes, 0, length);
                    System.out.println(Arrays.toString(bytes));
                    out.flush();
//                    Thread.sleep(200);
                }
                fis.close();
                fos.close();
//                for (int i=0;i<len;i++){
//                    in.read(bytes,0,bytes.length);
//                    list.add(bytes);
//                }
//                for (byte[] i:list){
//                    out.write(i);
//                }
            }

        }catch (Exception e){
            e.printStackTrace();
            try {
                new DataOutputStream(socket.getOutputStream()).writeUTF("@"+filename+"@108@1");
            }catch (Exception e1){}

        }

    }
}
