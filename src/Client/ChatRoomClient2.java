package Client;


import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class ChatRoomClient2 extends JFrame {
    Client client = new Client("localhost",1111);
    private JPanel contentPane;
    private JTextField textField;
    private JTextArea textArea;

    /**
     * Launch the application.
     */
    public void accept() throws Exception{
//        String[] ret = client.receive();
//        textArea.append(ret[1] + ":" + ret[2] + "\n");
    }
    public void send(String str) throws Exception{

    }
    public static void main(String[] args) {
        try {
            ChatRoomClient2 frame = new ChatRoomClient2();
            frame.setVisible(true);
            frame.client.Login("cdf", "123");
//			Scanner in = new Scanner(System.in);
//			in.next();
            while(true)
                frame.accept();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public ChatRoomClient2() throws IOException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        textArea = new JTextArea();
        textArea.setRows(15);
        textArea.setColumns(25);
        contentPane.add(new JScrollPane(textArea), BorderLayout.WEST);

        textField = new JTextField();
        contentPane.add(textField, BorderLayout.SOUTH);
        textField.setColumns(10);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.EAST);
        JButton btnNewButton = new JButton("发送");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.append(client.getUsername()+":"+textField.getText()+"\n");
                try {
                    client.send("abc", textField.getText());
//                    send(textField.getText());
                    textField.setText("");
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("退出");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(btnNewButton_1);


    }

}

