package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;
import solPicker.filters.FailScoreFilter;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class FailScoreFilterTest extends TestCase 
{
	private FailScoreFilter fFilter;
	private ArrayList<Oligo> oligos;
	public FailScoreFilterTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		fFilter = new FailScoreFilter(15);
		ParseSequence sequence = new ParseSequence("AAAAAAAAAACTAGATTTTTTTTTTTTAACCCATAGGGGGTTAGTATCAAAAAATCCCATTA", 17);
		oligos = sequence.parse();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		oligos = null;
		fFilter = null;
	}
	
	public void testFailScoreFilter()
	{
		oligos = fFilter.filter(oligos);
		assertTrue(oligos.size() == 26);
		ArrayList<Oligo> rejected = fFilter.getRejectedOligos();
		assertTrue(rejected.size() == 20);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new FailScoreFilterTest("testFailScoreFilter"));
		return suite;
	}

}
