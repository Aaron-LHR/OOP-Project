package src.Client;

import java.io.DataOutputStream;

public class SendFileThread implements Runnable{
    private DataOutputStream dos = Client.getInstance().getDos();
    private src.UI.chatRoom chatRoom;

    public SendFileThread(src.UI.chatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public void run() {
        chatRoom.refresh();
    }
}
