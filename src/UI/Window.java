package UI;

import javax.swing.*;
import java.awt.*;

public class Window {
    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame jFrame = new JFrame("聊天");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = jFrame.getContentPane();
        container.add(new JLabel("你好"));
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
