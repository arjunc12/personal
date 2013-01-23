package queryFactoryTest;



import solPicker.job.Query;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * @deprecated
 * @author Tyler Izatt
 */
public class QueryTest extends TestCase 
{
	private Query cr;

	public QueryTest(String name) 
	{
		super(name);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		cr = new Query("X", "334446", "345836","2",6);
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
		cr = null;
	}
	
	public void testQuery()
	{
		String cNumber = cr.getChromosomeIdentifier();
		String start = cr.getStart();
		String end = cr.getEnd();
		String organism = cr.getOrganism();
		
		assertTrue(cNumber.equals("X"));
		assertFalse(cr.equals("4"));
		assertTrue(start.equals("334446"));
		assertFalse(start.equals("976"));
		assertTrue(end.equals("345836"));
		assertFalse(end.equals("83"));
		assertTrue(cr.get3Padding().equals("0"));
		assertTrue(cr.get5Padding().equals("0"));
		assertFalse(cr.get3Padding().equals("6"));
		assertFalse(cr.get5Padding().equals("6"));
		assertTrue(organism.equals("Homo_sapiens"));
		assertFalse(organism.equals("Canis_lupis"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new QueryTest("testQuery"));
		return suite;
	}

}
