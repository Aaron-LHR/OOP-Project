package Client;

import java.io.*;
import java.net.Socket;

public class Register {
    public static void main(String[] args) throws IOException {
        String username;
        String password;
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (true)
        {
            System.out.println("请输入用户名：");
            username = input.readLine();
            System.out.println("请输入密码：");
            password = input.readLine();
            System.out.println("请确认密码：");
            if (input.readLine().equals(password)) {
                break;
            }
            else {
                System.out.println("两次密码不一致，请重新输入");
            }
        }
        Socket client = new Socket("localhost",1111);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        DataInputStream dis = new DataInputStream(client.getInputStream());
        dos.writeUTF("**" + username + "##" + password + "##");
        dos.flush();
        client.shutdownOutput();
        String ret = dis.readUTF();
        if (ret.equals("1")) {
            System.out.println("注册成功");
        }
        else {
            System.out.println("注册失败");
        }
        dos.close();
        dis.close();
        client.close();
    }
}
