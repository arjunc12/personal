package filtersTest;

import wswu.blast.WuBlast;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class WuBlastTest extends TestCase {

	public WuBlastTest(String name){
		super(name);
	}
	
	
	public void test1(){
		WuBlast wb = new WuBlast();
		System.out.println(8/7);
		System.out.println(4/5);
		wb.setDefaultInputParameters();
		wb.blast();
	}
	
	
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new WuBlastTest("test1"));
		return suite;
	}
}
