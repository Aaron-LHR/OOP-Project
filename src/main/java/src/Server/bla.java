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
        List<String>list=new ArrayList<>();
        list.add("131");
        list.add("222");
        list.add("333");
        list.add(0,list.get(0).replace("1",""));
        list.remove(1);
        for (String s:list){
            System.out.println(s);
        }
    }
}
