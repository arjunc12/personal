package filtersTest;


import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import solPicker.job.Oligo;
import solPicker.job.ParseSequence;
import solPicker.filters.GCFilter;



public class GCFilterTest extends TestCase
{
	private GCFilter gcFilter;
	private ArrayList<Oligo> oligos;
	
	public GCFilterTest(String name)
	{
		super(name);
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception 
	{
		super.setUp();
		gcFilter = new GCFilter(30, 50);
		oligos = new ParseSequence("ACACATTAGACAGAAGC", 5).parse();//ACACA 40 CACAT 40 \ACATT 20 \CATTA 20 \ATTAG 20 \TTAGA 20 TAGAC 40 AGACA 40 
		//\GACAG 60 ACAGA 40 CAGAA 40 AGAAG 40 \GAAGC 60
	}

	@After
	public void tearDown() throws Exception 
	{
		super.tearDown();
		gcFilter = null;
		oligos = null;
	}
	
	public void testGCFilter()
	{
		assertTrue(oligos.size() == 13);
		oligos = gcFilter.filter(oligos);
		assertTrue(oligos.size() == 7);
		assertTrue(oligos.toString().equals("[ACACA, CACAT, TAGAC, AGACA, ACAGA, CAGAA, AGAAG]"));
		assertTrue(gcFilter.getRejectedOligos().size() == 6);
		assertTrue(gcFilter.getRejectedOligos().toString().equals("[ACATT, CATTA, ATTAG, TTAGA, GACAG, GAAGC]"));
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new GCFilterTest("testGCFilter"));
		return suite;
	}
}
