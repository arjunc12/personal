package jobTest;

import solPicker.job.Oligo;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class OligoTest extends TestCase {
	private Oligo o;
	public OligoTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
		o = new Oligo("AAAAACAGGTGnNNNgtatgATatA");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		o = null;
	}
	
	public void testOligo()
	{
		o.reject("GC Count is too low: the Minimum Acceptable GC Count was 25, but the actual GC Count is 24");
		assertTrue(o.getReasonForRejection().equals("GC Count is too low: the Minimum Acceptable GC Count was 25, but the actual GC Count is 24"));
		o.reject("Complemented the Enzyme amylase:ACTAA", "amylase:ACTAA");
		assertTrue(o.getReasonForRejection().equals("Complemented the Enzyme amylase:ACTAA"));
		assertTrue(o.getComplementaryEnzyme().equals("amylase:ACTAA"));
		assertTrue(o.hasBeenRejected());
		assertTrue(o.getSequence().equals("AAAAACAGGTGnNNNgtatgATatA"));
		assertTrue(o.toString().equals("AAAAACAGGTGnNNNgtatgATatA"));
		assertTrue(o.getComplementaryEnzyme().equals("amylase:ACTAA"));
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new OligoTest("testOligo"));
		return suite;
	}

}
