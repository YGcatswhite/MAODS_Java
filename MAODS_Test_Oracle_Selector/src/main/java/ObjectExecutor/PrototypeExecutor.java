package ObjectExecutor;


import ObjectExecutor.PrototypeEntryPackage.GenedProtoEntry;

import java.lang.reflect.Method;
/**
 * 单例模式
 * 原生类运行类，运行原生类入口点，实际上是做了一层封装
 * @author 姬筠刚 191250055
 */
public class PrototypeExecutor {
    private static PrototypeExecutor prototypeExecutor;
    private String className;
    private GenedProtoEntry genedProtoEntry=new GenedProtoEntry();
    public static PrototypeExecutor getInstance(){
        if(prototypeExecutor==null){
            prototypeExecutor=new PrototypeExecutor();
        }
        return prototypeExecutor;
    }
    private PrototypeExecutor(){
    }
    /**
     * 通过使用包名+类名反射获得相应的类
     * @param className 类名
     */
    public Class getInstanceOfProtorype(String className){
        Class cl=null;
        try {
            cl=Class.forName("Testset."+className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cl;
    }
    /**
     * 运行原生类
     * @param className 类名
     */
    public void execute(String className){
        Class cl=getInstanceOfProtorype(className);
        try {
            genedProtoEntry.EntryMethod(cl.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
