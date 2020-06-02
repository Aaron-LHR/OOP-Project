package Server;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class Login extends Thread {
    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(Server.port);


            while (true){
                Socket socket=serverSocket.accept();
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream=socket.getInputStream();

                byte[] bytes = new byte[1024];
                int len;
                StringBuffer buff = new StringBuffer();
                while ((len = inputStream.read(bytes)) != -1) {
                    buff.append(new String(bytes, 0, len, "UTF-8"));
                }
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

                inputStream.close();
                outputStream.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
