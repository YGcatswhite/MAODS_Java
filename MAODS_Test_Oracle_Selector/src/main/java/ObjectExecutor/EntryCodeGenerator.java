package ObjectExecutor;

import Util.FileUtil;
/**
 * 单例模式
 * 指定的原生类和变异体入口代码生成类，生成的代码分别放到MutatorEntryPackage、PrototypeEntryPackage、MutantsResultGetter.java里
 * 其中MutatorEntryPackage是包含所有变异体执行代码的包
 * PrototypeEntryPackage是包含原生类执行代码的包
 * MutantsResultGetter.java是统一执行所有变异体执行代码入口，得到变异体执行结果
 * @author 姬筠刚 191250055
 */
public class EntryCodeGenerator {
    private static EntryCodeGenerator entryCodeGenerator;
    public static EntryCodeGenerator getInstance(){
        if(entryCodeGenerator ==null){
            entryCodeGenerator =new EntryCodeGenerator();
        }
        return entryCodeGenerator;
    }
    private EntryCodeGenerator(){
    }
    /**
     * 生成原生类执行入口代码
     * @param srcPath 生成代码所在的路径
     * @param className 类名，为这个类生成执行入口点
     */
    public void GenerateProgremOfProto(String srcPath,String className){
        FileUtil.cleanFile(srcPath);
        try {
            String code="package ObjectExecutor.PrototypeEntryPackage;\n" +
                    "\n" +
                    "import Testset."+className+";\n" +
                    "import Util.FileUtil;\n" +
                    "\n" +
                    "import java.lang.reflect.Field;\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.Arrays;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "//这是自动生成的类哦"+
                    "\n"+
                    "public class GenedProtoEntry {\n" +
                    "    public void EntryMethod(Object obj){\n" +
                    "        "+className+" c1 = (" +className+") obj;\n" +
                    "        c1.Entry();\n" +
                    "        Class clazz = "+className+".class;\n" +
                    "        List<Field> allFields = new ArrayList<>();\n" +
                    "        // 获取当前对象的所有属性字段\n" +
                    "        // clazz.getFields()：获取public修饰的字段\n" +
                    "        // clazz.getDeclaredFields()： 获取所有的字段包括private修饰的字段\n" +
                    "\n"+
                    "        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));\n" +
                    "        final String[] content = {\"\"};\n" +
                    "        allFields.stream().forEach(field -> {\n" +
                    "            // 设置字段可访问， 否则无法访问private修饰的变量值\n" +
                    "\n"+
                    "            field.setAccessible(true);\n" +
                    "            try {\n" +
                    "                // 获取字段名称\n" +
                    "                String fieldName = field.getName();\n" +
                    "                // 获取指定对象的当前字段的值\n" +
                    "\n"+
                    "                Object fieldVal = field.get(c1);\n" +
                    "                content[0] = content[0] +fieldName+\"=\"+fieldVal+\"\\n\";\n" +
                    "            } catch (IllegalAccessException e) {\n" +
                    "                e.printStackTrace();\n" +
                    "            }\n" +
                    "        });\n" +
                    "        FileUtil.writeToFile(content[0],\".\\\\result\\\\PrototypeResult.txt\");\n" +
                    "        System.out.println(\"原生类已执行完毕\");"+
                    "    }\n" +
                    "}";
            FileUtil.writeToFile(code,srcPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 生成指定序号变异体执行入口代码
     * @param srcPath 生成代码所在的路径
     * @param className 类名，为这个类的变异体生成执行入口点
     * @param numOfMu 变异体次序，表示为序号为numOfMu的变异体生成执行入口点
     */
    public void GenerateProgremOfMu(String srcPath,String className,int numOfMu){
        FileUtil.cleanFile(srcPath);
        try {
            String code="package ObjectExecutor.MutatorEntryPackage;\n" +
                    "\n" +
                    "import Testset."+className+"M."+className+numOfMu+"."+className+";\n" +
                    "import Util.FileUtil;\n" +
                    "\n" +
                    "import java.lang.reflect.Field;\n" +
                    "import java.util.ArrayList;\n" +
                    "import java.util.Arrays;\n" +
                    "import java.util.List;\n" +
                    "\n" +
                    "//这是自动生成的类哦"+
                    "\n"+
                    "public class GenedMutantEntry"+numOfMu+" {\n" +
                    "    public void EntryMethod(){\n" +
                    "        Class cl=null;\n" +
                    "        try {\n" +
                    "            cl=Class.forName(\"Testset."+className+"M."+className+numOfMu+"."+className+"\""+");\n" +
                    "        } catch (ClassNotFoundException e) {\n" +
                    "            e.printStackTrace();\n" +
                    "        }"+
                    "       Object obj=null;\n" +
                    "       try {\n" +
                    "           obj= cl.newInstance();\n" +
                    "       } catch (InstantiationException e) {\n" +
                    "           e.printStackTrace();\n" +
                    "       } catch (IllegalAccessException e) {\n" +
                    "           e.printStackTrace();\n" +
                    "       }\n"+
                    "        "+className+" c1 = (" +className+") obj;\n" +
                    "        c1.Entry();\n" +
                    "        Class clazz = "+className+".class;\n" +
                    "        List<Field> allFields = new ArrayList<>();\n" +
                    "        // 获取当前对象的所有属性字段\n" +
                    "        // clazz.getFields()：获取public修饰的字段\n" +
                    "        // clazz.getDeclaredFields()： 获取所有的字段包括private修饰的字段\n" +
                    "\n"+
                    "        allFields.addAll(Arrays.asList(clazz.getDeclaredFields()));\n" +
                    "        final String[] content = {\"\"};\n" +
                    "        allFields.stream().forEach(field -> {\n" +
                    "            // 设置字段可访问， 否则无法访问private修饰的变量值\n" +
                    "\n"+
                    "            field.setAccessible(true);\n" +
                    "            try {\n" +
                    "                // 获取字段名称\n" +
                    "                String fieldName = field.getName();\n" +
                    "                // 获取指定对象的当前字段的值\n" +
                    "\n"+
                    "                Object fieldVal = field.get(c1);\n" +
                    "                content[0] = content[0] +fieldName+\"=\"+fieldVal+\"\\n\";\n" +
                    "            } catch (IllegalAccessException e) {\n" +
                    "                e.printStackTrace();\n" +
                    "            }\n" +
                    "        });\n" +
                    "        FileUtil.writeToFile(content[0],\".\\\\result\\\\MuResult"+numOfMu+".txt\");\n" +
                    "        System.out.println(\"变异体"+numOfMu+"已执行完毕\");"+
                    "    }\n" +
                    "}";
            FileUtil.writeToFile(code,srcPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 为所有变异体执行入口点生成一层封装执行类，它是所有变异体执行代码的入口点
     * @param sumOfMu 变异体总数
     */
    public void GenerateMutantsResultGetter(int sumOfMu){
        String code="package ObjectExecutor;\n";
        for(int i=1;i<=sumOfMu;i++){
            code=code+"import ObjectExecutor.MutatorEntryPackage.GenedMutantEntry"+i+";\n";
        }
        String classCode="//这是自动生成的类哦\n" +
                "\n"+
                "public class MutantsResultGetter {\n" +
                "    public void getAllMutantsResult(){\n";
        for(int j=1;j<=sumOfMu;j++){
            classCode=classCode+"        GenedMutantEntry"+j+" m"+j+"=new GenedMutantEntry"+j+"();\n" +
                    "        m"+j+".EntryMethod();\n";
        }
        classCode=classCode+"    }\n" +
                "}\n";
        code=code+classCode;
        FileUtil.writeToFile(code,".\\src\\main\\java\\ObjectExecutor\\MutantsResultGetter.java");
    }
}