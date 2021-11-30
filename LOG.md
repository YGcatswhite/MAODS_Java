# MAODS开发日志

## MAODS开发简介

MAODS_Java是根据论文《[ICSE'12]Automated oracle creation support_How I learned》和《[ASE'16]Supporting_oracle_construction_via_static_analysis》描述复现的一项测试预言数据选择工具，它的测试粒度为类级。它首先需要使用变异进行故障生成（即生成变异体），通过运行原生类和变异体对每个字段的值进行比对，然后通过基于贪心策略的算法得到每个字段的故障检测率（变异杀死率）并排序，从中选择一部分字段作为测试预言数据集。MAODS选择进行测试预言的字段尽可能少，同时保证故障检测率尽可能最大化，来降低测试成本。

## MAODS与类似工具相比的突出创新点、特点及原理

由于MAODS的概念是2012年产生的，我们讨论的创新点基于与2012年之前的类似工具相比，突出特点和原理基于与2012年以后的即SODS相比。

#### **创新点：**相较于2012年及以前的相关工具

①MAODS应用了突变测试中的突变方法来进行缺陷模拟，选择变异杀死率高的字段进入测试预言字段选择序列

②MAODS使用变量优先级排序对测试预言字段集合进行选择，而之前的相关工作大多是用于自动生成用于回归测试的基于不变量的测试预言，并不会量化不变量的潜在有效性

#### 特点及原理：相较于SODS

MAODS是使用动态分析的方法对测试预言字段集合进行选择，使用自动化背靠背测试的方法，程序运行需要额外开销，而SODS使用静态分析的方法，开销较小但实现比较复杂，它依据被测程序的 [定义-使用] 链来定义和构造概率替换图，在评估中，SODS也被证实检测率没有很大差异的情况下在效率方面远远优于MAODS。

## MAODS环境配置

MAODS没有复杂的配置，只需要使用者了解开发环境、Mujava配置、待测类配置要求。

### 开发环境

