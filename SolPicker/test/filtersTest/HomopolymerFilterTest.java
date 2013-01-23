package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.filters.HomoPolymerFilter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HomopolymerFilterTest extends TestCase 
{
	private HomoPolymerFilter f1;
	private HomoPolymerFilter f2;
	private HomoPolymerFilter f3;
	private ArrayList<Oligo> oligos;
	private ArrayList<Oligo> oligos2;
	private ArrayList<Oligo> oligos3;

	public HomopolymerFilterTest(String name) 
	{
		super(name);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		f1 = new HomoPolymerFilter(0);
		f2 = new HomoPolymerFilter(5);
		f3 = new HomoPolymerFilter(20);
		String[] arr = {"ACTGATGA", "AAAAAGT", "TGGGGGC", "CCCCCCCCC", "TTTTTTTGA"};
		oligos = new ArrayList<Oligo>();
		oligos2 = new ArrayList<Oligo>();
		oligos3 = new ArrayList<Oligo>();
		for(int i = 0; i < arr.length; i++)
		{
			Oligo o = new Oligo(arr[i]);
			oligos.add(o);
			oligos2.add(o);
			oligos3.add(o);
		}
	}

	protected void tearDown() throws Exception 
	{
		super.tearDown();
		f1 = null;
		f2 = null;
		f3 = null;
		oligos = null;
		oligos2 = null;
		oligos3 = null;
	}

	public void testHomopolymerFilter()
	{
		assertTrue(oligos.size() == 5);
		assertTrue(oligos2.size() == 5);
		assertTrue(oligos3.size() == 5);
		oligos = f1.filter(oligos);
		assertTrue(oligos.size() == 1);
		assertTrue(oligos.toString().equals("[ACTGATGA]"));
		oligos2 = f2.filter(oligos2);
		assertTrue(oligos2.size() == 3);
		assertTrue(oligos2.toString().equals("[ACTGATGA, AAAAAGT, TGGGGGC]"));
		oligos3 = f3.filter(oligos3);
		assertTrue(oligos3.size() == 5);
		assertTrue(oligos3.toString().equals("[ACTGATGA, AAAAAGT, TGGGGGC, CCCCCCCCC, TTTTTTTGA]"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new HomopolymerFilterTest("testHomopolymerFilter"));
		return suite;
	}
}
