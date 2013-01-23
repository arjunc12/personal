/**
 * 
 */
package filtersTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import solPicker.filters.BlastFilter;
import solPicker.job.Oligo;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author tizatt
 * 
 */
public class BlastFilterTest extends TestCase {


	public BlastFilterTest(String name) {
		super(name);
	}

	/**
	 * @throws java.lang.Exception
	 */
	public void setUp() throws Exception {
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new java.util.Date();
	}

	/**
	 * @throws java.lang.Exception
	 */
	public void tearDown() throws Exception {}

	public void test1() {
		this.generateSequences(50, 70);
		bf = new BlastFilter();
		oligos = bf.filter(oligos);
		for (int i = 0; i < oligos.size(); i++) {
			assertTrue(oligos.get(i).wasBlasted());
		}

	}

	public void test2() {
		System.out.println("Before : " + dateFormat.format(date));
		this.generateSequences(2000, 70);
		bf = new BlastFilter();
		oligos = bf.filter(oligos);

		for (int i = 0; i < oligos.size(); i++) {
			assertTrue(oligos.get(i).wasBlasted());
		}
		System.out.println("After : " + dateFormat.format(date));
	}

	public void generateSequences(int numOfSequences, int length) {
		oligos = new ArrayList<Oligo>();
		for (int i = 0; i < numOfSequences; i++) {
			String sequence = "";
			Random random = new Random();
			for (int j = 0; j < length; j++) {
				int n = random.nextInt(4);
				if (n == 0)
					sequence += "a";
				else if (n == 1)
					sequence += "g";
				else if (n == 2)
					sequence += "c";
				else if (n == 3)
					sequence += "t";
			}

			oligos.add(new Oligo(sequence));
		}
	}


	/**
	 * @deprecated
	 */

	@SuppressWarnings("unused")
	private void readSequences() {
		try {
			Scanner scanner = new Scanner(new File(sequenceInputFile));
			scanner.useDelimiter(System.getProperty("line.separator"));
			String header = "";
			String sequence = "";

			while (scanner.hasNext()) {
				String line = scanner.next();
				if (line.startsWith(">") && header.equals("")) {
					header = line;
				} else if (line.startsWith(">")) {
					oligos.add(new Oligo(sequence));
					sequence = "";
					header = line;
				} else {
					sequence += line.trim();
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new BlastFilterTest("test1"));
		suite.addTest(new BlastFilterTest("test2"));

		return suite;
	}
	
	

	private BlastFilter bf;
	private DateFormat dateFormat;
	private java.util.Date date;
	private ArrayList<Oligo> oligos;
	private String sequenceInputFile;
}
