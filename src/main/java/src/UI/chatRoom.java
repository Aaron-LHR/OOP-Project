package src.UI;

import src.Client.Client;
import src.Client.Flag;
import src.Client.ReceiveThread;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;


public class chatRoom extends JFrame implements ActionListener {
    Client client = Client.getInstance();
    Flag runFlag = Flag.getInstance();
    String toUsername = "";


    // 聊天界面
    JPanel topBar;
    JLabel lbPort, lbIP, lbName, fname, fsize, fstyle, fcolor, fbackcol;
    JTextField txtPort, txtIP, txtName;
    JButton btnExt, btnSmt, btnRmv, btnRfrsh, btnChat, btnshift, btnImg, btnDel, btnMember;
    JTextArea txtMsg;
    JTextPane txtRcd;
    StyledDocument doc;
    JScrollPane txtScroll, txtScr, listScroll;
    JComboBox fontName, fontSize, fontStyle, fontColor, fontBackColor;
    JList onlineList;
    JFileChooser fileChooser;

    JFrame chatRoomFrame = new JFrame("Java聊天室");

    // 登录界面
    JPanel pnlLgn;
    JLabel lbSrvIP, lbUsr, lbPwd;
    JTextField txtSrvIP, txtUsr, txtShift;
    JPasswordField txtPwd;
    JButton btnLgn, btnRgst, btnExit;

    JDialog diaLgnFrame = new JDialog(this, "登录", true);

    // 搜索群聊窗口
    groupChat diaGrpChat;

    // 群聊成员显示窗口
    groupMember grpMember;

    // 辅助参数
    String strName, strPwd;
    boolean flag = true;
    String[] str_Name = { "宋体", "黑体", "Dialog", "Gulim" };
    String[] str_Size = { "12", "14", "18", "22", "30", "40" };
    String[] str_Style = { "常规", "斜体", "粗体", "粗斜体" };
    String[] str_Color = { "黑色", "红色", "蓝色", "黄色", "绿色" };
    String[] str_BackColor = { "无色", "灰色", "淡红", "淡蓝", "淡黄", "淡绿" };

