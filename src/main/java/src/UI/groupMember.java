package src.UI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class groupMember {
    JDialog diaGrpMember;
    JList grpMemList;
    JScrollPane grpMemScroll;
    JButton btnBack;

    public groupMember(JFrame frame, String[] memList) {
        diaGrpMember = new JDialog(frame, "群聊成员列表", true);
        
        grpMemList = new JList();
        grpMemList.setListData(memList);
        grpMemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grpMemList.setSelectedIndex(0);
        grpMemList.setBounds(10, 10, 185, 115);

        grpMemScroll = new JScrollPane(grpMemList);
        grpMemScroll.setBorder(BorderFactory.createTitledBorder(null, "群聊用户", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
        grpMemScroll.setFont(new Font("宋体", 0, 12));
        grpMemScroll.setBackground(new Color(255, 255, 255)); // white
        grpMemScroll.setBounds(5, 5, 190, 125);

        btnBack = new JButton("返回");
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diaGrpMember.dispose();
            }
        });
        btnBack.setFont(new Font("宋体", 0, 12));
        btnBack.setBounds(115, 135, 80, 30);

        diaGrpMember.setResizable(false);
        diaGrpMember.getContentPane().setLayout(null);
        diaGrpMember.getContentPane().add(grpMemScroll);
        diaGrpMember.getContentPane().add(btnBack);

        diaGrpMember.setSize(200, 200);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension fra = diaGrpMember.getSize();
        if (fra.width > screen.width) {
            fra.width = screen.width;
        }
        if (fra.height > screen.height) {
            fra.height = screen.height;
        }

        diaGrpMember.setLocation((screen.width - fra.width) / 2, (screen.height - fra.height) / 2);
        diaGrpMember.getContentPane().setBackground(Color.gray);
        diaGrpMember.setVisible(true);
        diaGrpMember.getRootPane().setDefaultButton(btnBack); // 默认回车按钮
    }
}