- JDK：1.8
- junit：4.12及以上
- µJava(Mujava)：Version 4(官网链接：https://cs.gmu.edu/~offutt/mujava/)
- ❗ ❗ ❗ IDEA编码配置要注意：全局编码和项目编码都为UTF-8，否则在UI创建和文件读写时会出现乱码

### Mujava配置

Mujava是本项目用到的变异工具，使用它可以通过插入新的运算符或者[使用不同的运算符或者变量替换运算符]或者是[替换具有不同变量或运算符的变量]，这里加"[]"的原因是防止分析句子成分时把或者分析错了，正好契合论文中对变异方法的要求

官方文档讲得不太明白，博客链接如下（也可以继续阅读我的配置方法），[参考博客]：https://blog.csdn.net/wkw1125/article/details/51967630

配置步骤如下：

1. 去官网下载mujava.jar、openjava.jar、mujava.config、junit.jar（版本自选，但到了某个版本以上还需要一个hamcrast.jar包），新建一个mujava文件夹，将以上四个jar包放到里面

2. 配置环境变量CLASSPATH，即将mujava里的jar包路径添加到CLASSPATH中，例如我的CLASSPATH环境变量为`.;%JAVA_HOME%\lib\dt.jar;%JAVA_HOME%\lib\tools.jar;C:\Users\jiyun\Desktop\MAODS_Java\mujava\mujava.jar;C:\Users\jiyun\Desktop\MAODS_Java\mujava\hamcrest-core-1.1.jar;C:\Users\jiyun\Desktop\MAODS_Java\mujava\openjava.jar;C:\Users\jiyun\Desktop\MAODS_Java\mujava\junit-4.12.jar`

3. 在mujava文件夹里再创建一个mujavaHome文件夹，在官网下载mujava.config放到mujavaHome下，并用记事本打开将MuJava_HOME设置为该文件夹，然后在mujavaHome文件夹下分别建立classes、result、src、testset四个文件夹。

   | 文件夹  | 说明                                                         |
   | ------- | ------------------------------------------------------------ |
   | src     | 待测Java程序源代码                                           |
   | classes | src中源代码经过正确编译的.class文件                          |
   | testset | JUnit测试用例（在MAODS项目中，这个用不到，但环境需要仍要提供） |
   | result  | 生成的变体结果                                               |

4. 在mujavaHome下创建两个批处理文件（扩展名为.bat或.cmd），用户存放DOS命令，文件名和内容分别为

   GenMutants.cmd: 

   ```bash
   cd C:\Users\jiyun\Desktop\MAODS_Java\mujava\mujavaHome ****这里根据自己mujavaHome的所在目录进行修改
   java mujava.gui.GenMutantsMain
   ```

    RunTest.cmd: 

   ```bash
   java mujava.gui.RunTestMain > TestResult.txt
   ```

5. 运行GenMutants.cmd即可，若不能正常运行从配置第一步检查哪里配错了，运行结果如图：

   ![image-20211130042337801](.\asserts\image-20211130042337801.png)

### 待测类配置要求

对待测类格式有以下几点要求：

- **一定要有Entry()方法，没有返回值，没有参数！**Entry方法相当于一个测试方法，通过外部测试输入执行本类的所有方法，MAODS是动态分析工具，且在运行过程中需要访问各个字段的值，使用Junit不方便使用java的反射机制，所以要在类内部设置Entry()方法供MAODS调用来运行原生类和变异体，得到Entry方法运行后的类内各字段的值并记录来获取每个字段的故障检测效率。
- **待测类一般情况下不能依赖于其他类或其他包（包括lombok注解），这是由MAODS测试粒度（类级）和Mujava工具共同限制的。**一般情况下，Mujava在对依赖其他包的待测类无法生成相应的变异体，实践出真知（确信）。
- 待测类字段应当比较多，这不是硬性要求，不这样的话没有测试预言字段选择的必要。
- 待测类的成员方法最好不要出现递归，例如阶乘，不然在程序变异以后会出现爆堆的现象，因为你无法预料变异程序递归的复杂性，这是从我自己实现的待测类Calculator得到的教训，之前实现了阶乘方法，结果动态执行时运行不动了。

## MAODS开发时间线及具体事务

### 2021.11.8

阅读《[ICSE'12]Automated oracle creation support_How I learned》即MAODS提出的论文，并理解实现步骤，翻译重要内容：

论文中表述的MAODS基本架构如下：

![image-20211130044845903](.\asserts\image-20211130044845903.png)

```markdown
测试预言是决定一个应用在测试执行时是否正确的人工制品，测试预言的选择影响了测试进程的效率。然而尽管测试输入选择的工作非常流行，但是测试预言有关的工作却非常少。这项工具是支持测试预言生成的方法。这种方法自动的选择了为特定值的测试预言预言数据（用来监视测试过程的变量集合）。基于变异分析的方法，根据一场寻找效率对变量进行排序。对四个工业例子进行了测试，证明了该工具是高效率的方法去生产小的、高效的预言数据，

测试预言的选择对测试过程的有效性具有显著影响，目前普遍的做法只是为输出定义预期值，这种做法不是最理想的。而且，手动定义期望值是潜在的耗时且昂贵的过程。因此，在实践中这种看似“监控一切”的幼稚解决方案是不可行的。

我们的目标是通过选择测试数据来支持测试预言的创建，以便在成本方面最大化测试过程的故障查找潜力。方法如下：首先从被测系统中生成变异体集合；然后使用原始系统作为预言（使用的是自动化的背靠背测试，即对某个部件的两个版本进行相同的输入）针对变异体运行测试套件；第三我们测量系统中的每个变量检测出变异体故障的概率，并基于这些信息根据故障查找对变量有效性进行排序。最后我们根据这个排名评估呢些变量应该被包含在测试数据里为一些期望值数据预言。潜在的假设是，与基于变异的测试数据选择一样，可能揭示变异体中的错误的预言数据也可能揭示被测系统中的错误。一旦测试数据被选择后，测试人员为预言数据的每个元素定义预期值。然后测试就可以从这个小型且高效的预言开始了。

使用了民用航空电子领域的四个商业子系统进行了评估

为输出定义预言是只输出预言（output-only oracle），在文章其余部分将会提到预言的大小，大小是指在预言数据集中用到的变量数量，最大预言集合虽然始终是最有效的预期值测试预言，但是代价非常昂贵。故障传播造成较大的预言数据集通常比较小的预言数据集更强大，导致错误状态的故障并不总是传播表现为预言数据集中变量的故障

两个人（不知道是谁）建议说：PIE方法依赖于某种形式的变异分析，可用于选择内部变量进行检测。有这样一种面向对象的系统，证实期望值预言优于基于状态的不变量，前者能检测到后者遗漏的故障。这些过去的方法不能执行优先级排序

我们工作的不同之处在于我们试图支持测试预言的创建而不是完全自动化。生成的预言的域和类型也不同，我们评估中测试输入的性质也不同。

变异分析最初作为一种评估测试输入选择方法有效性的方法引入，变异测试相关的技术创新也在不断更新。

## 预言数据选择

选择预言数据集的方法是基于变异测试来选择测试输入。在变异测试中通过将各种故障植入系统中来创建大量程序，成为变异体，能够将变异体与原始程序区分开的测试输入被称为杀死了变异体。

执行以下几个步骤：

1. 从测试系统中生成几个突变体，称为训练集。
2. 在训练集和原始系统上运行测试人员提供的测试输入，确定哪些变量将每个突变体与原始系统区分开来
3. 处理此信息以创建根据明显的故障查找有效性排序的变量列表进行变量排名
4. 检验此排名以及变异体和测试输入，来估计语言数据集应该有多达
5. 选择排名中前X个变量作为预言数据

下面是详细的阐述：

### 突变体生成和测试输入源

突变体产生每个故障都是通过在系统中插入新的运算符或者使用不同的运算符或者变量替换运算符（或者是替换具有不同变量或运算符的变量，or太多了有点分析不出英语句子成分了orz），这些变异的设计在句法和语义上都是有效的（不会使被测系统崩溃）

### 变量排名

产生突变体后我们可以在突变体和原始程序上运行测试输入。在这些输入执行过程中，我们在每个测试的每个步骤，即执行的每个点的完整状态，收集每个变量的值。我们将此结果数据称为跟踪数据。当在某些测试中变异程序的变量的原始值与正确系统不同时，则称该变量检测到了错误。

得到了这些信息以后，我们可以生成一组根据有效性排名的变量。但是单个变量的有效性可能是高度相关的，比如当使用某个变量A计算变量B时，如果A对于某些测试输入不正确则B也会不正确，两者结合可能比单独使用一个更有效，意思就是两个等价就取出其中一个。

因此为了避免选择一组单独有效但组合在一起无效的变量，使用贪婪算法来解决集合覆盖问题以生成一组排序的变量。选择覆盖最多集合的元素，从考虑中删除被多数元素覆盖的所有集合，重复以上过程直到所有集合都被覆盖（每个集合代表一个突变体，该集合的每个元素是变量），按照被移除的顺序进行排序

### 估计有用的预言数据大小

一旦我们计算出变量的排序列表，我们就可以选择大小为1、2等的预言数据集，指导系统中的变量的最大数量，大小的选择可能根据一些测试预算进行。在某些情况下，测试人员可能对语言数据的适当大小几乎没有指导，我们应当给测试人员提供建议，希望选择一个预言数据集，是的该数据集的大小是合理的，既不会小到忽略潜在有用的变量，也不会大到选择大量没有增加价值的变量。为了实现这一点，该工具确定了大小为1、2、3等的语言数据集在变异训练集上故障查找的有效性，这些预言变量集大小增加会伴随着故障查找效率的增加，但增加速度会减缓，规定一个阈值即可得到大小为n的预言数据集。

## 评估

在2012年及之前预言数据选择方法都不存在，所以作者是和两个潜在的语言数据集选择基线方法进行比较，有效性、测试输入数据的选择对该方法的影响

测试套件生成（测试输入生成）：这项不属于该工具的子系统，但作者是使用两个结构覆盖标准（分支覆盖和MC/DC覆盖），使用基于反例测试生成方法（虽然我不知道是什么==，但这不重要），使用简单的随机贪婪算法对抗冗余。

变异生成：就根据之前所述的方法进行变异生成，使用NuSMV模型删除功能等效的变异体。

预言数据集生成：就按照排序生成

数据采集：预言数据集的故障查找率有效性计算为被杀死的变异体占评估集的百分比（评估集的变异体也是新生成的吧）

后面就不手动翻译了，看了一遍没啥新思路。
```

### 2021.11.9

划水，有其他作业

### 2021.11.10

阅读《[ASE'16]Supporting_oracle_construction_via_static_analysis》和《[TSE'14]The_Oracle_Problem_in_Software-A_Survey》，其中前者将MAODS和SODS（静态分析进行测试预言选择）和DODONA（动态分析进行测试预言选择）进行了对比，后者是有关测试预言相关研究的综述，对MAODS复现没有实质性帮助==。

### 2021.11.10-2021.11.17

划水，有其他作业

### 2021.11.18

完成UI构建，另外完成了使用Java运行批处理文件（Util.CMDUtil类和Mutantor.MutantGenerator类），来实现Mujava的嵌入运行。

### 2021.11.19

构建执行入口点代码生成模块，即ObjectExecutor软件包的代码生成部分，生成方法比较暴力：基本就是相同框架的代码，根据不同的执行入口类（比如原生类和不同序号的变异体）类名包名和索引值有所不同，生成方法详见ObjectExecutor.EntryCodeGenerator类

随后编写了一个简单的待测类Calculator，包含加减乘除四个方法，用于模块的持续集成

### 2021.11.20

继续完善ObjectExecutor软件包，但遇到了一个非常棘手的问题：由于本项目需要程序生成来得到不同变异体和原生类的执行入口，但是java程序在运行前已经完成了类装载，因此无法运行生成的类代码，暂时没解决==

### 2021.11.21

stackover搜集昨天的解决方案，做其他作业，划水

### 2021.11.22

可以使用两个程序入口点来解决前天遇到的问题，即变异体及执行入口点生成模块入口和排序及生成结果入口，第一个入口执行完以后，所有生成类就可以被编入索引然后加载，通过第二个入口点进行调用。今天完成了第二个入口点的UI构建和监听方法编写，并完成了ObjectExecutor软件包的执行生成代码功能

### 2021.11.23

今天完成了Ranker（排序器）的构思（这部分是核心算法代码），并书写了框架代码，使用策略模式来应对策略扩展

### 2021.11.24

整合之前的模块并编写了基于贪心策略的排序算法的具体实现，然后进行了测试+修改，细节太多了，越界和索引错误经常出现，具体实现贴在下面：

```java
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
```

### 2021.11.25-2021.11.28

其他ddl太多了，划水

### 2021.11.29

完善待测类Calculator，增加了取模、字符串合并、倒数、~~阶乘~~（有递归，变异很可能爆堆）、平方、三次方、加减、加乘、加除、乘减、乘除、除减这些方法，字段（成员变量）增加到24项，完成对整个项目的有效性评估，开发结束

