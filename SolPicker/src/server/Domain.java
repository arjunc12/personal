package server;

import java.net.URL;

import solPicker.job.Query;
import solPicker.job.Range;


/**
 * This abstract class is the superclass of all the classes that retrieve
 * nucleotide sequences from the different genome browsers.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: Domain.java,v 1.9 2009/09/04 22:19:33 tizatt Exp $
 */
public abstract class Domain {
	/**
	 * Constructs a Domain object with instance fields initialized to the values
	 * of the parameters
	 * 
	 * @param chr
	 *            - chromosome number
	 * @param loc1
	 *            - start position along the chromosome
	 * @param loc2
	 *            - ending position along the chromosome
	 * @param organism
	 *            - target organism
	 */
	public Domain(String chr, String loc1, String loc2, String organism) {
		first = loc1;
		second = loc2;
		this.chr = chr;
		// middle = "";
		urlStr = "";
		this.organism = organism;
	}

	/**
	 * Constructs a Domain Object by taing a Query as a parameter and setting
	 * all the instance fields equal to the values of the corresponding fields
	 * in the Query Object
	 * 
	 * @param q
	 *            the Query whose DNA is beng analyzed
	 */
	public Domain(Query q) {
		if (!isEmpty(q.getChromosomeIdentifier())) {
			chr = q.getChromosomeIdentifier();
		}
		if (!isEmpty(q.getEnd())) {
			second = q.getEnd();
		}
		if (!isEmpty(q.getStart())) {
			first = q.getStart();
		}
		if (!isEmpty(q.getOrganism())) {
			organism = q.getOrganism();
		}
		if (!isEmpty(q.get5Padding()))
			p5 = q.get5Padding();
		if (!isEmpty(q.get3Padding()))
			p3 = q.get3Padding();
		urlStr = "";
	}

	public Domain(Range range) {
		if (!isEmpty(range.getChromosomeNumber())) {
			chr = range.getChromosomeNumber();
		}
		if (!isEmpty(range.getEndBase())) {
			first = range.getEndBase();
		}
		if (!isEmpty(range.getStartBase())) {
			second = range.getStartBase();
		}
		if (!isEmpty(range.getOrganism())) {
			organism = range.getOrganism();
		}
		urlStr = "";

	}

	public abstract String getDnaFor();
	/**
	 * Returns the start position of the sequence along the chromosome
	 * 
	 * @return - start position along the chromosome
	 */
	public String getFirst() {
		return first;
	}

	/**
	 * Returns the end position of the sequence along the chromosome
	 * 
	 * @return - end position along the chromosome
	 */
	public String getSecond() {
		return second;
	}

	// public String getMiddle(){return middle;}

	/**
	 * Returns the chromosome number
	 * 
	 * @return - chromosome number
	 */
	public String getChromosome() {
		return chr;
	}

	/**
	 * Returns the url containing the fasta sequence
	 * 
	 * @return - url containing the fasta sequence
	 */
	public URL getURL() {
		try {
			createURL();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return finalUrl;
	}

	// public String getURL(){return urlStr;}

	public String getOrganism() {
		return organism;
	}

	/**
	 * Changes the URL instance field to the fit the parameter
	 * 
	 * @param url
	 *            the new url containing the sequence
	 */
	public void setURL(String url) {
		urlStr = url;
	}

	/**
	 * Verifies that the human chromosome identifier is valid.
	 * 
	 * @return - whether or not the chromosome is valid
	 */
	public boolean isHumanChromosomeValid() {
		if (chr.equalsIgnoreCase(X_CHROMOSOME)
				|| chr.equalsIgnoreCase(Y_CHROMOSOME))
			return true;
		try {
			int x = Integer.parseInt(chr);
			for (int i = 1; i <= NUM_OF_HUMAN_CHROMOSOMES; i++) {
				if (x == i)
					return true;
			}
		} catch (NumberFormatException n) {
			return false;
		}
		return false;
	}

	/**
	 * Formats the location so that when added to the url the correct sequence
	 * will be produced
	 * 
	 * @param - loc, the location that is formatted
	 * @return - formatted location
	 */
	// public abstract String formatLocation(String loc);
	/**
	 * Checks whether a string is empty or null.
	 * 
	 * @param s
	 *            - a String object
	 * @return - true if s is empty or null, false if s is not empty or null
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	/**
	 * Creates the URL that will produce the website with the correct nucleotide
	 * sequence
	 * 
	 * @throws Exception
	 */
	protected abstract void createURL() throws Exception;

	// protected abstract void makeURL() throws Exception;
	// public abstract void createURL() throws Exception;

	/**
	 * Removes the html header from the fasta sequence so that it includes only
	 * the nucleotide sequence
	 * 
	 * @param fasta
	 *            - the fasta sequence produced from the URL
	 * @return - the sequence of nucleotides with the html header removed
	 */
	public abstract String cutHeader(String fasta);

	/**
	 * Retrieves the name of the Domain
	 * 
	 * @return the Domain name
	 */
	public abstract String getDomain();

	/**
	 * the url used by each Domain class to retrieve sequence information
	 */
	protected URL finalUrl;
	/**
	 * the base url which does not contain the query information
	 */
	protected String urlStr;
	/**
	 * chromosome identifier for the organism being queried.
	 */
	protected String chr = "";
	/**
	 * The start base of the query
	 */
	protected String first = "";
	/**
	 * The end base of the query
	 */
	protected String second = "";
	/**
	 * The name of the organism being queried
	 */
	protected String organism = "";
	/**
	 * Quantity of padding on the 5' end of the query
	 */
	protected String p5 = "0";
	/**
	 * Quantity of padding on the 3' end of the query
	 */
	protected String p3 = "0";
	/**
	 * Number of "numbered" human chromosomes, excludes "x" and "y"
	 */
	protected final int NUM_OF_HUMAN_CHROMOSOMES = 22;
	protected final String X_CHROMOSOME = "X";
	protected final String Y_CHROMOSOME = "Y";
}
