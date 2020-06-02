package Client;

import java.io.*;
import java.net.Socket;

public class Login {
    public static void main(String[] args) throws IOException {
        String username;
        String password;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入用户名：");
        username = input.readLine();
        System.out.println("请输入密码：");
        password = input.readLine();
        Socket client = new Socket("localhost",1111);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream dis = new DataInputStream(client.getInputStream());
        dos.writeUTF("!!" + username + "##" + password + "##");
        dos.flush();
        dos.close();
        String ret = dis.readUTF();
        if (ret.equals("1")) {
            System.out.println("登录成功");
        }
        else {
            System.out.println("登录失败");
        }
        client.close();
    }
}
