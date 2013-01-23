package solPicker.filters;

import java.util.ArrayList;

import solPicker.ErrorReporter;
import solPicker.job.Oligo;

/**
 * There are many areas of the genome that still have not been entirely
 * sequenced, most notably the centromere and telemere regions of each
 * chromosome. The genome browsers indicate these unknown bases with the letter
 * “n”. It is impractical to create an oligo with a large number of unknown
 * bases in its sequence, so this filter takes care of the problem. The user may
 * specify a maximum number of “n’s” allowed per oligo. If no such maximum is
 * provided, it will be set at 0. All oligos with more unknown than allowed are
 * discarded.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: NCountFilter.java,v 1.9 2009/12/15 21:14:05 tizatt Exp $
 */
public class NCountFilter extends Filter {
	/**
	 * Creates an N Count Filter with a maximum N Count set equal to the
	 * parameter
	 * 
	 * @param max
	 *            the maximum acceptable number of ocurrences of the letter N in
	 *            an oligo's sequence
	 */
	public NCountFilter(int max) {
		// super();
		maxN = max;
		rejectedOligos = new ArrayList<Oligo>();
		checkValues();
	}

	/**
	 * From the list of valid oligos, the number of n's is calculated for each
	 * oilgo. The Oligos with more occurrences of the letter N than the
	 * specified maximum are filtered out.
	 * 
	 * @return an ArrayList of Oligos with N Counts that are less than a
	 *         specified maximum
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> validOligos) {
		for (int i = 0; i < validOligos.size(); i++) {
			Oligo o = validOligos.get(i);
			int numN = getNCount(o);
			o.addFilterValue(this.getFilterType(),numN);
			/**
			 * If the n count is greater than the maximum allowable for a
			 * particular oligo, that oligo is transferred to the rejected oligo
			 * list. Each oligo keeps track of the reason it was rejected.
			 */
			if (numN > maxN) {
				o
						.reject("too many ocurrences of the letter n.  The maximum acceptable N count "
								+ maxN + " but the actual N count was " + numN);
				validOligos.remove(i--);
				invalidOligos.add(o);
				rejectedOligos.add(o);
			}
		}
		return validOligos;
	}

	/**
	 * Takes an Oligo as a parameter and calculates the Oligo's N Count
	 * 
	 * @param o
	 *            the Oligo whose N Count is to be calculated
	 * @return the number of ocurrences of the letter N in the oligo's sequence
	 */
	private int getNCount(Oligo o) {
		String sequence = o.getSequence();
		int nCount = 0;
		for (int i = 0; i < sequence.length(); i++) {
			String nucleotide = sequence.charAt(i) + "";
			if (nucleotide.equalsIgnoreCase("N"))
				nCount++;
		}
		return nCount;
	}

	
	public boolean checkValues() {
		if(maxN<=0)
			ErrorReporter.reportError(this, "Filter : "+getFilterType()+" max ("+maxN+") must be greater than 0.");
		return true;
	}
	/**
	 * Retrieves all the Oligos that have been rejected because they had too
	 * many occurrences of the letter N
	 * 
	 * @return the list of Oligos that have been rejected by the N Count Filter
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}

	/**
	 * returns the filter type
	 * 
	 * @return the filter type
	 */
	public String getFilterType() {
		return "ncount";
	}

	public String getValue(Oligo o) {
		return getNCount(o) + "";
	}

	/**
	 * The maximum number of "n"'s allowed by this filter
	 */
	private int maxN;
	/**
	 * A list containing the oligos that were rejected by this filter
	 */
	private ArrayList<Oligo> rejectedOligos;

	public String toXML() {
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"max"+"\" Value=\""+maxN+"\"/>\n";
		output += "</Filter>\n";
		return output;	
	}
}
