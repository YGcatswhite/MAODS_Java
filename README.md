# MAODS_Java设计文档

##### **191250055 姬筠刚**

------

## 一、写在前面

MAODS_Java是我在南京大学软件学院2021年秋自动化测试课程的大作业，这是使用Java预言对MAODS工具的复现。前后经历21天的开发时间（除去划水肝其他ddl的天数大概两周左右），代码均为根据论文描述独立构建（除Util包下部分类的部分方法来源于stackoverflow热心网友回答外），论文要求的工具功能都已实现。这是我第一次独立开发一款完整的工具，收获颇深。

这次工具复现共有以下几点收获：

1. 对Java反射机制有了更深入的了解并能灵活使用，这在之前的专业课学习中并未接触；
2. 学习了Mujava和Pitest变异工具的使用并有了深入了解；
3. 掌握了java.swing构建GUI页面的方法；
4. 了解了程序动态分析的工作原理，能够简单地完成程序生成任务，虽然是通过暴力的方法生成大量结构相似的类；
5. 对设计模式掌握更牢了，这个项目使用了单例模式和策略模式，极大地提高了程序的安全性和可扩展性，能够应对策略的变更；

本次作业的清单有：

1. 复现源码：在仓库根目录下的MAODS_Test_Oracle_Selector项目
2. 工具开发日志：仓库根目录下LOG.md
3. 工具设计文档：仓库目录下README.md，即本文档
4. 实验结果：仓库根目录下的实验结果文件夹，其中包含测试用例（自己编写的待测类Calculator）、原始系统和变异体运行的各字段对应的值的结果（在其中的RunResult文件夹）、排序选择得到的测试预言字段集合（RankResult.txt）
5. 演示视频：仓库根目录下 演示视频.mp4
6. 工具汇报PPT：仓库根目录下 MAODS工具复现汇报.pptx
7. 功能模块及数据交互图：仓库根目录下的 MAODS功能模块和数据交互图（基于类图）.png

**另外，本文档无过多代码的粘贴，代码注释很详细**

## 二、工具介绍

测试预言是自动化测试领域的一项起步比较早的技术，该技术根据程序分析的方式不同分为静态分析技术和动态分析技术，而MAODS则是一项基于动态分析的测试预言选择工具。由于在工业中待测类可能包含的字段非常多，如果对所有字段进行测试预言虽然故障检测率会达到最高但是成本很高，因此我们应当让被选择进行测试预言的字段尽可能少，同时还要保证故障检测效率最大化。

MAODS应运而生，MAODS目标是通过选择测试数据来支持测试预言的创建，以便在成本方面最大化测试过程的故障查找潜力。具体方法如下：首先从被测系统中生成变异体集合；然后使用原始系统作为预言（使用的是自动化的背靠背测试，即对某个部件的两个版本进行相同的输入）针对变异体运行测试套件；第三我们测量系统中的每个变量检测出变异体故障的概率，并基于这些信息根据故障查找对变量有效性进行排序（通过基于贪心策略的字段排序算法得到每个字段变异杀死效率的排序）。最后我们根据这个排名评估哪些变量应该被包含在测试数据里为一些期望值数据预言。一旦测试数据被选择后，测试人员为预言数据的每个元素定义预期值。然后测试就可以从这个小型且高效的预言开始了。

文献中没有明确要求该测试预言测试工具应用的粒度，由于反射机制是基于类的，我们复现的MAODS_Java测试粒度为类级，对类内的成员变量（下面统称字段）进行排序选择。

## 三、MAODS核心算法

MAODS的核心算法是执行自动化背靠背测试和基于贪心策略的排序算法。

### 1、MAODS自动化背靠背测试

背靠背测试是对某个部件的两个版本进行相同的输入，比对输出是否相同。在MAODS系统中，通过向原始系统中进行测试输入得到输出（即执行程序后每个字段的值）作为预言P，然后向所有变异体进行相同的测试输入得到输出（即执行程序后每个字段的值）同预言P进行比较，看哪些变异体能够被哪些字段杀死。

下面是我实现背靠背测试的代码（针对的系统是Calculator类，具体代码见仓库根目录里的MAODS_Test_Case文件夹）：

测试输入统一为：

```java
public void Entry(){
        op1Double=17;
        op2Double=5;
        op1Int=16;
        op2Int=6;
        op1String="bilibli";
        op2String="pilipili";
        append(1,"，干杯！");
        append(2,",cheers");
        add(op1Double,op2Double);
        sub(op1Double,op2Double);
        //......详细请看源码
    }
```

背靠背测试结果比对：

```java
for(int i=0;i<sumOfMu;i++){ //对每个变异体执行结果进行遍历
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
        //按行比对读取文件，即得到变异体与字段预言值的不同
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
```

### 2、MAODS基于贪心策略的排序算法

最简单的贪心是每轮挑选杀死变异体最多的字段，但是这样做挑选出来的故障检测率不一定高，原因如下：**单个变量的有效性可能是高度相关的，比如当使用某个变量A计算变量B时，如果A对于某些测试输入不正确则B也会不正确，意思就是两个等价变量相当于等价，取出其中一个来预言就可以，两个都取出有些浪费。**

所以我们采用更复杂但得到的测试预言字段集合更高效的贪心算法，首先定义每个集合代表一个突变体，该集合的每个元素是能够杀死对应突变体的字段，算法描述如下：

