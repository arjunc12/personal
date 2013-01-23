package serverTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import solPicker.job.GetRequest;



import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GetRequestTest extends TestCase {

	public GetRequestTest(){}

	public GetRequestTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	/**
	 * tests sendGetRequest() by comparing a real output from the UCSC website with 
	 * the parsed output from sendGetRequest().
	 * @throws IOException
	 */

	public void testSendGetRequest() throws IOException{

		String output="";
		String result ="";
		String url = "http://genome.ucsc.edu/cgi-bin/hgc?&hgSeq.padding3=&c=chr1&l=0&boolshad.hgSeq.revComp=0&g=htcGetDNA2&r=500&boolshad.hgSeq.maskRepeats" +
		"=1&hgSeq.casing=upper&hgSeq.padding5=&hgSeq.maskRepeats=on&hgSeq.repMasking=lower&db=hg18";
		String fileName = "bin/serverTest/gb_test";
		GetRequest gr = new GetRequest(url);
		gr.sendGetRequest();
		result = gr.getResult();

		try {
			File file = new File(fileName);
			Scanner scanner = new Scanner(file);
			scanner.useDelimiter("\n|\r\n|\r|\f");
			while (scanner.hasNext()) {
				output += scanner.next();
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assertTrue(output.equals(result));

		String output2 = "";
		String result2 = "";
		String url2 = "http://www.ensembl.org/Homo_sapiens/Location/Export?output=fasta;r=9:12398-15129;strand=1;time=1248472347.50336;genomic=hard_masked;" +
		"_format=Text";
		String fileName2 = "bin/serverTest/gb_test3";
		GetRequest gr2 = new GetRequest(url2);
		gr2.sendGetRequest();
		result2 += gr2.getResult();
		try
		{
			File file2 = new File(fileName2);
			Scanner scanner2 = new Scanner(file2);
			scanner2.useDelimiter("\n|\r\n|\r|\f");
			while(scanner2.hasNext())
			{
				output2 += scanner2.next();
			}
			scanner2.close();
		}
		catch(IOException i)
		{
			i.printStackTrace(System.err);
		}
		assertTrue(output2.equals(result2));
	}


	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		suite.addTest(new GetRequestTest("testSendGetRequest"));
		return suite;
	}

}
