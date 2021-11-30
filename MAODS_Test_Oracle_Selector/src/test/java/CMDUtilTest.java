import Util.CMDUtil;
import org.junit.Test;

public class CMDUtilTest {
    @Test
    public void excuteBatFileTest(){
        CMDUtil.excuteBatFile("..\\mujava\\mujavaHome\\GenMutants.cmd",false);
    }
}
