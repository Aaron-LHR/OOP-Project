package Server;

import java.io.*;
import java.net.Socket;

public class AddGroup extends Thread{
    public String[] member;
    public Socket socket;
    public AddGroup(String[] member,Socket socket){
        this.member=member;
        this.socket=socket;
    }

    @Override
    public void run() {
        super.run();
        DataOutputStream out= null;
        try {
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sum=new StringBuffer();

        for (int i=1;i<member.length;i++){
            sum.append(member[i]);
        }
        File groupfile=new File("Group/"+member[0]+member[1]);
        if (!groupfile.exists()){
            try {
                groupfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                out.writeUTF("1");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            FileOutputStream fos=new FileOutputStream(groupfile);
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            for(int i=1;i<member.length;i++){
                osw.write(member[i]+" ");
            }
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
