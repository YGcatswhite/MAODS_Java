package Ranker.RankStrategy;

import java.util.List;
/**
 * 策略模式
 * 排序策略类接口
 * 后续可以根据策略模式进行功能扩展，原本想写个基于静态分析的策略来着，时间不够了==
 * @author 姬筠刚 191250055
 */
public interface RankStrategy {
    /**
     * 排序策略接口
     * @param isFeildKilling 二维数组表示哪个字段可以杀死哪个变异程序
     * @param feildsKillNum 数组表示哪个字段可以杀死多少变异程序
     * @return List<String> 排序得到的字段-预言值列表
     */
    public List<String> rank(boolean[][] isFeildKilling, int[] feildsKillNum);
}
