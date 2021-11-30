public class Calculator {
    //类内的各种字段（也是MAODS操作的对象），访问权限可以设为private，也可以设为public，这里是为了方便
    double op1Double=0;
    double op2Double=0;
    int op1Int=0;
    int op2Int=0;

    //以下三个字段必然不可以被选择，因为根本没用在任何方法上
    int distractor1=0;
    double distractor2=0;
    String distractor3="";

    String op1String="";
    String op2String="";
    double addResult=0;
    double mulResult=0;
    double divResult=0;
    double subResult=0;
    int modResult=0;
    double squareResult=0;
    double reciprocalResult=0;
    int factorialResult=1;
    double cubicResult=0;

    //一下几个字段均被多个方法使用，因此变异杀死率会高一些，排名靠前
    double addandmulResult=0;
    double addanddivResult=0;
    double addandsubResult=0;
    double mulanddivResult=0;
    double mulandsubResult=0;
    double divandsubResult=0;

    //各种需要被变异的方法，时间关系，实现都很简单,不影响最后的结果检测
    public String append(int num,String append){
        if(num==1){
            op1String=op1String+append;
            return op1String;
        }
        if(num==2){
            op2String=op2String+append;
            return op2String;
        }
        else
            return "";
    }
    public double add(double a,double b){
        addResult= a+b;
        return addResult;
    }
    public double sub(double a,double b){
        subResult=a-b;
        return subResult;
    }
    public double mul(double a,double b){
        mulResult=a*b;
        return mulResult;
    }
    public double div(double a,double b){
        if(b==0){
            divResult=0;
            return 0;
        }
        divResult=a/b;
        return a/b;
    }
    public int mod(int a,int b){
        if(b==0){
            modResult=0;
            return 0;
        }
        modResult=a%b;
        return modResult;
    }
    public double square(double a){
        squareResult=a*a;
        return squareResult;
    }
    public double reciprocal(double a){
        reciprocalResult=1/a;
        return reciprocalResult;
    }
    public double cubic(double a){
        cubicResult=a*a*a;
        return cubicResult;
    }
    public double addandmul(double a,double b){
        addandmulResult=a+b;
        addandmulResult=addandmulResult*a*b;
        return addandmulResult;
    }
    public double addanddiv(double a,double b){
        addanddivResult=a+b;
        addanddivResult=addanddivResult/a/b;
        return addanddivResult;
    }
    public double addandsub(double a,double b){
        addandsubResult=a+b;
        addandsubResult=addandsubResult-a-b;
        return addandsubResult;
    }
    public double mulanddiv(double a,double b){
        mulanddivResult=a*b;
        mulanddivResult=mulanddivResult/a/b;
        return mulanddivResult;
    }
    public double mulandsub(double a,double b){
        mulandsubResult=a*b;
        mulandsubResult=mulandsubResult-a-b;
        return mulandsubResult;
    }
    public double divandsub(double a,double b){
        divandsubResult=a/b;
        divandsubResult=divandsubResult-a-b;
        return divandsubResult;
    }

    //每个使用MAODS都需要有的方法，这个方法相当于提供了测试输入和测试程序
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
        mul(op1Double,op2Double);
        div(op1Double,op2Double);
        add(op1Int,op2Int);
        sub(op1Int,op2Int);
        mul(op1Int,op2Int);
        div(op1Int,op2Int);
        mod(op1Int,op2Int);
        square(op1Double);
        square(op2Double);
        square(op1Int);
        reciprocal(op1Double);
        reciprocal(op2Double);
        reciprocal(op1Int);
        cubic(op2Int);
        addandmul(op1Double,op2Double);
        addandsub(op1Double,op2Double);
        addanddiv(op1Double,op2Double);
        mulanddiv(op1Double,op2Double);
        mulandsub(op1Double,op2Double);
        divandsub(op1Double,op2Double);
    }
}
