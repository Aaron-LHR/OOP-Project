package UI;

import Client.Client;
import Client.Flag;
import Client.ReceiveThread;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
目前还有一个小 bug 搞不定：我在 JPanel bottomBar 中添加的控件无法显示
，但在差不多的 JPanel topBar 中添加的控件都正常显示
 */

public class test1 extends JFrame implements ActionListener {
    Client client = new Client("localhost",1111);
    Flag runFlag = client.getRunFlag();
    String toUsername = "cdf";

    // 聊天界面
    JPanel pnlChat, topBar, leftBar, middleBar, bottomBar;
    JLabel lbPort, lbIP, lbName;
    JTextField txtPort, txtIP, txtName;
    JButton btnExt, btnSmt;
    JTextArea txtMsg, txtRcd;
    JScrollPane txtScroll, txtScr;

    JFrame test1Frame = new JFrame("Java聊天室");

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

    public test1() throws IOException {
        new Thread(new ReceiveThread(client.getDis(), this.runFlag)).start();

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
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        diaLgnFrame.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    closeDiaLgnFrame();
                } catch (IOException ex) {
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


        if (flag) {

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
                    } catch (IOException ex) {
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

            // 侧边栏
            leftBar = new JPanel();
            leftBar.setBorder(BorderFactory.createTitledBorder(null, "在线用户", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            leftBar.setLayout(null);
            leftBar.setFont(new Font("宋体", 0, 12));
            leftBar.setBackground(new Color(255, 255, 255)); // white
            leftBar.setBounds(5,80, 225, 590);

            // 对话框
            txtRcd = new JTextArea(20, 50);
            txtRcd.setLineWrap(true); // 激活自动换行功能
            txtRcd.setWrapStyleWord(true); // 换行不换字
            txtRcd.setEditable(false);


            txtScr = new JScrollPane(txtRcd);
            txtScr.setBorder(BorderFactory.createTitledBorder(null, "聊天信息", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            txtScr.setBounds(235,80, 610, 450);
            txtScr.setBackground(new Color(255, 255, 255));
            txtScr.setFont(new Font("宋体", 0, 12));

            // 编辑信息区
            txtMsg = new JTextArea(4, 40);
            txtMsg.setLineWrap(true); // 激活自动换行功能
            txtMsg.setWrapStyleWord(true); // 换行不换字

            txtScroll = new JScrollPane(txtMsg);
            txtScroll.setBorder(BorderFactory.createTitledBorder(null, "发送", TitledBorder.DEFAULT_JUSTIFICATION,
                    TitledBorder.DEFAULT_POSITION, new Font("宋体", 0, 12), new Color(135, 206, 250)));
            txtScroll.setBounds(235, 540, 610,90);
            txtScroll.setBackground(new Color(250, 250, 250));
            txtScroll.setFont(new Font("宋体", 0, 12));

            // 发送按钮
            btnSmt = new JButton("发送");
            btnSmt.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        String s = txtMsg.getText();
                        if (!client.send(toUsername, s)) {
                            popWindows("对方不在线", "提示");
                        }
                        else {
                            submitText(s, strName);
                            txtMsg.setText("");
                        }
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            btnSmt.setFont(new Font("宋体", 0, 12));
            btnSmt.setBounds(720, 635, 100, 30);

            // 最终添加
            setLayout(null);
            add(topBar);
            add(leftBar);
            add(txtScr);
            add(txtScroll);
            add(btnSmt);

            // 设置界面可见
            setVisible(true);
//            receive();

        }

    }

    public void closeChatFrame() throws IOException {
        client.exit();
        this.dispose();
    }

    public void closeDiaLgnFrame() throws IOException {
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

    public void submitText(String s, String name) {
        synchronized (txtRcd) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
            txtRcd.setEditable(true);
            txtRcd.append(df.format(new Date()) + name + ":\n" + s + "\n\n");
            txtRcd.setEditable(false);
        }
    }

//    private void receive() throws IOException {
//        while (true) {
//            String string = client.receive();
//            String[] output = string.split("@");
//            switch (output[2]) {
//                case
//            }
//            if (output[0].equals("0")) {
//                synchronized (txtRcd) {
//                    txtRcd.setEditable(true);
//                    txtRcd.append(output[1] + ":\n    " + output[2] + "\n\n");
//                    txtRcd.setEditable(false);
//                }
//            }
////            System.out.println(string);
//
//        }
//    }

    // 弹出提示信息
    public void popWindows(String strWarning, String strTitle) {
        JOptionPane.showMessageDialog(this, strWarning, strTitle, JOptionPane.INFORMATION_MESSAGE);
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
        new test1();
    }

}

