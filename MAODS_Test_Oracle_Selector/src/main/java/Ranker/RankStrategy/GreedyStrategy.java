package Ranker.RankStrategy;

import Util.ArrayUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/**
 * 策略模式
 * 贪心策略实现，具体算法详见本项目文档
 * 后续可以根据策略模式进行功能扩展
 * @author 姬筠刚 191250055
 */
public class GreedyStrategy implements RankStrategy{
    /**
     * 基于贪心策略的排序算法
     * 原文算法描述如下：选择覆盖最多集合的元素，删除被该元素覆盖的所有集合，
     * 重复以上过程直到所有集合都被覆盖（每个集合代表一个突变体，该集合的每个元素是变量），
     * 按照被移除的顺序进行排序
     *
     * 但是我使用的数据结构有所改变即该方法使用的两个数组
     *
     * @param isFeildKilling 二维数组表示哪个字段可以杀死哪个变异程序
     * @param fieldsKillNum 数组表示哪个字段可以杀死多少变异程序
     * @return List<String> 排序得到的字段-预言值列表
     */
    @Override
    public List<String> rank(boolean[][] isFeildKilling, int[] fieldsKillNum) {
        //适用贪心策略进行字段优先级排序
        int sumOfFields=fieldsKillNum.length;
        int sumOfMutants=isFeildKilling[0].length;

        //setOfMutants数组true代表位于这个索引的变异程序已经被覆盖
        boolean[] isMutantsCovered=new boolean[sumOfMutants];

        int[] rankFieldsIndex=new int[sumOfFields];
        List<String> rankResult=new ArrayList<>();
        List<String> allFields=new ArrayList<>();

        //这是一个贪心循环终止的条件，当所有的变异体都被覆盖，程序就会停止贪心循环
        boolean isAllMutantsCovered=false;
        //这是一个贪心循环终止的条件，当所有的字段覆盖当前还剩下的变异体数量全部为0（即字段无法杀死任何变异体），程序就会停止贪心循环
        boolean isAllFeildsZero=false;

        int nowMaxIndexOfFieldsKillNum= ArrayUtil.selectMax(fieldsKillNum);
        int rankListLength=0;
        rankFieldsIndex[rankListLength]=nowMaxIndexOfFieldsKillNum;
        rankListLength++;

        //贪心循环
        while(!isAllFeildsZero&&!isAllMutantsCovered){
            //更新isMutantsCovered数组，剔除已经被覆盖的变异体
            //更新fieldsKillNum数组，根据贪心策略，当某个变异体被覆盖时，检测出该变异体的字段所检测数的变异体数量应该减一（剔除已经被覆盖的变异体），之后才能在从中取最大值
            for(int j=0;j<sumOfMutants;j++){
                if(isFeildKilling[nowMaxIndexOfFieldsKillNum][j]){
                    if(!isMutantsCovered[j]) {//如果之前没有被覆盖就更新两个数组
                        isMutantsCovered[j] = true;//被覆盖了
                        for (int k = 0; k < sumOfFields; k++) {//更新fieldsKillNum数组，来进行下一个贪心选择
                            if (isFeildKilling[k][j])
                                fieldsKillNum[k]--;
                        }
                    }
                }
            }
            //检查是否所有变异体都被覆盖了
            for(int i=0;i<isMutantsCovered.length;i++){
                if(!isMutantsCovered[i])
                    break;
                if(i==isMutantsCovered.length-1)
                    isAllMutantsCovered=true;
            }
            //检查是否所有字段能杀死的变异体数目都为0了
            for(int i=0;i<fieldsKillNum.length;i++){
                if(fieldsKillNum[i]!=0)
                    break;
                if(i==fieldsKillNum.length-1)
                    isAllFeildsZero=true;
            }
            //用于防止越界情况或最后一个覆盖变异体数目为0的字段进入结果序列的情况发生
            if(rankListLength>=24||isAllFeildsZero||isAllMutantsCovered)
                break;
            nowMaxIndexOfFieldsKillNum= ArrayUtil.selectMax(fieldsKillNum);
            rankFieldsIndex[rankListLength]=nowMaxIndexOfFieldsKillNum;
            rankListLength++;
        }

        //根据排序结果得到字段名称及预言的序列
        File finProto=new File(".\\result\\PrototypeResult.txt");
        FileInputStream fisProto = null;
        try {
            fisProto = new FileInputStream(finProto);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader brProto = new BufferedReader(new InputStreamReader(fisProto));
        String lineProto = null;
        //读入所有字段名称及预言
        while (true) {
            try {
                if (!((lineProto = brProto.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            allFields.add(lineProto);
        }
        //根据排序结果选择从所有字段名称及预言中进行选择
        for(int j=0;j<rankListLength;j++){
            rankResult.add(allFields.get(rankFieldsIndex[j]));
        }
        return rankResult;
    }
}
