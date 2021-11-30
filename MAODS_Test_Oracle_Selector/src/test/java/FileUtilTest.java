import Util.FileUtil;
import org.junit.Test;

import java.io.IOException;

public class FileUtilTest {
    @Test
    public void copyDirTest(){
        try {
            FileUtil.copyDir("C:\\Users\\jiyun\\Desktop\\MAODS_Java\\mujava\\mujavaHome\\result","C:\\Users\\jiyun\\Desktop\\MAODS_Java\\MAODS_Test_Oracle_Selector\\MutantsGenDir");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
