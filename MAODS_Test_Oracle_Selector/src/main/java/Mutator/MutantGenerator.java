package Mutator;


import Util.CMDUtil;
import Util.FileUtil;

import java.io.File;
import java.io.IOException;

/**
 * 单例模式
 * 待测类变异体生成器
 * @author 姬筠刚 191250055
 */
public class MutantGenerator {
    private static MutantGenerator mutantGenerator;
    public static MutantGenerator getInstance(){
        if(mutantGenerator==null){
            mutantGenerator=new MutantGenerator();
        }
        return mutantGenerator;
    }
    private MutantGenerator(){
    }
    //C:\Users\jiyun\Desktop\MAODS_Java\MAODS_Test_Case\Calculator.java
    //C:\Users\jiyun\Desktop\MAODS_Java\MAODS_Test_Case\Calculator.class
    //C:\Users\jiyun\Desktop\MAODS_Java\MAODS_Test_Case\CalculatorTest.java
    /**
     * 使用Mujava工具生成变异体，通过命令行CMD进行操作
     * @param className 类名，由于该工具测试粒度为类级，对这个类进行方法级别的变异即可
     * @param objectJavaPath 原生类所在路径
     * @param objectClassPath 原生类class文件所在路径
     * @param objectTestPath 原生类单元测试文件所在路径，此文件是为了让Mujava工具正常运行才提供
     */
    public void MutantGenerate(String className,String objectJavaPath,String objectClassPath,String objectTestPath){
        try {
            FileUtil.copyFile(objectJavaPath,"..\\mujava\\mujavaHome\\src\\"+className+".java");
            FileUtil.copyFile(objectClassPath,"..\\mujava\\mujavaHome\\classes\\"+className+".class");
            FileUtil.copyFile(objectTestPath,"..\\mujava\\mujavaHome\\testset\\"+className+"Test.java");
            CMDUtil.excuteBatFile("..\\mujava\\mujavaHome\\GenMutants.cmd",false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 将原生类和变异体拷贝到Testset包下
     * @param className 类名，对这个类的原生类和变异体进行操作
     * @param objectJavaPath 原生类所在路径
     */
    public void CopyToTestSet(String className,String objectJavaPath){
        File file = new File(".\\MutantsGenDir\\"+className+"\\traditional_mutants");
        String[] filePath=file.list();
        int k=1;
        try {
            FileUtil.copyFile(objectJavaPath,".\\src\\main\\java\\Testset\\"+className+".java");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String header = "package Testset;\n";
        try {
            FileUtil.appendFileHeader(header.getBytes(),".\\src\\main\\java\\Testset\\"+className+".java");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0;i<filePath.length;i++){
            if(filePath[i].equals("void_Entry()"))
                continue;
            File childFile=new File(".\\MutantsGenDir\\"+className+"\\traditional_mutants" + file.separator + filePath[i]);
            if((childFile.isDirectory())){
                if (!(new File(".\\src\\main\\java\\Testset\\"+className+"M")).exists()) {
                    (new File(".\\src\\main\\java\\Testset\\"+className+"M")).mkdir();
                }
                String[] childFilePath=childFile.list();
                for(int j=0;j<childFilePath.length;j++){
                    File childChildFile=new File(".\\MutantsGenDir\\"+className+"\\traditional_mutants" + file.separator + filePath[i]+childFilePath[j]+className+".java");
                    (new File(".\\src\\main\\java\\Testset\\"+className+"M"+file.separator+className+k)).mkdir();
                    try {
                        FileUtil.copyFile(".\\MutantsGenDir\\"+className+"\\traditional_mutants" + file.separator + filePath[i]+file.separator+childFilePath[j]+file.separator+className+".java",
                                ".\\src\\main\\java\\Testset\\"+className+"M"+file.separator+className+k+file.separator+className+".java");
                        String headerPac = "package Testset."+className+"M"+"."+className+k+";\n";
                        try {
                            FileUtil.appendFileHeader(headerPac.getBytes(),".\\src\\main\\java\\Testset\\"+className+"M"+file.separator+className+k+file.separator+className+".java");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        k++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
