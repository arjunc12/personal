package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;
import solPicker.filters.NCountFilter;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class NCountFilterTest extends TestCase 
{
	private NCountFilter nFilter;
	private ArrayList<Oligo> oligos;

	public NCountFilterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		nFilter = new NCountFilter(3);
		oligos = new ParseSequence("ACCCANNNATNNANNN", 6).parse();//[ACCCAN, CCCANN, CCANNN, CANNNA, ANNNAT, \NNNATN, \NNATNN, NATNNA, ATNNAN, \TNNANN, 
		//\NNANNN]
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		nFilter = null;
		oligos = null;
	}
	
	public void testNFilter()
	{
		assertTrue(oligos.size() == 11);
		oligos = nFilter.filter(oligos);
		assertTrue(oligos.size() == 7);
		assertTrue(oligos.toString().equals("[ACCCAN, CCCANN, CCANNN, CANNNA, ANNNAT, NATNNA, ATNNAN]"));
		assertTrue(nFilter.getRejectedOligos().size() == 4);
		assertTrue(nFilter.getRejectedOligos().toString().equals("[NNNATN, NNATNN, TNNANN, NNANNN]"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new NCountFilterTest("testNFilter"));
		return suite;
	}

}
