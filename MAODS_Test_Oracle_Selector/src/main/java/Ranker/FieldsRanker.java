package Ranker;


import Ranker.RankStrategy.GreedyStrategy;
import Ranker.RankStrategy.RankStrategy;
import Util.FileUtil;

import java.io.*;
import java.util.List;
/**
 * 单例模式
 * 对字段排序并根据需求选出出测试预言字段集结果的操作类
 * 该类目前使用基于贪心策略的算法完成任务，后续可以根据策略模式进行功能扩展
 * @author 姬筠刚 191250055
 */
public class FieldsRanker {
    private static FieldsRanker fieldsRanker;
    private RankStrategy rankStrategy=new GreedyStrategy();

    //这个二维数组表示哪个字段可以杀死哪个变异程序
    public boolean[][] isFeildKilling;
    //这个数组表示哪个字段可以杀死多少变异程序
    public int[] feildsKillNum;
    //这个数组表示哪个变异程序被杀死了或没被杀死
    public boolean[] isKilled;

    public static FieldsRanker getInstance(){
        if(fieldsRanker==null){
            fieldsRanker=new FieldsRanker();
        }
        return fieldsRanker;
    }
    private FieldsRanker(){
    }

    /**
     * 将原生类运行结果和变异体运行结果进行比对，
     * 得到哪个字段可以杀死哪个变异程序、
     * 哪个字段可以杀死多少变异程序和哪个变异程序被杀死了或没被杀死，
     * 并存到该类的三个数组字段中
     * @param className 原生类的类名称，用于反射
     */
    public void CompareResuls(String className){
        Class cl=null;
        try {
            cl=Class.forName("Testset."+className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int sumOfFeilds=cl.getDeclaredFields().length;
        int sumOfMu=(new File(".\\src\\main\\java\\Testset"+"\\"+className+"M")).list().length;
        isFeildKilling= new boolean[sumOfFeilds][sumOfMu];
        feildsKillNum=new int[sumOfFeilds];
        isKilled=new boolean[sumOfMu];
        for(int i=0;i<sumOfMu;i++){
            int feildsOrder=0;
            File finMu=new File(".\\result\\MuResult"+(i+1)+".txt");
            File finProto=new File(".\\result\\PrototypeResult.txt");
            FileInputStream fisMu = null;
            try {
                fisMu = new FileInputStream(finMu);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader brMu = new BufferedReader(new InputStreamReader(fisMu));
            String lineMu = null;
            FileInputStream fisProto = null;
            try {
                fisProto = new FileInputStream(finProto);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader brProto = new BufferedReader(new InputStreamReader(fisProto));
            String lineProto = null;
            while (true) {
                //按行比对读取文件
                try {
                    if (!((lineMu = brMu.readLine()) != null)||!((lineProto = brProto.readLine()) != null)||feildsOrder>=sumOfFeilds) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(lineMu.equals(lineProto)){
                    isFeildKilling[feildsOrder][i]=false;
                    isKilled[i]=false;
                }else{
                    isFeildKilling[feildsOrder][i]=true;
                    feildsKillNum[feildsOrder]++;
                    isKilled[i]=true;
                }
                feildsOrder++;
            }
        }
    }
    /**
     * 使用基于贪心策略的算法原生类进行字段排序，
     * 变异体杀死能力越高越排在前面，并得到挑选的得到的测试预言字段集，
     * 结果以每行 [字段名称]=[原生类中运行得到的值] 的形式存在RankResult.txt中
     * @param className 原生类名
     * @param setNum 用户指定的挑选测试预言字段集的大小，要挑选多少个字段，若为0则代表用户未指定，挑选变异体杀死率最高的字段集
     */
    public void rankResultGenerate(String className,int setNum){
        CompareResuls(className);
        List<String> result=rankStrategy.rank(isFeildKilling,feildsKillNum);
        int numToSelect=0;
        if(setNum<result.size()&&setNum!=0)
            numToSelect=setNum;
        else
            numToSelect=result.size();
        //将排序并选择得到的结果写入文件
        String resultContent="";
        for(int i=0;i<numToSelect;i++){
            resultContent=resultContent+result.get(i)+"\n";
        }
        FileUtil.writeToFile(resultContent,".\\RankResult.txt");
    }
}
