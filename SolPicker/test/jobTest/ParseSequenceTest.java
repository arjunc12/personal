package jobTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ParseSequenceTest extends TestCase {

	private ParseSequence p1;
	private ParseSequence p2;
	private ArrayList<Oligo> o1;
	private ArrayList<Oligo> o2;
	public ParseSequenceTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		String sequence = "ACTGATACATAG";
		p1 = new ParseSequence(sequence, 5);//ACTGA CTGAT TGATA GATAC ATACA TACAT ACATA CATAG
		p2 = new ParseSequence(sequence, 4, 3);//ACTG GATA ACAT
		o1 = p1.parse();
		o2 = p2.parse();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		p1 = null;
		p2 = null;
		o1 = null;
		o2 = null;
	}
	
	public void testParseSequence()
	{
		assertTrue(o1.size() == 8);
		assertTrue(o2.size() == 3);
		assertTrue(o1.toString().equals("[ACTGA, CTGAT, TGATA, GATAC, ATACA, TACAT, ACATA, CATAG]"));
		assertTrue(o2.toString().equals("[ACTG, GATA, ACAT]"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new ParseSequenceTest("testParseSequence"));
		return suite;
	}

}
