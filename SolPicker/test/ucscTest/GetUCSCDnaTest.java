/**
 * 
 */
package ucscTest;

import java.net.URL;
import java.util.HashMap;

import server.ucsc.GetUCSCDna;
import solPicker.job.Configuration;
import solPicker.job.GetRequest;
import solPicker.job.Query;
import solPicker.job.Range;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * @author Tyler Izatt
 *	Tests the GetDna Class which generates a GetDna url.  Tests if a correct url was created through testCreateUrl().  Then tests if
 *  isChromosomeValid catches invalid chromosome entries.
 */
@SuppressWarnings("unused")
public class GetUCSCDnaTest extends TestCase {
	
	
	public GetUCSCDnaTest(){}
	
	public GetUCSCDnaTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		test = new HashMap<String,String>();
		test.put("g=", g);
		test.put("db=",db);
		test.put("c=", c);
		test.put("l=", l);
		test.put("r=",r);
		test.put("hgSeq.padding5=",padding5);
		test.put("hgSeq.padding3=",padding3);
		test.put("hgSeq.casing=",casing);
		test.put("hgSeq.maskRepeats=",maskRepeats);
		test.put("boolshad.hgSeq.maskRepeats=",boolShadeRepeats);
		test.put("hgSeq.repMasking=",repMasking);
		test.put("boolshad.hgSeq.revComp=",revComp);
		Query q = new Query("12", l, r, id+"",length);
		gt = new GetUCSCDna(q);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	
	/**
	 * Tests if a url was generated
	 * @throws Exception 
	 */
	public void testCreateUrl() throws Exception{
		assertTrue(gt.getURL().toString().equals("http://genome.ucsc.edu/cgi-bin/hgc?&hgSeq.padding3=0&c=chr12&l=9999&boolshad.hgSeq.revComp=0&g=htcGetDNA" +
				"2&r=10050&boolshad.hgSeq.maskRepeats=1&hgSeq.casing=upper&hgSeq.padding5=0&hgSeq.maskRepeats=on&hgSeq.repMasking=lower&db=hg18"));
	}
	
	/**
	 * Tests if GetDna catches invalid chromosome entry 
	 * @throws Exception
	 */
	
	public void testIsChromosomeValid() throws Exception{
		
		String chr = "0";
		GetUCSCDna gd = new GetUCSCDna(chr,"0", "500");
		assertFalse(gd.isHumanChromosomeValid());
		
		gd = new GetUCSCDna("A","0","500");
		assertFalse(gd.isHumanChromosomeValid());
		
		gd = new GetUCSCDna("21A","0","500");
		assertFalse(gd.isHumanChromosomeValid());
		
		gd = new GetUCSCDna("A20","0","500");
		assertFalse(gd.isHumanChromosomeValid());
		
		gd = new GetUCSCDna("~5","0","500");
		assertFalse(gd.isHumanChromosomeValid());
		
		gd = new GetUCSCDna("","0","500");
		assertFalse(gd.isHumanChromosomeValid());
	}
	
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new GetUCSCDnaTest("testCreateUrl"));
		suite.addTest(new GetUCSCDnaTest("testIsChromosomeValid"));
		return suite;
	}
	
	HashMap<String,String> test;
	private GetRequest gr;
	private GetUCSCDna gt;
	private String c="20";
	private int length=0;
	private String padding3="";
	private String padding5="";
	private String organism="";
	private int minD=0;
	private int id=0;
	private Configuration cr;
	protected String query="";
	protected String g = "htcGetDNA2";
	protected String db="hg18";
	protected String maskRepeats="on";
	protected String boolShadeRepeats="1";
	protected String casing = "upper";
	protected String repMasking="lower";
	protected String revComp = "0";
	protected String l="10000";
	protected String r="10050";
//chr x, 16500, 18547
}
