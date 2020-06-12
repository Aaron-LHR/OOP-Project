package src.Server;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class bla {
    public static void main(String[] args) {
        System.out.println("##FILE##123".substring(8));
        List<String >tmp=new ArrayList<>();
        String username="333";
        tmp.add("111 ddd 333 vvv aaa ddssd333                     ");
        String[] ss=tmp.get(0).split("\\s+");
        String k="";
        for (int i=0;i<ss.length;i++){
            if (!ss[i].equals(username)) k=k+ss[i]+" ";
        }
        System.out.println(k);
    }
}
