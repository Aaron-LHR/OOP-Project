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
        String[] s= {"111","222"};//群成员列表
        String tmp1="@"+s.length+"@107@0@"+s[0];
        int cnt=s.length;
        for (int i=1;i<cnt;i++){
            tmp1=tmp1+"#"+s[i];
        }
        System.out.println(tmp1);
    }
}
