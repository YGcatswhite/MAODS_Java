package Util;
/**
 * 对数组操作的工具类，用于代码复用，该类只有一个选择最大值方法
 * @author 姬筠刚 191250055
 */
public class ArrayUtil {
    /**
     * 选择数组中的最大值
     * @param array 操作数组
     * @return 最大值所在的索引
     */
    public static int selectMax(int[] array){
        int nowMax=array[0];
        int indexOfMax=0;
        for(int i=1;i<array.length;i++){
            if(array[i]>=nowMax){
                nowMax=array[i];
                indexOfMax=i;
            }
        }
        return indexOfMax;
    }
}
