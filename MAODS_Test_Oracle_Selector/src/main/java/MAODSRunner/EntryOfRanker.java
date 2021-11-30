package MAODSRunner;

import OperationGUI.RankerOpBoard;
/**
 * 控制 执行指定类及其变异体 和 生成测试预言数据集选择结果 的入口点
 * @author 姬筠刚 191250055
 */
public class EntryOfRanker {
    public static void main(String[] args) {
        //获取字段排名操作页面GUI，执行MAODS程序
        RankerOpBoard.getInstance();
    }
}
