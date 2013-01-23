package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;
import solPicker.filters.LowerCaseFilter;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class LowerCaseFilterTest extends TestCase 
{
	private LowerCaseFilter lFilter;
	private ArrayList<Oligo> oligos;
	
	public LowerCaseFilterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		lFilter = new LowerCaseFilter(4);
		oligos = new ParseSequence("ACCAtaataTAccatAggaTA", 7).parse();//[ACCAtaa, CCAtaat, \CAtaata, \AtaataT, \taataTA, \aataTAc, \ataTAcc, \taTAcca, 
		//\aTAccat, 
		//TAccatA, \AccatAg, \ccatAgg, \catAgga, \atAggaT, tAggaTA]
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		lFilter = null;
		oligos = null;
	}
	
	public void testLowerCaseFilter()
	{
		assertTrue(oligos.size() == 15);
		oligos = lFilter.filter(oligos);
		assertTrue(oligos.size() == 4);
		assertTrue(oligos.toString().equals("[ACCAtaa, CCAtaat, TAccatA, tAggaTA]"));
		assertTrue(lFilter.getRejectedOligos().size() == 11);
		assertTrue(lFilter.getRejectedOligos().toString().equals("[CAtaata, AtaataT, taataTA, aataTAc, ataTAcc, taTAcca, aTAccat, AccatAg, ccatAgg, " +
				"catAgga, atAggaT]"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new LowerCaseFilterTest("testLowerCaseFilter"));
		return suite;
	}

}
