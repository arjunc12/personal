package solPicker.filters;

import java.util.ArrayList;
import solPicker.ErrorReporter;
import solPicker.job.Oligo;


/**
 * For a variety of reasons, many of them purely practical, an ideal oligo has a
 * good balance of GC and AT base pairs. This filter allows the user to set a
 * minimum and a maximum GC percent in order to meet their particular research
 * needs. The default values are 45 and 60 percent, respectively.
 * 
 * @author Arjun Chandrasekhar
 * @version $Id: GCFilter.java,v 1.7 2009/12/15 21:14:05 tizatt Exp $
 */

public class GCFilter extends Filter {
	/**
	 * Creates a GC Percent Filter object with minimum and maximum GC
	 * percentages set equal to the parameters
	 * 
	 * @param min
	 *            the mimimum acceptable GC percentage in an oligo's sequence
	 * @param max
	 *            the maximum acceptable GC percentage in an oligo's sequence
	 */

	public GCFilter(double min, double max) {
		// super();
		minGC = min;
		maxGC = max;
		rejectedOligos = new ArrayList<Oligo>();
		checkValues();
	}

	/**
	 * The number of g's and c's is counted and divided into the total number of
	 * bases to determine the gc percent. All oligos with a GC percent that is
	 * not within the acceptable range is filtered out.
	 * 
	 * @return an ArrayList of Oligos with GC percentages that fall within a
	 *         certain range
	 */
	public ArrayList<Oligo> filter(ArrayList<Oligo> oligos) {
		for (int i = 0; i < oligos.size(); i++) {
			Oligo o = oligos.get(i);
			double percent = getGCPercent(o);
			o.addFilterValue(this.getFilterType(),percent);
			/**
			 * If the gc percent is above the maximum or below the minimum, it
			 * is removed from the valid oligo list and added to the rejected
			 * oligo list. Each oligo keeps track of the reason it was rejected
			 * (filter and value).
			 */
			if (percent > maxGC) {
				o
						.reject("gc percent is too high.  The maximum acceptable percent was "
								+ maxGC
								+ " but the actual percent was "
								+ percent);
				oligos.remove(i--);
				invalidOligos.add(o);
				rejectedOligos.add(o);
			} else if (percent < minGC) {
				o
						.reject("gc percent is too low.  The minimum acceptable percent was "
								+ minGC
								+ " but the actual percent was "
								+ percent);
				oligos.remove(i--);
				invalidOligos.add(o);
				rejectedOligos.add(o);
			}
		}
		return oligos;
	}

	/**
	 * Takes an Oligo as a parameter and calculates the Oligo's GC Percentage
	 * 
	 * @param o
	 *            the Oligo whose GC percentage is to be calculated
	 * @return the oligo's GC percentage
	 */
	private double getGCPercent(Oligo o) {
		String sequence = o.getSequence();
		double gcCount = 0;
		for (int i = 0; i < sequence.length(); i++) {
			String nucleotide = sequence.charAt(i) + "";
			if (nucleotide.equalsIgnoreCase("c")
					|| nucleotide.equalsIgnoreCase("g"))
				gcCount++;
		}
		return ((gcCount / sequence.length()) * 100);
	}
	
	
	public boolean checkValues(){
		
			if(minGC>maxGC){
				ErrorReporter.reportError(this,"Filter : "+getFilterType()+" min ("+minGC+") is greater than max("+maxGC+").");
				return false;
			}
			
			if(minGC<0 || minGC>100)
				ErrorReporter.reportError(this, "Filter : "+getFilterType()+" min ("+minGC+") must be between 0 and 100.");
			if(maxGC<0 || maxGC>100)
				ErrorReporter.reportError(this, "Filter : "+getFilterType()+" max ("+maxGC+") must be between 0 and 100.");
			return true;
	}

	/**
	 * retrieves the list of oligos that have been filtered out because of their
	 * GC Percentages
	 * 
	 * @return an ArrayList of Oligos that have been rejected by the GC Filter
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
		return "gcpercent";
	}
	
	public String getValue(Oligo o)
	{
		return "" + getGCPercent(o);
	}
	
	public String toXML(){
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"min"+"\" Value=\""+minGC+"\"/>\n";
		output += "<Parameter Constraint=\"max"+"\" Value=\""+maxGC+"\"/>\n";
		output += "</Filter>\n";
		return output;
	}

	/**
	 * The minimum gc percentage allowed by this filter
	 */
	private double minGC;
	/**
	 * The maximum gc percentage allowed by this filter
	 */
	private double maxGC;
	/**
	 * A list containing oligos that were rejected by this filter
	 */
	private ArrayList<Oligo> rejectedOligos;
}
