package MAODSRunner;

import OperationGUI.CodeGeneratorOpBoard;
/**
 * 控制 变异 和 生成指定类及其变异体执行代码 的入口点
 * @author 姬筠刚 191250055
 */
public class EntryOfCodeGenerator {
    public static void main(String[] args) {
        //获取程序生成操作页面GUI，执行MAODS程序
        CodeGeneratorOpBoard.getInstance();
    }
}
