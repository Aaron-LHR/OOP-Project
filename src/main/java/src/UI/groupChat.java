package src.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class groupChat {

    JDialog diaGrpChatFrame;
    JLabel lbGrpChat;
    JTextField txtGrpChat;
    JButton btnConfirm, btnCancel;
    String GroupName;

    public groupChat(JFrame room) {
        diaGrpChatFrame = new JDialog(room, "加入群聊", true);
        lbGrpChat = new JLabel("请输入群聊名称：");
        txtGrpChat = new JTextField("群聊名称",12);
        btnConfirm = new JButton("确定");
        btnCancel = new JButton("取消");

        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupName = txtGrpChat.getText();
                diaGrpChatFrame.dispose();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diaGrpChatFrame.dispose();
            }
        });

        diaGrpChatFrame.setResizable(false);
        diaGrpChatFrame.getContentPane().setLayout(new FlowLayout());
        diaGrpChatFrame.getContentPane().add(lbGrpChat);
        diaGrpChatFrame.getContentPane().add(txtGrpChat);
        diaGrpChatFrame.getContentPane().add(btnConfirm);
        diaGrpChatFrame.getContentPane().add(btnCancel);

        diaGrpChatFrame.setSize(200, 120);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frame = diaGrpChatFrame.getSize();
        if (frame.width > screen.width) {
            frame.width = screen.width;
        }
        if (frame.height > screen.height) {
            frame.height = screen.height;
        }

        diaGrpChatFrame.setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
        diaGrpChatFrame.getContentPane().setBackground(Color.gray);
        diaGrpChatFrame.setVisible(true);
        diaGrpChatFrame.getRootPane().setDefaultButton(btnConfirm); // 默认回车按钮

    }
}
