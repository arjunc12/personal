package solPicker.filters;

import java.util.ArrayList;
import solPicker.job.Oligo;


/**
 * This filter discards oligos whose temperatures are not within a certain
 * range, as specified by the user. The default minimum temperature is 77, and
 * the default maximum is 81. The filter uses an algorithm to calculate the
 * temperature based on the number of G's and C's in each oligo.
 * 
 * @version $Id: TMFilter.java,v 1.5 2009/10/28 21:14:05 tizatt Exp $
 * 
 */

public class TMFilter extends Filter {
	public TMFilter() {
		rejectedOligos = new ArrayList<Oligo>();
		minTM = 77;
		maxTM = 81;
		checkValues();
	}

	/**
	 * Constructs a TM Filter with the min TM Score set equal to the first
	 * parameter and the max TM Score set equal to the second TM Score.
	 * 
	 * @param min
	 *            the minimum acceptable TM Score
	 * @param max
	 *            the maximum acceptable TM Score
	 */
	public TMFilter(double min, double max) {
		rejectedOligos = new ArrayList<Oligo>();
		minTM = min;
		maxTM = max;
	}

	/**
	 * Takes an ArrayList of oligos as a parameter and filters out all oligos
	 * with a temperature greater than the specified maxmimum, or less than the
	 * specified minimum.
	 * 
	 * @param validOligos
	 *            the ArrayList of oligos to be filtered
	 * @return an ArrayList of oligos whose TM scores meet the parameters
	 */

	public ArrayList<Oligo> filter(ArrayList<Oligo> validOligos) {
	
		for (int i = 0; i < validOligos.size(); i++) {
			Oligo o = validOligos.get(i);
			double tm = getTMScore(o);
			o.addFilterValue(this.getFilterType(), tm);
			if (tm < minTM || tm > maxTM) {
				String reason = "";
				if (tm < minTM)
					reason += "TM Score is too low.  The minimum acceptable score was "
							+ minTM;
				else
					reason += "TM Score is too high.  The maximum acceptable score was "
							+ maxTM;
				reason += " but the actual TM Score was " + tm;
				o.reject(reason);
				validOligos.remove(i--);
				rejectedOligos.add(o);
				invalidOligos.add(o);
			}
		}
		return validOligos;
	}

	/**
	 * Uses the GC count to determine the temperature of the oligo, based on an
	 * existing formula
	 * 
	 * @param o
	 *            the oligo to calculate the TM score
	 */

	private double getTMScore(Oligo o) {
		String sequence = o.getSequence();
		int gc = 0;
		for (int i = 0; i < sequence.length(); i++) {
			String letter = sequence.charAt(i) + "";
			if (letter.equalsIgnoreCase("c") || letter.equalsIgnoreCase("g"))
				gc++;
		}
		gc -= 16.4;
		gc *= 41;
		gc /= sequence.length();
		gc += 64.9;
		return gc;
	}

	
	
	public boolean checkValues() {
		return true;
	}
	
	/**
	 * returns filter type
	 * 
	 * @return filter type
	 */
	public String getFilterType() {
		return "TM Score Filter";
	}

	/**
	 * returns oligos that were filtered out because of their TM score
	 * 
	 * @return oligos filtered out due to TM score
	 */
	public ArrayList<Oligo> getRejectedOligos() {
		return rejectedOligos;
	}

	
	public String getValue(Oligo o)
	{
		return getTMScore(o) + "";
	}
	
	public String toXML() {
		String output = "";
		output += "<Filter Type=\""+getFilterType()+"\">\n";
		output += "<Parameter Constraint=\"min"+"\" Value=\""+minTM+"\"/>\n";
		output += "<Parameter Constraint=\"max"+"\" Value=\""+maxTM+"\"/>\n";
		output += "</Filter>\n";
		return output;	
	}
	

	/**
	 * The minimum temperature allowed by the filter
	 */
	private double minTM;
	/**
	 * The maximum temperature allowed by the filter
	 */
	private double maxTM;
	

	/**
	 * a list of oligos rejected by the temperature filter
	 */
	private ArrayList<Oligo> rejectedOligos;

}
