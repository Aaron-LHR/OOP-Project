package Server;

import java.io.*;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class bla {
    public static void main(String[] args) {
        try {
            String[] member={"123","abc","dede","abac"};
            FileOutputStream fos=new FileOutputStream(new File("Group/group1"));
            OutputStreamWriter osw= new OutputStreamWriter(fos, "UTF-8");

            for(int i=1;i<member.length;i++){
                osw.write(member[i]+" ");
            }
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }














//    public static void main(String[] args) {
//
//        InetAddress inetAddress;//声明InetAddress对象
//        try {
//            inetAddress= InetAddress.getLocalHost();//实例化InetAddress对象，返回本地主机
//            String hostName=inetAddress.getHostName();//获取本地主机名
//            String canonicalHostName=inetAddress.getCanonicalHostName();//获取此 IP地址的完全限定域名
//            byte[] address=inetAddress.getAddress();//获取原始IP地址
//            int a=0;
//            if(address[3]<0){
//                a=address[3]+256;
//            }
//            String hostAddress=inetAddress.getHostAddress();//获取本地主机的IP地址
//            boolean reachable=inetAddress.isReachable(2000);//获取布尔类型，看是否能到达此IP地址
//            System.out.println(inetAddress.toString());
//            System.out.println("主机名为："+hostName);//输出本地主机名
//            System.out.println("此IP地址的完全限定域名："+canonicalHostName);//输出此IP地址的完全限定域名
//            System.out.println("原始IP地址为："+address[0]+"."+address[1]+"."+address[2]+"."+a);//输出本地主机的原始IP地址
//            System.out.println("IP地址为："+hostAddress);//输出本地主机的IP地址
//            System.out.println("是否能到达此IP地址："+reachable);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }

}
