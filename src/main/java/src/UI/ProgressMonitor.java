package src.UI;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;


/** a self-defined public class for file load process
 *  @author 康曦文
 *  @since 13.0.2
 * */
public class ProgressMonitor {

    public void progressMonitorBar(JFrame frame, InputStream in) {
        try {
            // 读取文件，如果总耗时超过2秒，将会自动弹出一个进度监视窗口。
            ProgressMonitorInputStream pm = new ProgressMonitorInputStream(frame, "文件发送中，请稍后...", in);
            pm.close(); // 关闭输入流
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}