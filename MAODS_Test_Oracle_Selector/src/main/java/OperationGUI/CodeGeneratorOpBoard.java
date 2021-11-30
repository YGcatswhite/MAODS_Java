package OperationGUI;

import MAODSRunner.MAODSRunner;

import javax.swing.*;
/**
 * 单例模式
 * 变异体和入口代码生成UI
 * @author 姬筠刚 191250055
 */
public class CodeGeneratorOpBoard extends JPanel {
    private static CodeGeneratorOpBoard operationBoard;
    private boolean isMutated=false;
    public static CodeGeneratorOpBoard getInstance(){
        if(operationBoard==null){
            operationBoard=new CodeGeneratorOpBoard();
        }
        return operationBoard;
    }
    private CodeGeneratorOpBoard(){
        this.drawUI();
        this.requestFocus();
    }
    /**
     * 画出UI
     */
    private void drawUI() {
        JFrame window = new JFrame("MAODSCodeGenerator");
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
        JLabel nameLabel=new JLabel("Your object's name: ");
        nameLabel.setBounds(10,20,200,25);
        this.add(nameLabel);
        JTextField nameFeild = new JTextField(20);
        nameFeild.setBounds(300,20,165,25);
        this.add(nameFeild);
        JLabel objectDotJavaLabel=new JLabel("Path of your object.java: ");
        objectDotJavaLabel.setBounds(10,50,200,25);
        this.add(objectDotJavaLabel);
        JTextField objectDotJavaFeild = new JTextField(20);
        objectDotJavaFeild.setBounds(300,50,165,25);
        this.add(objectDotJavaFeild);
        JLabel objectDotClassLabel=new JLabel("Path of your object.class: ");
        objectDotClassLabel.setBounds(10,80,200,25);
        this.add(objectDotClassLabel);
        JTextField objectDotClassFeild = new JTextField(20);
        objectDotClassFeild.setBounds(300,80,165,25);
        this.add(objectDotClassFeild);
        JLabel objectTestLabel=new JLabel("Path of your object's junit test file: ");
        objectTestLabel.setBounds(10,110,200,25);
        this.add(objectTestLabel);
        JTextField objectTestFeild = new JTextField(20);
        objectTestFeild.setBounds(300,110,165,25);
        this.add(objectTestFeild);
        JButton mutateButton = new JButton("Mutate");
        mutateButton.setBounds(10, 140, 80, 25);
        this.add(mutateButton);
        JButton generateButton = new JButton("Generate");
        generateButton.setBounds(110, 140, 120, 25);
        this.add(generateButton);
        //给按钮添加监听方法，被按下即触发
        mutateButton.addActionListener((actionEvent -> {
            if(!nameFeild.getText().equals("")&&!objectDotJavaFeild.getText().equals("")&&!objectDotClassFeild.getText().equals("")&&!objectTestFeild.getText().equals("")) {
                MAODSRunner.getInstance().MutateListener(nameFeild.getText(),objectDotJavaFeild.getText(),objectDotClassFeild.getText(),objectTestFeild.getText());
                isMutated=true;
            }
            else{
                System.out.println("请完整输入所有字段！");
            }
        }));
        //给按钮添加监听方法，被按下即触发
        generateButton.addActionListener((actionEvent -> {
            if(isMutated) {
                MAODSRunner.getInstance().GenerateListener(nameFeild.getText(),objectDotJavaFeild.getText());
                MAODSRunner.getInstance().GenerateEntryCode(nameFeild.getText());
                System.exit(0);
            }
            else{
                System.out.println("请先生成变异体再生成预言数据集！");
            }
        }));
    }
}
