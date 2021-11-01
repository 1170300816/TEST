import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;


public class cal extends JFrame {

    private Border border = BorderFactory.createEmptyBorder(20, 20, 20, 20);

    private JTextField textbox = new JTextField("0");
    private JTextField result = new JTextField("");
    private boland  bd=new boland();
    private listen core = new listen();

    private ActionListener listener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();
            String label = b.getText();
            String textresult = core.process(label);
            textbox.setText("");
            if(b.getText()=="C"|b.getText()=="CE"){
                result.setText("");
                textbox.setText("0");
            }
            else if(b.getText()=="="){
                result.setText(result.getText());
                textbox.setText(""+(float)bd.mecalc(result.getText().toString()));
            }
            else if(b.getText()!="CE"&&b.getText()!="C"){
                result.setText(result.getText()+label);
            }
        }
    };

    public cal(String title) throws HeadlessException {
        super(title);       // 调用父类构造方法
        setupFrame();       // 调整窗体属性
        setupControls();    // 创建控件
    }

    private void setupControls() {
        setupDisplayPanel();    // 创建文本面板
        setupDisplayPanel();    // 创建文本面板
        setupButtonsPanel();    // 创建按钮面板
    }

    // 创建按钮面板并添加按钮
    private void setupButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(border);
        panel.setLayout(new GridLayout(4, 5, 3, 3));

        createButtons(panel, new String[]{
                "7", "8", "9", "+", "C",
                "4", "5", "6", "-", "CE",
                "1", "2", "3", "*", "",  // 空字符串表示这个位置没有按钮
                "0", ".", "=", "/", ""
        });

        this.add(panel, BorderLayout.CENTER);
    }

    /**
     * 在指定的面板上创建按钮
     *
     * @param panel  要创建按钮的面板
     * @param labels 按钮文字
     */
    private void createButtons(JPanel panel, String[] labels) {
        for (String label : labels) {
            // 如果 label 为空，则表示创建一个空面板。否则创建一个按钮。
            if (label.equals("")) {
                panel.add(new JPanel());

            } else {
                JButton b = new JButton(label);
                b.addActionListener(listener);  // 为按钮添加侦听器
                panel.add(b);
            }
        }
    }

    // 设置显示面板，用一个文本框来作为计算器的显示部分。
    private void setupDisplayPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(border);
        setupTextbox();
        panel.add(textbox, BorderLayout.CENTER);
        panel.add(result, BorderLayout.NORTH);
        this.add(panel, BorderLayout.NORTH);
    }

    // 调整文本框
    private void setupTextbox() {
        textbox.setHorizontalAlignment(JTextField.RIGHT);   // 文本右对齐
        textbox.setEditable(false);                         // 文本框只读
        textbox.setBackground(Color.white);                 // 文本框背景色为白色
        result.setHorizontalAlignment(JTextField.RIGHT);
        result.setEditable(false);
        result.setBackground(Color.white);
    }

    // 调整窗体
    private void setupFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);   // 当窗体关闭时程序结束
        this.setLocation(100, 50);      // 设置窗体显示在桌面上的位置
        this.setSize(500, 500);         // 设置窗体大小
        this.setResizable(true);       // 窗体大小非固定
    }


}

