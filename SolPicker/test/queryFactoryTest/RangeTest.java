/**
 * 
 */
package queryFactoryTest;

import solPicker.job.Query;
import solPicker.job.Range;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @deprecated
 * @author Tyler Izatt
 *	Tests the ChromosomeQuery Class.
 */
public class RangeTest extends TestCase {

	
	public Range cq;
	/**
	 * @param name - name of the test
	 */
	public RangeTest(String name) {
		super(name);
	}
	
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		Query q = new Query("X", "334446", "345836", "2", 6);
		cq = new Range(q);
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
		cq = null;
	}
	
	
	/**
	 * Tests the ChromosomeQuery Class methods
	 */
	public void testRange(){
		assertTrue(cq.size() == 1);
		assertTrue(cq.getStartBase().equals("334446"));
		assertTrue(cq.getEndBase().equals("345836"));	
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new RangeTest("testRange"));
		return suite;
	}
	//Chromosome Query object
}
