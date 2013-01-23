package solPicker.job;

import java.util.HashMap;

/**
 * Each oligo, with a unique id number, keeps track of its filters and its value
 * for each filter applied to it (e.g. its gc percent). Each oligo contains its
 * sequence, and has methods to compute its lowercase count, n count, gc
 * percent, fail score, et cetera when a filter calls upon it to do so. After an
 * oligo fails a filter, it keeps track of the reason it was rejected.
 * 
 * @author Arjun Chandrasekhar, Tyler Izatt
 * @version $Id: Oligo.java,v 1.7 2009/09/16 20:41:52 tizatt Exp $
 */
public class Oligo {
	/**
	 * Creates an Oligo object with a DNA sequence equal to the parameter
	 * 
	 * @param seq
	 *            the DNA sequence of the oligo
	 */
	public Oligo(String seq) {
		if(seq != null)
			sequence = seq;
		else
			sequence = " ";
		filterValues = new HashMap<String, Double>();
		reasonForRejection = "This oligo was not rejected";
		complementaryEnzyme = "this oligo did not complement any enzyme";
		hasBeenRejected = false;
		blasted = false;
		id = ID;
		ID++;
	}

	/**
	 * Notifies the oligo object that it has been rejected and provides the
	 * reason why
	 * 
	 * @param reason
	 *            reason why the oligo was rejected
	 */
	public void reject(String reason) {
		reasonForRejection = reason;
		hasBeenRejected = true;
		blasted = false;
	}

	/**
	 * Notifies the oligo that it has been rejected, provides the reason why,
	 * and provides the complementary enzyme that caused it to be rejected
	 * 
	 * @param reason
	 *            reason why the oligo was rejected
	 * @param enzyme
	 *            complementay enzyme that causes the oligo to be rejected
	 */
	public void reject(String reason, String enzyme) {
		reasonForRejection = reason;
		complementaryEnzyme = enzyme;
		hasBeenRejected = true;
	}

	public void blasted(){
		blasted = true;
	}
	
	public boolean wasBlasted(){
		return blasted;
	}

	/**
	 * Returns the enzyme that complemented the oligo; Returns an empty String
	 * if no enzyme has complemented the Oligo
	 * 
	 * @return the oligo's complementary enzyme
	 */
	public String getComplementaryEnzyme() {
		return complementaryEnzyme;
	}

	/**
	 * Returns the reason that the oligo was rejected; returns
	 * "This oligo was not rejected" if the oligo has not been rejected
	 * 
	 * @return the oligo's reason for rejection
	 */
	public String getReasonForRejection() {
		return reasonForRejection;
	}

	/**
	 * returns the nucleotide sequence of the oligo
	 * 
	 * @return the oligo's nucleotide sequence
	 */
	public String getSequence() {
		return sequence;
	}

	/**
	 * Gives a string representation of the oligo by returning the sequence
	 */
	public String toString() {
		// if(hasBeenRejected)
		// return sequence + " " + reasonForRejection;
		return sequence;
	}
	
	public String toFasta(){
		return "";
	}

	/**
	 * Returns true if the oligo has been rejected at some point, false if not
	 * 
	 * @return whether or not the oligo has been rejected
	 */
	public boolean hasBeenRejected() {
		return hasBeenRejected;
	}

	/**
	 * Returns the oligo's unique ID Number
	 * 
	 * @return the oligo's ID Number
	 */
	public int getID() {
		return id;
	}

	public static void resetOffsetIDs() {
		ID = 1;
	}

	public void setQID(String QID) {
		qID = QID;
	}

	public String getQID() {
		return qID;
	}
	
	/**
	 * Adds a filter value to this oligo.
	 * @param filterType
	 * @param percent
	 */
	public void addFilterValue(String filterType, double percent) {
		// TODO Auto-generated method stub
		filterValues.put(filterType, percent);
	}
	
	/**
	 * Gets filter values for this oligo.  Returns null if this filter was not run.
	 * @param filterType
	 * @return
	 */
	public Object getFilterValue(String filterType){
		return filterValues.get(filterType);
	}
	
	
	public String toXML(){
		return "";
	}

	private HashMap<String, Double> filterValues;
	private boolean blasted;
	private String sequence;
	private String reasonForRejection;
	private boolean hasBeenRejected;
	private String complementaryEnzyme;
	private int id;
	private static int ID = 1;
	private String qID;

}
