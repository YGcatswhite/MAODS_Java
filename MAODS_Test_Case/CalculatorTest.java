import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
//随意写一个单元测试，否则Mutest运行会出错
public class CalculatorTest {
    private Calculator c;
    @Before
    public void setUp() throws Exception{
        c=new Calculator();
    }
    @After
    public void tearDown() throws Exception{
        c=null;
    }
    @Test
    public void testAdd(){
        assertEquals(2.0,c.add(1.0,1.0),0.000f);
    }
}
