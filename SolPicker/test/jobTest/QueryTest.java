package jobTest;

import solPicker.job.Query;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class QueryTest extends TestCase 
{
	private Query q1;
	private Query q2;
	public QueryTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		q1 = new Query("X", "16500", "18547", "1", 25);
		q2 = new Query("Y", "600", "1300", "2", 34);
		q2.set3Padding(5);
		q2.set5Padding(3);
		q2.setDomain("server.ensembl");
		q2.setMinDistance(3);
		q2.setOrganism("Canis_lupis");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		q1 = null;
		q2 = null;
	}
	
	public void testQuery()
	{
		assertTrue(q1.getChromosomeIdentifier().equals("X"));
		assertTrue(q1.getStart().equals("16500"));
		assertTrue(q1.getEnd().equals("18547"));
		assertTrue(q1.getID().equals("1"));
		assertTrue(q1.getOligoLength() == 25);
		assertTrue(q1.get3Padding().equals("0"));
		assertTrue(q1.get5Padding().equals("0"));
		assertTrue(q1.getDomain().equals("server.ucsc"));
		assertTrue(q1.getOrganism().equals("Homo_sapiens"));
		assertTrue(q1.getMinDistance() == 25);
		
		assertTrue(q2.getChromosomeIdentifier().equals("Y"));
		assertTrue(q2.getStart().equals("600"));
		assertTrue(q2.getEnd().equals("1300"));
		assertTrue(q2.getID().equals("2"));
		assertTrue(q2.getOligoLength() == 34);
		assertTrue(q2.get3Padding().equals("5"));
		assertTrue(q2.get5Padding().equals("3"));
		assertTrue(q2.getDomain().equals("server.ensembl"));
		assertTrue(q2.getOrganism().equals("Canis_lupis"));
		assertTrue(q2.getMinDistance() == 3);
		
		assertFalse(q1.equals(q2));
		assertTrue(q1.compareTo(q2) == -1);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new QueryTest("testQuery"));
		return suite;
	}

}
