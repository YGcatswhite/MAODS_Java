package OperationGUI;

import MAODSRunner.MAODSRunner;

import javax.swing.*;
/**
 * 单例模式
 * 待测类字段排序和测试预言字段集选择UI
 * @author 姬筠刚 191250055
 */
public class RankerOpBoard extends JPanel{
    private static RankerOpBoard rankerOpBoard;
    public static RankerOpBoard getInstance(){
        if(rankerOpBoard==null){
            rankerOpBoard=new RankerOpBoard();
        }
        return rankerOpBoard;
    }

    private RankerOpBoard(){
        this.drawUI();
        this.requestFocus();
    }
    /**
     * 画出UI
     */
    private void drawUI() {
        JFrame window = new JFrame("MAODSRanker");
        window.setSize(500, 250);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //window.setLayout(new BorderLayout());
        window.add(this);
        this.placeComponents();
        window.setVisible(true);
    }
    /**
     * 向UI中添加文本框、按钮等元素
     */
    private void placeComponents(){
        this.setLayout(null);
        JLabel nameLabel=new JLabel("Input your class name to rank: ");
        nameLabel.setBounds(10,20,200,25);
        this.add(nameLabel);
        JTextField nameFeild = new JTextField(20);
        nameFeild.setBounds(300,20,165,25);
        this.add(nameFeild);
        JLabel numLabel=new JLabel("Input what number of fields you want(如果没要求，请填写0): ");
        numLabel.setBounds(10,50,400,25);
        this.add(numLabel);
        JTextField numFeild = new JTextField(20);
        numFeild.setBounds(10,80,165,25);
        this.add(numFeild);
        JButton rankButton = new JButton("Rank and Generate");
        rankButton.setBounds(10, 140, 200, 25);
        this.add(rankButton);
        //给按钮添加监听方法，被按下即触发
        rankButton.addActionListener((actionEvent -> {
            if(!nameFeild.getText().equals("")) {
                MAODSRunner.getInstance().RankListener(nameFeild.getText(),Integer.parseInt(numFeild.getText()));
                System.exit(0);
            }
            else{
                System.out.println("请完整输入类名！");
            }
        }));
    }
}
