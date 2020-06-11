package src.UI;

import com.vdurmont.emoji.Emoji;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Arrays;
import java.util.List;

public class emojiImgBox {
    JDialog diaImgMember;
    JList imgList;
    JScrollPane imgScroll;
    JButton btnConfirm, btnCancel;

    public emojiImgBox(JFrame frame,JTextArea txtArea, String[] imgUnicodeList) {
        diaImgMember = new JDialog(frame, "Emoji表情包", true);

        imgList = new JList();

        imgList.setListData(imgUnicodeList);
        imgList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        imgList.setSelectedIndex(0);
        imgList.setBounds(10, 10, 170, 335);

        imgScroll = new JScrollPane(imgList);
        imgScroll.setBorder(BorderFactory.createTitledBorder(null, "emoji表情", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
        imgScroll.setFont(new Font("宋体", 0, 12));
        imgScroll.setBackground(new Color(255, 255, 255)); // white
        imgScroll.setBounds(5, 5, 180, 345);

        btnConfirm = new JButton("确定");
        btnConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] imgCodeList = imgList.getSelectedIndices();
                List<String> list = Arrays.asList(imgUnicodeList);
                for (int i : imgCodeList) {
                    txtArea.append(list.get(i));
                }
                diaImgMember.dispose();
            }
        });
        btnConfirm.setFont(new Font("宋体", 0, 12));
        btnConfirm.setBounds(20, 355, 80, 30);

        btnCancel = new JButton("取消");
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diaImgMember.dispose();
            }
        });
        btnCancel.setFont(new Font("宋体", 0, 12));
        btnCancel.setBounds(105, 355, 80, 30);

        diaImgMember.setResizable(false);
        diaImgMember.getContentPane().setLayout(null);
        diaImgMember.getContentPane().add(imgScroll);
        diaImgMember.getContentPane().add(btnConfirm);
        diaImgMember.getContentPane().add(btnCancel);

        diaImgMember.setSize(200, 400);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension fra = diaImgMember.getSize();
        if (fra.width > screen.width) {
            fra.width = screen.width;
        }
        if (fra.height > screen.height) {
            fra.height = screen.height;
        }

        diaImgMember.setLocation((screen.width - fra.width) / 2, (screen.height - fra.height) / 2);
        diaImgMember.getContentPane().setBackground(Color.gray);
        diaImgMember.setVisible(true);
        diaImgMember.getRootPane().setDefaultButton(btnConfirm); // 默认回车按钮
    }
}
