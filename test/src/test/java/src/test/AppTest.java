

package src.test;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Unit test for simple App.
 */
public class AppTest

{

    @org.junit.Test
    public void TestCheckRawText(){
        ResultManager resultManager = new ResultManager();
        assertEquals(100,resultManager.CheckRawText("int a = 9;","int a = 9;"));

    }

    @org.junit.Test
    public void TestCheckName(){
        ResultManager resultManager = new ResultManager();
        assertEquals(100,resultManager.CheckName("int a = 9;","int b = 9;"));
    }

    @org.junit.Test
    public void TestCheckLoop(){
        ResultManager resultManager = new ResultManager();
        assertEquals(100,resultManager.CheckLoop("while","for"));
    }

    @org.junit.Test
    public void TestCheckCondition(){
        ResultManager resultManager = new ResultManager();
        assertEquals(100,resultManager.CheckCondition("if","switch"));
    }

    @org.junit.Test
    public void TestCheckComment(){
        ResultManager resultManager = new ResultManager();
        assertEquals(100,resultManager.CheckComment("//aa","//aa"));
    }
}
