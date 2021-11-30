package ObjectExecutor;
/**
 * 单例模式
 * 变异体运行类，运行[所有变异体入口集合]，实际上是做了一层封装
 * @author 姬筠刚 191250055
 */
public class MutatorExecutor {
    private static MutatorExecutor mutatorExecutor;
    private MutantsResultGetter mutantsResultGetter=new MutantsResultGetter();
    public static MutatorExecutor getInstance(){
        if(mutatorExecutor==null){
            mutatorExecutor=new MutatorExecutor();
        }
        return mutatorExecutor;
    }
    private MutatorExecutor(){
    }
    /**
     * 运行所有变异体
     */
    public void execute(){
        mutantsResultGetter.getAllMutantsResult();
    }
}
