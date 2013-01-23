package solPicker.filters;

import java.util.ArrayList;
import solPicker.ErrorReporter;
import solPicker.job.Oligo;


/**
 * The program retrieves the specified sequences from online genome databases.
 * These databases mask repeated regions of the genome by casting the sequence
 * in lowercase letters. This filter discards all oligos that have more than a
 * certain number of repeated, or lowercase, bases. The user may specify a
 * maximum, but if they do not, the default is set at 0.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: LowerCaseFilter.java,v 1.6 2009/12/15 21:14:05 tizatt Exp $
 */
public class LowerCaseFilter extends Filter {
	/**
	 * Creates a Lower Case Filter Object with the maximum acceptable lower case
	 * count set equal to the parameter
	 * 
	 * @param maxL
	 *            the maximum acceptable number of lower case letters in an
	 *            oligo's sequence
	 */
	public LowerCaseFilter(int maxL) {
		// super();
		maxLower = maxL;
		rejectedOligos = new ArrayList<Oligo>();
		rejectedOligos.ensureCapacity(100000);
		checkValues();
	}

	/**
	 * Retrieves the valid oligos and determines the number of lowercase bases
	 * in each and filters out the Oligos that have more lowercase letters than
	 * the specified maximum
	 * 
	 * @return an ArrayList of Oligos with lowercase counts that are less than a
	 *         certain specified maximum
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> validOligos) {
		for (int i = 0; i < validOligos.size(); i++) {
			Oligo o = validOligos.get(i);
			int numLower = getLowerCaseCount(o);
			o.addFilterValue(this.getFilterType(),numLower);

			/**
			 * If the number of lowercase bases exceeds the maximum, the oligo
			 * is discarded, and moved from the valid oligo list to the invalid
			 * oligo list
			 */
			if (numLower > maxLower) {
				o
						.reject("too many lower case letters.  The maximum acceptable lowercase count was "
								+ maxLower
								+ " but the actual lowerCaseCount was "
								+ numLower);
				validOligos.remove(i--);
				invalidOligos.add(o);
				rejectedOligos.add(o);
			}
		}
		return validOligos;
	}

	/**
	 * Takes an Oligo as a parameter and calculates its lower case letter count
	 * 
	 * @param o
	 *            the oligo whose lower case letter count is to be calculated
	 * @return the amount of lower case letters present in the oligo's sequence
	 */
	private int getLowerCaseCount(Oligo o) {
		String sequence = o.getSequence();
		int lowerCount = 0;
		for (int i = 0; i < sequence.length(); i++) {
			String nucleotide = sequence.charAt(i) + "";
			String nucleotideUpper = nucleotide.toUpperCase();
			if (!nucleotide.equals(nucleotideUpper) && !nucleotide.equals("n"))
				lowerCount++;
		}
		return lowerCount;
	}

	
	public boolean checkValues() {
		
		if(maxLower<0)
			ErrorReporter.reportError(this, "Filter : "+getFilterType()+" max ("+maxLower+") must be between 0 and 100.");
		return true;
	}
	
	/**
	 * The oligos rejected in this filter keep track of the filter that rendered
	 * its failure
	 * 
	 * @return the oligos that have been rejected by the lowercase filter
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
		return "lowercase";
	}

	
	public String getValue(Oligo o)
	{
		return getLowerCaseCount(o) + "";
	}

	// private static final String FILTER_TYPE = "Lower Case Filter";
	/**
	 * The maximum number of lowercase bases allowed by this filter.
	 */
	private int maxLower;
	/**
	 * A list containing oligos that were rejected by this filter
	 */
	private ArrayList<Oligo> rejectedOligos;
	
	public String toXML() {
		String output="";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"max\""+" Value=\""+maxLower+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}
}
