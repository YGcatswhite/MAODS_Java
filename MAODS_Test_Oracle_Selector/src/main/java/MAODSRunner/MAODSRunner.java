package MAODSRunner;

import Mutator.MutantGenerator;
import ObjectExecutor.EntryCodeGenerator;
import ObjectExecutor.MutatorExecutor;
import ObjectExecutor.PrototypeExecutor;
import Ranker.FieldsRanker;
import Util.FileUtil;

import java.io.File;
import java.io.IOException;
/**
 * 单例模式
 * MAODS运行类，其中包括了UI界面各个按钮的监听器，执行UI端和后端的数据交换任务（相当于web项目的Controller）
 * @author 姬筠刚 191250055
 */
public class MAODSRunner {
    private static MAODSRunner maodsRunner;
    private MutantGenerator mutantGenerator;
    private MutatorExecutor mutatorExecutor;
    private PrototypeExecutor prototypeExecutor;
    private FieldsRanker fieldsRanker;
    public static MAODSRunner getInstance(){
        if(maodsRunner==null){
            maodsRunner=new MAODSRunner();
        }
        return maodsRunner;
    }
    private MAODSRunner(){
        mutantGenerator=MutantGenerator.getInstance();
        mutatorExecutor=MutatorExecutor.getInstance();
        prototypeExecutor=PrototypeExecutor.getInstance();
        fieldsRanker=FieldsRanker.getInstance();
    }
    /**
     * Mutate按钮监听方法
     * @param className 类名，由于该工具测试粒度为类级，对这个类进行方法级别的变异即可
     * @param objectJavaPath 原生类所在路径
     * @param objectClassPath 原生类class文件所在路径
     * @param objectTestPath 原生类单元测试文件所在路径，此文件是为了让Mujava工具正常运行才提供
     */
    public void MutateListener(String className,String objectJavaPath,String objectClassPath,String objectTestPath){
        mutantGenerator.MutantGenerate(className,objectJavaPath,objectClassPath,objectTestPath);
    }
    /**
     * Generate按钮监听方法
     * @param className 类名，由于该工具测试粒度为类级，对这个类进行方法级别的变异即可
     * @param objectJavaPath 原生类所在路径
     */
    public void GenerateListener(String className,String objectJavaPath){
        try {
            FileUtil.copyDir("..\\mujava\\mujavaHome\\result",".\\MutantsGenDir");
            mutantGenerator.CopyToTestSet(className,objectJavaPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 对原生类和变异体执行入口代码生成模块进行的一层封装
     * @param className 类名
     */
    public void GenerateEntryCode(String className){
        //入口代码生成模块用的方法过于暴力hhhh
        //获得复制过来的生成的变异体的数量
        int sumOfMu=(new File(".\\src\\main\\java\\Testset"+"\\"+className+"M")).list().length;
        //生成原生类代码（姑且叫它prototype）的执行入口代码即在ObjectExecutor.PrototypeEntryPackage包下的代码
        EntryCodeGenerator.getInstance().GenerateProgremOfProto(".\\src\\main\\java\\ObjectExecutor\\PrototypeEntryPackage\\GenedProtoEntry.java",className);
        //生成变异类代码（有很多个）的执行入口代码即在ObjectExecutor.MutatorEntryPackage包下的代码
        for(int i=1;i<=sumOfMu;i++) {
            EntryCodeGenerator.getInstance().GenerateProgremOfMu(".\\src\\main\\java\\ObjectExecutor\\MutatorEntryPackage"+"\\GenedMutantEntry"+i+".java",className,i);
        }
        //生成执行所有变异类入口方法的代码，这一段代码是MutantsResultGetter.java，在ObjectExecutor包下，负责根据已有测试输入执行所有的变异代码获得结果
        EntryCodeGenerator.getInstance().GenerateMutantsResultGetter(sumOfMu);
    }
    /**
     * Rank按钮监听方法
     * @param className 类名，由于该工具测试粒度为类级，对这个类进行方法级别的变异即可
     * @param setNum 用户指定的挑选测试预言字段集的大小，要挑选多少个字段，若为0则代表用户未指定，挑选变异体杀死率最高的字段集
     */
    public void RankListener(String className,int setNum){
        executeGenedCode(className);
        fieldsRanker.rankResultGenerate(className,setNum);
    }
    /**
     * 对原生类和变异体执行入口代码执行模块进行的最后一层封装
     * @param className 类名
     */
    public void executeGenedCode(String className){
        mutatorExecutor.execute();
        prototypeExecutor.execute(className);
    }
}
