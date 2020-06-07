package UI;

import Client.Client;
import Client.Flag;
import Client.ReceiveThread;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;


public class chatRoom extends JFrame implements ActionListener {
    Client client = Client.getInstance();
    Flag runFlag = Flag.getInstance();
    String toUsername = "cdf";

    // 聊天界面
    JPanel topBar;
    JLabel lbPort, lbIP, lbName, fname, fsize, fstyle, fcolor, fbackcol;
    JTextField txtPort, txtIP, txtName;
    JButton btnExt, btnSmt, btnRmv, btnRfrsh, btnChat;
    JTextArea txtMsg;
    JTextPane txtRcd;
    StyledDocument doc;
    JScrollPane txtScroll, txtScr, listScroll;
    JComboBox fontName, fontSize, fontStyle, fontColor, fontBackColor;
    JList onlineList;

    JFrame chatRoomFrame = new JFrame("Java聊天室");

    // 登录界面
    JPanel pnlLgn;
    JLabel lbSrvIP, lbUsr, lbPwd;
    JTextField txtSrvIP, txtUsr;
    JPasswordField txtPwd;
    JButton btnLgn, btnRgst, btnExit;

    JDialog diaLgnFrame = new JDialog(this, "登录", true);

    // 辅助参数
    String strName, strPwd;
    boolean flag = true;
    String[] str_Name = { "宋体", "黑体", "Dialog", "Gulim" };
    String[] str_Size = { "12", "14", "18", "22", "30", "40" };
    String[] str_Style = { "常规", "斜体", "粗体", "粗斜体" };
    String[] str_Color = { "黑色", "红色", "蓝色", "黄色", "绿色" };
    String[] str_BackColor = { "无色", "灰色", "淡红", "淡蓝", "淡黄", "淡绿" };