    public chatRoom() throws IOException {
        new Thread(new ReceiveThread(this)).start();

        // 登录界面
        pnlLgn = new JPanel();
        pnlLgn.setLayout(null);

        lbSrvIP = new JLabel("服务器IP：");
        lbUsr = new JLabel(" 用户名：");
        lbPwd = new JLabel("    密码：");
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

        diaLgnFrame.setSize(250, 170);
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

            // 聊天室界面
            this.setSize(850, 710);
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
            lbPort = new JLabel("端口:");
            lbPort.setFont(new Font("宋体", 0, 14));
            lbPort.setBounds(10, 27, 50, 20);

            txtPort = new JTextField();
            txtPort.setText("1111");
            txtPort.setBorder(BorderFactory.createEtchedBorder());
            txtPort.setBackground(Color.WHITE);
            txtPort.setFont(new Font("宋体", 0, 12));
            txtPort.setEditable(false);
            txtPort.setBounds(70, 25, 80, 30);

            // 服务器IP
            lbIP = new JLabel("服务器IP:");
            lbIP.setFont(new Font("宋体", 0, 14));
            lbIP.setBounds(170, 27, 90, 20);

            txtIP = new JTextField();
            txtIP.setText("127.0.0.1");
            txtIP.setBorder(BorderFactory.createEtchedBorder());
            txtIP.setBackground(Color.WHITE);
            txtIP.setFont(new Font("宋体", 0, 12));
            txtIP.setEditable(false);
            txtIP.setBounds(270, 25, 80, 30);

            // 姓名
            lbName = new JLabel("姓名:");
            lbName.setFont(new Font("宋体", 0, 14));
            lbName.setBounds(370, 27, 50, 20);

            txtName = new JTextField(10);
            txtName.setText(strName);
            txtName.setBorder(BorderFactory.createEtchedBorder());
            txtName.setBackground(Color.WHITE);
            txtName.setFont(new Font("宋体", 0, 12));
            txtName.setEditable(false);
            txtName.setBounds(430, 25, 80, 30);

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
            btnExt.setBounds(720, 25, 100, 30);

            txtShift = new JTextField(3);
            txtShift.setText("1111");
            txtShift.setBorder(BorderFactory.createEtchedBorder());
            txtShift.setBackground(Color.WHITE);
            txtShift.setFont(new Font("宋体", 0, 12));
            txtShift.setEditable(false);
            txtShift.setBounds(70, 20, 100, 35);

            btnshift = new JButton("切换");
            btnshift.setFont(new Font("宋体", 0, 12));
            btnshift.setBounds(720, 15, 100, 35);
            btnshift.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    toUsername = txtShift.getText();
                }
            });

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
                    if (cl.size() == 1 && !toUsername.equals(cl.get(0)) && cl.get(0).charAt(0) != '群') {    //私聊
                        popWindows(cl.get(0) + "私聊", "会话邀请");
                        btnDel.setVisible(false);
                        btnMember.setVisible(false);
                        toUsername = cl.get(0);
                        synchronized (runFlag) {
                            runFlag.setCurToUsername(toUsername);
                        }
                        txtRcd.setText("");
                        try {
                            infoReminder(toUsername, false);
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            for (String loadMessage : Client.readRecord(Client.getUsername(), toUsername)) {
                                String[] MessageSplit = loadMessage.split("@");
                                String[] fontSplit = MessageSplit[2].split("#");
                                infoTransfer(MessageSplit[1], MessageSplit[0], fontSplit[0], Integer.parseInt(fontSplit[1]), Integer.parseInt(fontSplit[2]), fontSplit[3], fontSplit[4]);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else if (cl.size() > 1 && !toUsername.equals(cl.get(0))){   //创建群聊
                        String s = strName + "邀请";
                        String[] names = new String[cl.size()];
                        for (int i = 0; i < cl.size(); i++) {
                            String tmp = cl.get(i);
                            if (tmp.charAt(0) == '群') {
                                popWindows("多选不能选择群聊", "警告");
                                return;
                            }
                            if (i != cl.size() - 1) {
                                s += (tmp + "、");
                            }
                            else {
                                s += tmp;
                            }
                            if (!tmp.equals(Client.getUsername())) {
                                names[i] = tmp;
                            }
                        }
                        try {
                            String groupName = popGrpChat();
                            if (client.createGroup(groupName, Client.getUsername(), names)) {
                                popWindows(s + "参与会话", "会话邀请");
                                btnDel.setVisible(true);
                                btnMember.setVisible(true);
                            }
                            else {
                                popWindows("群聊已存在，进入群聊", "会话邀请");
                                btnDel.setVisible(true);
                                btnMember.setVisible(true);
                                toUsername = "群：" + groupName + "(" + Client.getUsername() + ")";
                                synchronized (runFlag) {
                                    runFlag.setCurToUsername(toUsername);
                                }
                                txtRcd.setText("");
                                try {
                                    infoReminder(toUsername, false);
                                } catch (IOException | InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    client.activateGroup(toUsername);
                                } catch (IOException | InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }

                    }
                    else if (cl.size() == 1 && !toUsername.equals(cl.get(0)) && cl.get(0).charAt(0) == '群') {   //进入群聊
                        toUsername = cl.get(0);
                        btnDel.setVisible(true);
                        btnMember.setVisible(true);
                        synchronized (runFlag) {
                            runFlag.setCurToUsername(toUsername);
                        }
                        txtRcd.setText("");
                        try {
                            infoReminder(toUsername, false);
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            client.activateGroup(toUsername);
                        } catch (IOException | InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                    // 发起群聊或私聊
                }
            });
            btnChat.setFont(new Font("宋体", 0, 12));
            btnChat.setBounds(5, 640, 80, 30);

            btnRfrsh = new JButton("刷新");
            btnRfrsh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    refresh();
                }
            });
            btnRfrsh.setFont(new Font("宋体", 0, 12));
            btnRfrsh.setBounds(90, 640, 80, 30);

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
                        String color = "黑色";
                        String backColor = "无色";
                        if (Color.BLACK.equals(font.color)) {
                            color = "黑色";
                        } else if (Color.red.equals(font.color)) {
                            color = "红色";
                        } else if (Color.BLUE.equals(font.color)) {
                            color = "蓝色";
                        } else if (Color.YELLOW.equals(font.color)) {
                            color = "黄色";
                        } else if (Color.GREEN.equals(font.color)) {
                            color = "绿色";
                        }
                        if (font.backColor != null) {
                            if (font.backColor.equals(new Color(200, 200, 200))) {
                                backColor = "灰色";
                            }
                            else if (font.backColor.equals(new Color(255, 200, 200))) {
                                backColor = "淡红";
                            }
                            else if (font.backColor.equals(new Color(200, 200, 255))) {
                                backColor = "淡蓝";
                            }
                            else if (font.backColor.equals(new Color(255, 255, 200))) {
                                backColor = "淡黄";
                            }
                            else if (font.backColor.equals(new Color(200, 255, 200))) {
                                backColor = "淡绿";
                            }
                        }
                        String Font = font.name + "#" + font.style + "#" + font.size + "#" + color + "#" + backColor;
                        if (toUsername.charAt(0) != '群') {  //给私聊用户发消息
                            if (!client.sendPrivateMessage(toUsername, s, Font)) {
                                popWindows("对方不在线", "提示");
                            }
                            else {
                                submitText(getFontAttrib(), strName);
                                txtMsg.setText("");
                            }
                        }
                        else {  //给群聊发消息
                            if (!client.sendGroupMessage(toUsername, s, Font)) {
                                popWindows("群消息发送失败", "提示");
                            }
                            else {
//                                submitText(getFontAttrib(), strName);
                                txtMsg.setText("");
                            }
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

            // 表情选择
            btnImg = new JButton("表情");
            btnImg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileChooser = new JFileChooser();
                    File f = new File("./src");
                    String s = f.getPath() + "/Icon";

                    fileChooser.setCurrentDirectory(new File(s));
                    fileChooser.showOpenDialog(null);
                    try {
                        insertIcon(fileChooser.getSelectedFile());
                    } catch (BadLocationException badLocationException) {
                        badLocationException.printStackTrace();
                    }
                }
            });
            btnImg.setFont(new Font("宋体", 0, 12));
            btnImg.setBounds(595, 535, 80, 30);

            // 删除
            btnDel = new JButton("退出群聊");
            btnDel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (client.exitGroup(toUsername)) {
                            popWindows("退出群聊成功", "退出群聊");
                            toUsername = "";
                            synchronized (runFlag) {
                                runFlag.setCurToUsername("");
                            }
                            txtRcd.setText("");
                            refresh();
                        }
                        else {
                            popWindows("退出群聊失败，请重试", "退出群聊");
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btnDel.setFont(new Font("宋体", 0, 12));
            btnDel.setBounds(510, 535, 80, 30);

            // 成员列表
            btnMember = new JButton("成员");
            btnMember.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        popGrpMember();
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btnMember.setFont(new Font("宋体", 0, 12));
            btnMember.setBounds(425, 535, 80, 30);

            // 编辑信息区
            txtMsg = new JTextArea();
            txtMsg.setLineWrap(true); // 激活自动换行功能
            txtMsg.setWrapStyleWord(true); // 换行不换字

            txtScroll = new JScrollPane(txtMsg);
            txtScroll.setBorder(BorderFactory.createTitledBorder(null, "发送", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            txtScroll.setBounds(175, 560, 670, 113);
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
            add(txtShift);
            add(btnshift);

            add(txtScroll);

            add(btnRmv);
            add(btnSmt);
            add(btnImg);
            add(btnDel);
            add(btnMember);
            btnDel.setVisible(false);
            btnMember.setVisible(false);

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
            switch (client.Login(username, password)) {
                case 0:
                    popWindows("登录成功", "登录");
                    strName = username;
                    strPwd = password;
                    diaLgnFrame.dispose(); // 登录完成后关闭登录页以启动聊天室界面
                    break;
                case 1:
                    popWindows("用户名或密码错误", "登录");
                    break;
                case 2:
                    popWindows("该用户已登录", "登录");
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

        if (backCol.equals("无色")) c2 = Color.WHITE;
        else if (backCol.equals("灰色")) c2 = new Color(200, 200, 200);
        else if (backCol.equals("淡红")) c2 = new Color(255, 200, 200);
        else if (backCol.equals("淡蓝")) c2 = new Color(200, 200, 255);
        else if (backCol.equals("淡黄")) c2 = new Color(255, 255, 200);
        else if (backCol.equals("淡绿")) c2 = new Color(200, 255, 200);

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

    // 弹出提示信息
    public void popWindows(String strWarning, String strTitle) {
        JOptionPane.showMessageDialog(this, strWarning, strTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    // 弹出成员列表
    public void popGrpMember() throws IOException, InterruptedException {
        grpMember = new groupMember(chatRoomFrame, getMemList());
    }

    public String[] getMemList() throws IOException, InterruptedException {
        return client.getGroupMembers(toUsername);
    }

    public String[] getOnlineList() throws IOException, InterruptedException {
        return client.getOnlineList();
    }

    public void infoReminder(String name, boolean flag) throws IOException, InterruptedException {
//        onlineList.setListData(getOnlineList());
        onlineList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                String s = (String)value;
                if (s.equals(name)) {
                    if (flag) {
                        setBackground(Color.GREEN);
                    }
                    else {
                        setBackground(Color.WHITE);
                    }
                }


                return this;
            }
        });

//        popWindows(name + "向你发送信息", "消息提示");
//
//        onlineList.setCellRenderer(new DefaultListCellRenderer(){
//            @Override
//            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
//                String s = (String)value;
//                if (s.equals(name))  setBackground(Color.WHITE);
//                return this;
//            }
//        });
    }

    public void clearBox() throws BadLocationException {
        remove(txtScr);
        remove(txtRcd);
        txtRcd = new JTextPane();
        txtRcd.setEditable(false);
        doc = txtRcd.getStyledDocument();

        txtScr = new JScrollPane(txtRcd);
        txtScr.setBorder(BorderFactory.createTitledBorder(null, "聊天信息", TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
        txtScr.setBounds(175,80, 670, 415);
        txtScr.setBackground(new Color(255, 255, 255));
        txtScr.setFont(new Font("宋体", 0, 12));

        add(txtScr);
        setVisible(true);
        repaint();
        validate();
    }

    public void refresh() {
        try {
            onlineList.setListData(getOnlineList());
        } catch (InterruptedException | IOException err) {
            err.printStackTrace();
        }
    }

    public String popGrpChat() {
        diaGrpChat = new groupChat(chatRoomFrame);
        return diaGrpChat.GroupName;
    }

    public void insertIcon(File file) throws BadLocationException {
        if (file != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
            String s1 = df.format(new Date()) + "  " + strName + "\n";
            FontAttrib attrib1 = new FontAttrib(s1, "宋体", 0, 12, Color.BLACK, Color.WHITE);
            doc.insertString(doc.getLength(), s1, attrib1.getAttrSet());
        }

        txtRcd.setCaretPosition(doc.getLength());
        txtRcd.insertIcon(new ImageIcon(file.getPath()));
        FontAttrib attrib = new FontAttrib();
        doc.insertString(doc.getLength(), attrib.getText() + "\n\n", attrib.getAttrSet());
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    public static void main(String[] args) throws IOException {
        try {//修改风格
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                System.out.println(info.getName());
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }catch(Exception e) {
            System.out.println(e);
        }
        new chatRoom();
    }

}

