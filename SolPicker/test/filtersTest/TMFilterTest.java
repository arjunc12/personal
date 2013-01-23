package filtersTest;

import java.util.ArrayList;

import solPicker.job.Oligo;
import solPicker.filters.TMFilter;


import junit.framework.TestCase;

public class TMFilterTest extends TestCase 
{
	private TMFilter f1;
	private TMFilter f2;
	private TMFilter f3;
	private ArrayList<Oligo> oligos1;
	private ArrayList<Oligo> oligos2;
	private ArrayList<Oligo> oligos3;
	
	public TMFilterTest(String name) 
	{
		super(name);
	}

	protected void setUp() throws Exception 
	{
		super.setUp();
		String[] arr = {"ACTGATAAACAT", "ACCGATTAGGACA", "TCCCGACCGGGG", "GCCGGCTGGCGGCCGC", "ACTTA", "ACCTT", "CGGCC"};
		//19.12, 32.1, 43.03, 63.875, -61.38, -44.98, -25.58
		f1 = new TMFilter(-100, 0);
		f2 = new TMFilter(-35, 35);
		f3 = new TMFilter(21, 100);
		oligos1 = new ArrayList<Oligo>();
		oligos2 = new ArrayList<Oligo>();
		oligos3 = new ArrayList<Oligo>();
		for(int i = 0; i < arr.length; i++)
		{
			Oligo o = new Oligo(arr[i]);
			oligos1.add(o);
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
		oligos1 = null;
		oligos2 = null;
		oligos3 = null;
	}

	public void testTMFilter()
	{
		assertTrue(oligos1.size() == 7);
		assertTrue(oligos2.size() == 7);
		assertTrue(oligos3.size() == 7);
		oligos1 = f1.filter(oligos1);
		assertTrue(oligos1.size() == 3);
		assertTrue(oligos1.toString().equals("[ACTTA, ACCTT, CGGCC]"));
		oligos2 = f2.filter(oligos2);
		assertTrue(oligos2.size() == 3);
		assertTrue(oligos2.toString().equals("[ACTGATAAACAT, ACCGATTAGGACA, CGGCC]"));
		oligos3 = f3.filter(oligos3);
		assertTrue(oligos3.size() == 3);
		assertTrue(oligos3.toString().equals("[ACCGATTAGGACA, TCCCGACCGGGG, GCCGGCTGGCGGCCGC]"));
	}
}
