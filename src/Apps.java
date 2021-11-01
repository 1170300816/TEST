import javax.swing.*;

public class Apps {
    // 程序入口
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        cal frame = new cal("计算器");
        frame.setVisible(true);          // 在桌面上显示窗体
    }
}