    public chatRoom() throws IOException {
        new Thread(new ReceiveThread(client.getDis(), this)).start();

        // 登录界面
        pnlLgn = new JPanel();
        pnlLgn.setLayout(null);

        lbSrvIP = new JLabel("服务器IP：");
        lbUsr = new JLabel("用户名：");
        lbPwd = new JLabel("密码：");
        txtSrvIP = new JTextField(12);
        txtUsr = new JTextField(12);
        txtPwd = new JPasswordField(12);

        txtSrvIP.setText("127.0.0.1");

        btnLgn = new JButton("登陆");
        btnRgst = new JButton("注册");
        btnExit = new JButton("退出");

        btnLgn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsrLogin();
            }
        });
        btnRgst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsrRgst();
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    closeDiaLgnFrame();
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });

        diaLgnFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    closeDiaLgnFrame();
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });

        diaLgnFrame.setResizable(false);

        diaLgnFrame.getContentPane().setLayout(new FlowLayout());
        diaLgnFrame.getContentPane().add(lbSrvIP);
        diaLgnFrame.getContentPane().add(txtSrvIP);
        diaLgnFrame.getContentPane().add(lbUsr);
        diaLgnFrame.getContentPane().add(txtUsr);
        diaLgnFrame.getContentPane().add(lbPwd);
        diaLgnFrame.getContentPane().add(txtPwd);
        diaLgnFrame.getContentPane().add(btnLgn);
        diaLgnFrame.getContentPane().add(btnRgst);
        diaLgnFrame.getContentPane().add(btnExit);

        diaLgnFrame.setSize(250, 150);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frame = diaLgnFrame.getSize();
        if (frame.width > screen.width) {
            frame.width = screen.width;
        }
        if (frame.height > screen.height) {
            frame.height = screen.height;
        }

        diaLgnFrame.setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);
        diaLgnFrame.getContentPane().setBackground(Color.gray);
        diaLgnFrame.setVisible(true);
        diaLgnFrame.getRootPane().setDefaultButton(btnLgn); // 默认回车按钮

        if (flag) {

            /* 使用Windows的界面风格
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception e) {
                e.printStackTrace();
            }

             */

            // 聊天室界面
            this.setSize(850, 700);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setResizable(false);

            // 在屏幕居中显示
            Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension fra = this.getSize();
            if (fra.width > scr.width) {
                fra.width = scr.width;
            }
            if (fra.height > scr.height) {
                fra.height = scr.height;
            }
            this.setLocation((scr.width - fra.width) / 2,(scr.height - fra.height) / 2);

            // 上边栏
            topBar = new JPanel();
            topBar.setBorder(BorderFactory.createTitledBorder(null, "连接信息", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            topBar.setLayout(null);
            topBar.setFont(new Font("宋体", 0, 12));
            topBar.setBackground(new Color(255, 255, 255)); // white
            topBar.setBounds(5,5, 840, 70);

            // 上边栏内信息:
            // 端口
            lbPort = new JLabel("端口");
            lbPort.setFont(new Font("宋体", 0, 12));
            lbPort.setBounds(10, 30, 50, 12);

            txtPort = new JTextField();
            txtPort.setText("1111");
            txtPort.setBorder(BorderFactory.createEtchedBorder());
            txtPort.setBackground(Color.WHITE);
            txtPort.setFont(new Font("宋体", 0, 12));
            txtPort.setEditable(false);
            txtPort.setBounds(70, 20, 100, 35);

            // 服务器IP
            lbIP = new JLabel("服务器IP");
            lbIP.setFont(new Font("宋体", 0, 12));
            lbIP.setBounds(210, 30, 50, 12);

            txtIP = new JTextField();
            txtIP.setText("127.0.0.1");
            txtIP.setBorder(BorderFactory.createEtchedBorder());
            txtIP.setBackground(Color.WHITE);
            txtIP.setFont(new Font("宋体", 0, 12));
            txtIP.setEditable(false);
            txtIP.setBounds(270, 20, 100, 35);

            // 姓名
            lbName = new JLabel("姓名");
            lbName.setFont(new Font("宋体", 0, 12));
            lbName.setBounds(420, 30, 50, 12);

            txtName = new JTextField(10);
            txtName.setText(strName);
            txtName.setBorder(BorderFactory.createEtchedBorder());
            txtName.setBackground(Color.WHITE);
            txtName.setFont(new Font("宋体", 0, 12));
            txtName.setEditable(false);
            txtName.setBounds(480, 20, 100, 35);

            // 退出按钮
            btnExt = new JButton("退出");
            btnExt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    btnExt.setBackground(new Color(30, 144, 255));
                    try {
                        closeChatFrame();
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btnExt.setFont(new Font("宋体", 0, 12));
            btnExt.setBounds(720, 15, 100, 35);

            // 添加至 topBar
            topBar.add(lbPort);
            topBar.add(txtPort);
            topBar.add(lbIP);
            topBar.add(txtIP);
            topBar.add(lbName);
            topBar.add(txtName);
            topBar.add(btnExt);

            // 在线用户列表
            onlineList = new JList();

            try {
                onlineList.setListData(getOnlineList());
            } catch (InterruptedException | IOException err) {
                err.printStackTrace();
            }

            onlineList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            onlineList.setSelectedIndex(0);

            /*
            onlineList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    popWindows("该用户在线: " + onlineList.getSelectedValue(), "在线提示");
                }
            });

             */

            listScroll = new JScrollPane(onlineList);
            listScroll.setBorder(BorderFactory.createTitledBorder(null, "在线用户", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            listScroll.setFont(new Font("宋体", 0, 12));
            listScroll.setBackground(new Color(255, 255, 255)); // white
            listScroll.setBounds(5,80, 165, 560);

            btnChat = new JButton("发起会话");
            btnChat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List<String> cl = onlineList.getSelectedValuesList();
                    String s = strName + "邀请";
                    for (int i = 0; i < cl.size(); i++) {
                        if (i != cl.size() - 1) s += (cl.get(i) + "、");
                        else s += cl.get(i);
                    }
                    popWindows(s + "参与会话", "会话邀请");
                    toUsername = cl.get(1);
                    // 发起群聊或私聊
                }
            });
            btnChat.setFont(new Font("宋体", 0, 12));
            btnChat.setBounds(5, 645, 80, 30);

            btnRfrsh = new JButton("刷新");
            btnRfrsh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        onlineList.setListData(getOnlineList());
                    } catch (InterruptedException | IOException err) {
                        err.printStackTrace();
                    }
                }
            });
            btnRfrsh.setFont(new Font("宋体", 0, 12));
            btnRfrsh.setBounds(90, 645, 80, 30);

            // 对话框
            txtRcd = new JTextPane();
            txtRcd.setEditable(false);
            doc = txtRcd.getStyledDocument();

            txtScr = new JScrollPane(txtRcd);
            txtScr.setBorder(BorderFactory.createTitledBorder(null, "聊天信息", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            txtScr.setBounds(175,80, 670, 415);
            txtScr.setBackground(new Color(255, 255, 255));
            txtScr.setFont(new Font("宋体", 0, 12));

            // 字体设置
            fname = new JLabel("字体");
            fname.setFont(new Font("宋体", 0, 14));
            fname.setBounds(175, 500, 35, 30);

            fontName = new JComboBox(str_Name); // 字体名称
            fontName.setFont(new Font("宋体", 0, 12));
            fontName.setBounds(215, 500, 90, 30);

            fsize = new JLabel("大小");
            fsize.setFont(new Font("宋体", 0, 14));
            fsize.setBounds(310, 500, 35, 30);

            fontSize = new JComboBox(str_Size); // 字号
            fontSize.setFont(new Font("宋体", 0, 12));
            fontSize.setBounds(350, 500, 90, 30);

            fstyle = new JLabel("样式");
            fstyle.setFont(new Font("宋体", 0, 14));
            fstyle.setBounds(445, 500, 35, 30);

            fontStyle = new JComboBox(str_Style); // 样式
            fontStyle.setFont(new Font("宋体", 0, 12));
            fontStyle.setBounds(485, 500, 90, 30);

            fcolor = new JLabel("颜色");
            fcolor.setFont(new Font("宋体", 0, 14));
            fcolor.setBounds(580, 500, 35, 30);

            fontColor = new JComboBox(str_Color); // 颜色
            fontColor.setFont(new Font("宋体", 0, 12));
            fontColor.setBounds(620, 500, 90, 30);

            fbackcol = new JLabel("背景色");
            fbackcol.setFont(new Font("宋体", 0, 14));
            fbackcol.setBounds(175, 535, 50, 30);

            fontBackColor = new JComboBox(str_BackColor); // 背景颜色
            fontBackColor.setFont(new Font("宋体", 0, 12));
            fontBackColor.setBounds(230, 535, 75, 30);

            // 发送按钮
            btnSmt = new JButton("发送");
            btnSmt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String s = txtMsg.getText();
                        FontAttrib font = getFontAttrib();
                        String Font = font.name + "#" + font.style + "#" + font.style + "#" + font.size + "#" + font.color + "#" + font.backColor;
                        if (!client.send(toUsername, s, Font)) {
                            popWindows("对方不在线", "提示");
                        }
                        else {
                            submitText(getFontAttrib(), strName);
                            txtMsg.setText("");
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btnSmt.setFont(new Font("宋体", 0, 12));
            btnSmt.setBounds(765, 535, 80, 30);

            // 清除已编辑信息
            btnRmv = new JButton("清空");
            btnRmv.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    txtMsg.setText("");
                }
            });
            btnRmv.setFont(new Font("宋体", 0, 12));
            btnRmv.setBounds(680, 535, 80, 30);

            // 编辑信息区
            txtMsg = new JTextArea();
            txtMsg.setLineWrap(true); // 激活自动换行功能
            txtMsg.setWrapStyleWord(true); // 换行不换字

            txtScroll = new JScrollPane(txtMsg);
            txtScroll.setBorder(BorderFactory.createTitledBorder(null, "发送", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            txtScroll.setBounds(175, 570, 670,100);
            txtScroll.setBackground(new Color(250, 250, 250));
            txtScroll.setFont(new Font("宋体", 0, 12));

            // 最终添加
            setLayout(null);

            add(topBar);

            add(listScroll);
            add(btnChat);
            add(btnRfrsh);

            add(txtScr);

            add(fname);
            add(fontName);
            add(fsize);
            add(fontSize);
            add(fstyle);
            add(fontStyle);
            add(fcolor);
            add(fontColor);
            add(fbackcol);
            add(fontBackColor);

            add(txtScroll);

            add(btnRmv);
            add(btnSmt);

            // 设置界面可见
            setVisible(true);
            // receive();

        }

    }

    public void closeChatFrame() throws IOException, InterruptedException {
        client.exit();
        this.dispose();
    }

    public void closeDiaLgnFrame() throws IOException, InterruptedException {
        diaLgnFrame.dispose();
        flag = false;
        closeChatFrame();
    }

    public void UsrRgst() {
        try {
            String username = txtUsr.getText().trim();
            String password = new String(txtPwd.getPassword()).trim();
            if (client.register(username, password)) {
                popWindows("注册成功", "注册");
                diaLgnFrame.dispose(); // 登录完成后关闭登录页以启动聊天室界面
            }
            else {
                popWindows("用户名已被占用", "注册");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void UsrLogin() {
        try {
            String username = txtUsr.getText().trim();
            String password = new String(txtPwd.getPassword()).trim();
            if (client.Login(username, password)) {
                popWindows("登录成功", "登录");
                strName = username;
                strPwd = password;
                diaLgnFrame.dispose(); // 登录完成后关闭登录页以启动聊天室界面
            }
            else {
                popWindows("用户名或密码错误", "登录");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void submitText(FontAttrib attrib, String name) {
        synchronized (txtRcd) {
            try { // 插入文本
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式

                String s1 = df.format(new Date()) + "  " + name + "\n";
                String s2 = attrib.getText() + "\n\n";

                FontAttrib attrib1 = new FontAttrib(s1, "宋体", 0, 12, Color.BLACK, Color.WHITE);

                doc.insertString(doc.getLength(), s1, attrib1.getAttrSet());
                doc.insertString(doc.getLength(), s2, attrib.getAttrSet());

            } catch (BadLocationException e) {
                e.printStackTrace();
            }

        }
    }

    public void infoTransfer(String msg, String name, String font_Name, int style, int size, String color, String backCol) {
        Color c1 = null, c2 = null;

        if (color.equals("黑色")) c1 = Color.BLACK;
        else if (color.equals("红色")) c1 = Color.RED;
        else if (color.equals("蓝色")) c1 = Color.BLUE;
        else if (color.equals("黄色")) c1 = Color.YELLOW;
        else if (color.equals("绿色")) c1 = Color.GREEN;

        if (backCol.equals("黑色")) c2 = Color.BLACK;
        else if (backCol.equals("红色")) c2 = Color.RED;
        else if (backCol.equals("蓝色")) c2 = Color.BLUE;
        else if (backCol.equals("黄色")) c2 = Color.YELLOW;
        else if (backCol.equals("绿色")) c2 = Color.GREEN;

        FontAttrib att = new FontAttrib(msg, font_Name, style, size, c1, c2);

        submitText(att, name);
    }

    public FontAttrib getFontAttrib() {
        FontAttrib att = new FontAttrib();

        att.setText(txtMsg.getText());
        att.setName((String) fontName.getSelectedItem());
        att.setSize(Integer.parseInt((String) fontSize.getSelectedItem()));

        String temp_style = (String) fontStyle.getSelectedItem();
        if (temp_style.equals("常规")) {
            att.setStyle(FontAttrib.GENERAL);
        }
        else if (temp_style.equals("粗体")) {
            att.setStyle(FontAttrib.BOLD);
        }
        else if (temp_style.equals("斜体")) {
            att.setStyle(FontAttrib.ITALIC);
        }
        else if (temp_style.equals("粗斜体")) {
            att.setStyle(FontAttrib.BOLD_ITALIC);
        }

        String temp_color = (String) fontColor.getSelectedItem();
        if (temp_color.equals("黑色")) {
            att.setColor(new Color(0, 0, 0));
        }
        else if (temp_color.equals("红色")) {
            att.setColor(new Color(255, 0, 0));
        }
        else if (temp_color.equals("蓝色")) {
            att.setColor(new Color(0, 0, 255));
        }
        else if (temp_color.equals("黄色")) {
            att.setColor(new Color(255, 255, 0));
        }
        else if (temp_color.equals("绿色")) {
            att.setColor(new Color(0, 255, 0));
        }

        String temp_backColor = (String) fontBackColor.getSelectedItem();
        if (!temp_backColor.equals("无色")) {
            if (temp_backColor.equals("灰色")) {
                att.setBackColor(new Color(200, 200, 200));
            }
            else if (temp_backColor.equals("淡红")) {
                att.setBackColor(new Color(255, 200, 200));
            }
            else if (temp_backColor.equals("淡蓝")) {
                att.setBackColor(new Color(200, 200, 255));
            }
            else if (temp_backColor.equals("淡黄")) {
                att.setBackColor(new Color(255, 255, 200));
            }
            else if (temp_backColor.equals("淡绿")) {
                att.setBackColor(new Color(200, 255, 200));
            }
        }

        return att;
    }

    /*
    private void receive() throws IOException {
        while (true) {
            String string = client.receive();
            String[] output = string.split("@");
            switch (output[2]) {
                case
            }
            if (output[0].equals("0")) {
                synchronized (txtRcd) {
                    txtRcd.setEditable(true);
                    txtRcd.append(output[1] + ":\n    " + output[2] + "\n\n");
                    txtRcd.setEditable(false);
                }
            }
            System.out.println(string);

        }
    }

     */

    // 弹出提示信息
    public void popWindows(String strWarning, String strTitle) {
        JOptionPane.showMessageDialog(this, strWarning, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    public String[] getOnlineList() throws IOException, InterruptedException {
        return client.getOnlineList();
    }

    /*
    // 建立与服务端通信的套接字
    public void connectServer() {
        try {
            skt = new Socket(txtSrvIP.getText(), 8888);
            in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(skt.getOutputStream())), true);
        } catch (ConnectException e) {
            popWindows("连接服务器失败!", "ERROR CONNECTION");
            txtSrvIP.setText("");
            System.out.println(e);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

     */

    @Override
    public void actionPerformed(ActionEvent e) {}

    public static void main(String[] args) throws IOException {
        new chatRoom();
    }

}