1. 选择覆盖最多集合的元素
2. 删除被最多元素覆盖的集合，以更新得到目前还剩下的的突变体和对应能杀死该突变体的字段
3. 重复以上过程直到所有集合被覆盖（所有变异体都被杀死）或所有集合中都没有元素了（所有字段都不能杀死剩下的变异体）

实现代码概述如下（省略号是省掉的代码，具体详见源码）：

```java
public List<String> rank(boolean[][] isFeildKilling, int[] fieldsKillNum) {
    //......
    //setOfMutants数组true代表位于这个索引的变异程序已经被覆盖
    boolean[] isMutantsCovered=new boolean[sumOfMutants];
    //......
    //这是一个贪心循环终止的条件，当所有的变异体都被覆盖，程序就会停止贪心循环
    boolean isAllMutantsCovered=false;
    //这是一个贪心循环终止的条件，当所有的字段覆盖当前还剩下的变异体数量全部为0（即字段无法杀死任何变异体），程序就会停止贪心循环
    boolean isAllFeildsZero=false;
    //......
    //贪心循环
    while(!isAllFeildsZero&&!isAllMutantsCovered){
        //更新isMutantsCovered数组，剔除已经被覆盖的变异体
        //更新fieldsKillNum数组，根据贪心策略，当某个变异体被覆盖时，检测出该变异体的字段所检测数的变异体数量应该减一（剔除已经被覆盖的变异体），之后才能在从中取最大值
        for(int j=0;j<sumOfMutants;j++){
            //......
            		//更新fieldsKillNum数组，来进行下一个贪心选择
                    for (int k = 0; k < sumOfFields; k++) {
                        //......
                    }
                }
            }
        }
        //检查是否所有变异体都被覆盖了
        //.....
        //检查是否所有字段能杀死的变异体数目都为0了
        //......
        //用于防止越界情况或最后一个覆盖变异体数目为0的字段进入结果序列的情况发生
        //......
    }
    //根据排序结果得到字段名称及预言的序列
    //......
}
```

## 四、MAODS模块构成

MAODS项目结构如下图：

![image-20211130072342313](.\asserts\image-20211130072342313.png)

总共有Runner、Mutator、ObjectorExcutor、OperationUI、Ranker、Testset、Util这七大模块。

### 1、Runner

该模块是MAODS整个工具的运行器，包含两个入口点（**第一个是控制 变异 和 生成指定类及其变异体执行代码 的入口点，第二个是控制 执行指定类及其变异体 和 生成测试预言数据集选择结果 的入口点，分成两个入口点是由于java程序运行时装载的类就已经确定导致，在类生成后需要重新运行程序并装载新生成的类才能继续下面的排序选择任务**）和一个MAODSRunner类，该类包括了UI界面各个按钮的监听器，执行UI端和后端的数据交换任务（相当于web项目的Controller）。

### 2、Mutator

该模块负责调用Mujava工具实现待测类的变异体生成，并将生成的变异体和原生类移到Testset包下，具体方法的解释详见源码注释。

### 3、ObjectorExcutor

该模块负责生成变异体和原生类的执行入口代码和运行这些生成的执行入口代码，得到运行结果（各个字段及其经过一系列运行后的值）并保存到result文件夹下。

### 4、OperationUI

该模块负责绘制与用户交互的UI，分别有两个入口点的UI。

### 5、Ranker

该模块负责核心算法背靠背测试和基于贪心策略的字段排序算法的实现，最后得到用户需要的已进行筛选的测试预言字段集结果。

### 6、Testset

该模块是原生类和变异体代码所在的模块，所有的变异体放到className+“M”文件夹下，所有原生类命名为className并放到该模块下。

### 7、Util

该模块是工具模块，其中包含文件操作工具类、CMD操作工具类、数组操作工具类，用于对大量重复代码的复用。

## 五、MAODS使用说明

MAODS使用步骤如下：

1. 根据LOG.md文档中对mujava的配置说明进行mujava配置，**一定不要忘记加环境变量**，其中四个jar包我已经下载好，只需配置环境变量即可
2. 保证待测类满足LOG.md中的四项要求：一定要有Entry()方法、待测类一般情况下不能依赖于其他类或其他包、待测类字段应当比较多、待测类的成员方法最好不要出现递归
3. 将仓库根目录下的MAODS_Test_Oracle_Selector项目导入IDEA或其他集成开发环境，**检查编码是否都为UTF-8**，若不是会造成生成程序编码错误
4. 运行入口点1EntryOfCodeGenerator.main，依次按要求输入（待测类名、java文件路径、class文件路径、junit单元测试文件路径），点击mutate，调用Mujava执行变异操作，然后点击generate，原生类和变异体执行入口代码生成后，程序结束运行
5. 运行入口点2EntryOfRanker.main，依次按要求输入（待测类名、指定的测试预言字段集的大小），并点击Rank按钮，在项目目录下的RankResult.txt可看到排序和选择的测试预言字段集结果。

## 六、写在最后

自动化测试课程的文献综述和工具复现任务确实给我带来了很多帮助，从前只知道查阅中文网站、CSDN寻找资料，现在发现阅读英文文献也得心应手了，还接触到了Stack Overflow这一优秀的问答平台，视野确实拓宽了很多。感谢自动化测试的各位老师、助教为自动化测试这门课的倾心付出！