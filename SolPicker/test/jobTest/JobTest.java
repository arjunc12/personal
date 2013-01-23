package jobTest;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;

import solPicker.job.Configuration;
import solPicker.job.Job;
import solPicker.job.Query;


public class JobTest extends TestCase
{
	private Job j;
	
	public JobTest(String name)
	{
		super(name);
	}
	
	@Before
	public void setUp() throws Exception 
	{
		Query q = new Query("X", "16500", "18547", "1", 25);
		Configuration c = new Configuration("1");
		j = new Job(c, q,1);
	}

	@After
	public void tearDown() throws Exception 
	{
		j = null;
	}
	
	public void testJob()
	{

		//j.performJob();
		String str = j.getQuerySequences().substring(0,2047);
		String fasta = "ATTGGGCCTAGGTCTCATTGAGGGCAGGTAGAGAGCCGACTGTACAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGGCAGATAGAGAGCAGACGGTGCAACCTTTAGAGTCTGCATTGGGCCT" +
				"AGGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGACTGCATTGAGCCTGAGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCATTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGGC" +
				"ACATAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCATTGGGTCTAGGGCCCACTGAGGGCAGACAGAGAGCAGACT" +
				"GTGCAAACTTTAGAGTCAGCATTGGGCCTAGGTCTCATTGAAGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGTCCGAATTGGGCCTCGGTCTCATTGAGGACCGATGGAGAGCAGACTGTGCAACCTTTAGAGT" +
				"CTGCATTGGGCCTGGGTCTCACTGAGGACAAATAGAGAGCAGACTGTGCAACCTTTAGGGACTGCATTGGACCTAGGTCTCATTGAGGACAGATAGAGAGCAGACTAGGCAACCTTTAGAGTCGGCACTGGGCCTAGG" +
				"TCTCATTGAGGACACATATAGAGCAGACTGTGCAACCTTTACAGTCTGCATTGGGCCTGGGTCTCATTGAGGACAGATAGAGACCAGACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGGCAGA" +
				"TAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCACTGGGCCTAGGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGAGCAGATAGAGAGCATACTGTG" +
				"CAACCTTTAGAGTCGGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGACCAGACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGAGCAGACTAGGCAACCATTAGAGTCGG" +
				"CACTGGTCCTAGGTCTCATTGAGGACAGATATAGAGCAGACTGTGCAACCTTTAGAGTCTGCATTGTGCCTGGGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCACTGGGCCTAGGTCT" +
				"CATTGAGGACAGATAGAGAGCAGACTGTGCAACCTCTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGGCATTTAGAGAGCAGACTGCGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGAGCAGATAG" +
				"AGAGCACACTGTGCAACCACTAGAGTCGGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGACCAGACTGTGCAAACTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGGGCAGACTGTGCAA" +
				"CCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGTGGACCGATACAGAGCAGACTGTGCAACCTTTAGAGTCTGCACTGGGCCTAGGTCTCATTGAGGGCAGATAGAGAGCACACTGTGTAACCTTTAGAGTCTGCAT" +
				"AGGGCCTCGGTCTCATTGAGGACCGATAGAGAGCAGACTGTGCAATCTTTAGAGTCTGCATTGGGCCTGGGTCTCATTGAAGACAGATAGAGACCAGACTGTGCAACCTTTAGAGTTTGCATTGGACCTAGGTCTCAT" +
				"TGAGGGCAGATAGAGAGCAAACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCATTGAGGACAGATAGAGAGCAGACTGTGCAACCTTTAGAGTCTGCACTGGGCCTAGGTCTCTTTGAGGACAGACAGAGA" +
				"GCAGACTGTGCAAACTTTAGAGTCTGCACTGGGCCTAGGTGTCATTGAGGACAGAAAGAGACCAGACTGTGCAACCTTTAGAGTCTGCATTGGGCCTAGGTCTCCTTGAGGTCAGATAGACA";
		assertTrue(str.equals(fasta));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new JobTest("testJob"));
		return suite;
	}
}