package server.ucsc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import server.Domain;
import solPicker.ErrorReporter;
import solPicker.job.Query;
import solPicker.job.Range;

/**
 * This class accesses the UCSC website to retrieve sequence information using
 * chromosome locations.
 * 
 * Valid organism names are as follows:
 * 
 * @author Tyler Izatt
 * @version $Id: GetUCSCDna.java,v 1.7 2009/10/14 17:38:14 achandra Exp $
 * 
 */
public class GetUCSCDna extends Domain {
	/**
	 * Creates a GetUCSCDna object with chromosome number, start position, and
	 * end location set equal to the parameters
	 * 
	 * @param chr
	 *            chromosome number/letter
	 * @param loc1
	 *            chromosome start location
	 * @param loc2
	 *            chromosome end location
	 */
	public GetUCSCDna(String chr, String loc1, String loc2) {
		super(chr, loc1, loc2, null);
		urlStr = "http://genome.ucsc.edu/cgi-bin/hgc?";
		init();
	}

	/**
	 * Creates a GetUCSCDna Object with the Query passed to the super class
	 * constructor
	 * 
	 * @param q
	 *            the Query that is being analyzed
	 */
	public GetUCSCDna(Query q) {
		super(q);
		if (!isEmpty(q.get3Padding())) {
			padding3 = q.get3Padding();
		}
		if (!isEmpty(q.get5Padding())) {
			padding5 = q.get5Padding();
		}
		q.getOrganism();
		urlStr = "http://genome.ucsc.edu/cgi-bin/hgc?";
		init();
	}

	/**
	 * Creates a GetUCSCDna Object with the Range passed to the super class
	 * constructor
	 * 
	 * @param range
	 *            the Range that is being analyzed
	 */
	public GetUCSCDna(Range range) {
		super(range);
		urlStr = "http://genome.ucsc.edu/cgi-bin/hgc?";
		init();
	}

	public String getDnaFor() {
		String result = "";
		result.trim();
		createURL();
		// Send a request to the server
		try {
			// open the url connection
			URLConnection conn = finalUrl.openConnection();
			
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			rd.close();
			result = sb.toString();
		} catch (IOException e) {
			ErrorReporter.reportFatalError(this, "Could not retrieve UCSC dna sequence using : "+finalUrl.toString());
		}
		return (cutHeader(result));
	}

	/**
	 * Initializes the parameters for the creation of a UCSC url.
	 */
	protected void init() {
		int x = Integer.parseInt(first) - 1;
		params = new ArrayList<String>();
		values = new ArrayList<String>();
		params.add("db");
		params.add("hgSeq.casing");
		params.add("g");
		params.add("c");
		params.add("l");
		params.add("r");
		params.add("hgSeq.padding5");
		params.add("hgSeq.padding3");
		params.add("hgSeq.maskRepeats");
		params.add("boolshad.hgSeq.maskRepeats");
		params.add("hgSeq.repMasking");
		params.add("boolshad.hgSeq.revComp");
		values.add(db);
		values.add(casing);
		values.add(g);
		values.add("chr"+chr);
		values.add(x+"");
		values.add(second);
		values.add(padding5);
		values.add(padding3);
		values.add(maskRepeats);
		values.add(boolShadeRepeats);
		values.add(repMasking);
		values.add(revComp);
		}

	/**
	 * Creates the URL to be searched using a UCSC url (which retrieves a fasta
	 * sequence) and the chromosome locations.
	 */

	protected void createURL() {
		if (!isHumanChromosomeValid()) {
			ErrorReporter.reportFatalError(this, "Chromosome identifier is not valid : "+ chr);
		}
		for(int i = 0; i<params.size(); i++){
			query += "&"+params.get(i)+"="+values.get(i);
		}

		try {
			finalUrl = new URL(urlStr + query);
		} catch (MalformedURLException e) {
			ErrorReporter
					.reportFatalError(this,"GetUCSCDna URL could not be formed correctly : "
							+ urlStr + query);
		}
	}

	/**
	 * Returns a substring representing the header of the fasta file. This
	 * method is used because the sequence needs to be separated from its header
	 * so that it can be split into oligos, but the header is still preserved.
	 * The UCSC getDNA service returns html within the text returns and this needs
	 * to be parsed out.
	 * 
	 * @return - String representing fasta file
	 */
	public String cutHeader(String fasta) {
		String first = getFirst();
		String second = getSecond();
		String str = "<PRE>>hg18_dna range=chr" + chr + ":" + first + "-"
				+ second + " 5'pad=" + p5 + " 3'pad=" + p3
				+ " strand=+ repeatMasking=lower";
		int m = str.length();
		int x = fasta.indexOf(str);
		if (x != -1)
			m = x + m;
		// System.err.println(str);
		String string = fasta.substring(m);
		int z = string.indexOf("</PRE>");
		if (z >= 0)
			string = string.substring(0, z);
		return string;
	}

	/**
	 * Gets the domain name
	 * 
	 * @return - String representing domain name - server.ucsc
	 */
	public String getDomain() {
		return "ucsc";
	}

	/**
	 * Sets the GetDna type equal to the parameter
	 * 
	 * @param g
	 *            - String representing the GetDna type used in server.ucsc
	 */
	public void setG(String g) {
		this.g = g;
	}

	/**
	 * Sets the database name equal to the parameter
	 * 
	 * @param db
	 *            the new database name
	 */
	public void setDatabase(String db) {
		this.db = db;
	}

	/**
	 * Sets the 5' padding equal to the parameter
	 * 
	 * @param padding5
	 *            the new 5' padding
	 */
	public void setPadding5(String padding5) {
		this.padding5 = padding5;
	}

	/**
	 * sets the 3' padding equal to the parameter
	 * 
	 * @param padding3
	 *            the new 3' padding
	 */
	public void setPadding3(String padding3) {
		this.padding3 = padding3;
	}

	/**
	 * sets the reverse complement equal to the parameter
	 * 
	 * @param revComp
	 *            the new reverse complement
	 */
	public void setRevComp(String revComp) {
		this.revComp = revComp;
	}


	/**
	 * The query part of this server.ucsc url.
	 */
	protected String query = "";
	/**
	 * Represents the GetDna type
	 */
	protected String g = "htcGetDNA2";
	/**
	 * The database type (unique to server.ucsc)
	 */
	protected String db = "hg18";
	/**
	 * The amount of padding to be added to the 5' end of the sequence
	 */
	protected String padding5 = "";
	/**
	 * The amount of padding to be added to the 3' end of the sequence
	 */
	protected String padding3 = "";
	/**
	 * Specifies whether repeat segments of the sequence should be masked. This
	 * function is always "on" for SolPicker.
	 */
	protected String maskRepeats = "on";
	/**
	 *  
	 */
	protected String boolShadeRepeats = "1";
	/**
	 * Specifies the case (upper or lower) of the bases in the sequence. "upper"
	 * is used for SolPicker to differentiate from the lower case repeats.
	 */
	protected String casing = "upper";
	/**
	 * Specifies the case (upper or lower) of the repeats segments of the
	 * sequence. "lower" is used for SolPicker.
	 */
	protected String repMasking = "lower";
	/**
	 * Specifies if the revComp should be retrieved. Default is set to "0".
	 */
	protected String revComp = "0";
	protected ArrayList<String> params;
	protected ArrayList<String> values;
}
