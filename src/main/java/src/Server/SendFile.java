package src.Server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendFile {
    Socket socket;
    String fromUser,toUser,filename;
    Long len;
    byte[] bytes=new byte[1024];
    List<byte[]> list=new ArrayList<>();
    public SendFile(String fromUser, String toUser,String filename,Long len, Socket socket){
        this.filename=filename;
        this.len=len;
        this.fromUser=fromUser;
        this.toUser=toUser;
        this.socket=socket;
    }
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
