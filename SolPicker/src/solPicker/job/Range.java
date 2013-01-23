package solPicker.job;

import java.util.ArrayList;

/**
 * This class contains queries that have been joined together, so that fewer
 * requests will be sent to the web server. Each ChromosomeQuery Object is
 * distinguished by chromosome number/letter as well as location on the
 * chromosome. Queries that are within a certain distance of each other on a
 * chromosome may be joined together into a ChromosomeQuery. A ChromosomeQuery
 * object shares the same sequence.
 * 
 * @author Tyler Izatt
 * @version $Id: Range.java,v 1.1 2009/08/18 21:31:41 tizatt Exp $
 */
public class Range {

	/**
	 * Constructor
	 */
	public Range(Query q) {
		queries = new ArrayList<Query>();
		addQuery(q);
		setStartBase(q.getStart());
		setEndBase(q.getEnd());
		setChromosomeNumber(q.getChromosomeIdentifier());
		setOrganism(q.getOrganism());
	}

	/**
	 * 
	 * @return - DNA sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * 
	 * @return - a list of Query objects
	 */
	public ArrayList<Query> getQueries() {
		return queries;
	}

	/**
	 * adds a query to queries list
	 * 
	 * @param q
	 *            - a Query Object to be added
	 */
	public void addQuery(Query q) {
		queries.add(q);
		if (compareStringAsInt(q.getStart(), startBase) < 0) {
			startBase = q.getStart();
		}

		if (compareStringAsInt(q.getEnd(), endBase) > 0) {
			endBase = q.getEnd();
		}

	}

	/**
	 * Sets the chromosome start location
	 * 
	 * @param startBase
	 *            the query's new start position
	 */
	public void setStartBase(String startBase) {
		this.startBase = startBase;
	}

	/**
	 * Sets the chromosome end location
	 * 
	 * @param endBase
	 *            the query's new end base
	 */
	public void setEndBase(String endBase) {
		this.endBase = endBase;
	}

	public void setChromosomeNumber(String chr) {
		chromosomeNumber = chr;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	/**
	 * Gets the chromosome start location
	 * 
	 * @return - chromosome start location
	 */
	public String getStartBase() {
		return startBase;
	}

	/**
	 * Gets the chromosome end location
	 * 
	 * @return - chromosome end location
	 */
	public String getEndBase() {
		return endBase;
	}

	public String getChromosomeNumber() {
		return chromosomeNumber;
	}

	public String getOrganism() {
		return organism;
	}

	/**
	 * 
	 * @return - integer representing size of the query object
	 */
	public int size() {
		return queries.size();
	}

	public int compareStringAsInt(String a, String b) {
		int i = Integer.parseInt(a);
		int j = Integer.parseInt(b);

		if (i < j) {
			return -1;
		} else if (i == j) {
			return 0;
		} else
			return 1;
	}

	public String toString() {
		String range = "";
		for (int i = 0; i < queries.size(); i++) {
			range += queries.get(i).toString() + ", ";
		}
		return range;
	}

	public boolean equals(Range r) {
		if (r.getStartBase().equals(startBase)
				&& r.getEndBase().equals(endBase)) {
			return true;
		}
		return false;
	}

	private String organism;
	// The chromosome number/letter
	private String chromosomeNumber;
	// The chromosome start location
	private String startBase = "0";
	// The chromosome end location
	private String endBase = "0";
	// A list representing Query objects
	public ArrayList<Query> queries;
	// DNA sequence
	public String sequence;

}